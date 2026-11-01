package com.example.data.scoring

import com.example.data.model.QuestionReviewItem
import com.example.data.model.QuestionType
import com.example.data.model.SessionResultSummary
import com.example.data.model.SubtopicPerformance
import com.example.data.model.TkaQuestion
import com.example.data.model.TkaSubject
import kotlin.math.roundToInt

object IrtScoringEngine {

    /**
     * Evaluates a user's answer against a TkaQuestion.
     * Returns a pair of (isFullyCorrect, creditRatio in 0.0..1.0).
     */
    fun evaluateAnswer(question: TkaQuestion, userAnswerCode: String): Pair<Boolean, Double> {
        val cleanUser = userAnswerCode.trim()
        val cleanExpected = question.correctAnswerCode.trim()
        if (cleanUser.isEmpty()) return false to 0.0

        return when (question.type) {
            QuestionType.SINGLE_CHOICE -> {
                val isCorrect = cleanUser == cleanExpected
                isCorrect to (if (isCorrect) 1.0 else 0.0)
            }

            QuestionType.MCMA -> {
                val expectedSet = cleanExpected.split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toSet()
                val userSet = cleanUser.split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toSet()

                if (expectedSet.isEmpty() || userSet.isEmpty()) {
                    false to 0.0
                } else if (userSet == expectedSet) {
                    true to 1.0
                } else {
                    val correctSelected = userSet.intersect(expectedSet).size
                    val wrongSelected = (userSet - expectedSet).size
                    // Partial credit if student selected some correct options and didn't spam wrong ones
                    val netCorrect = (correctSelected - wrongSelected).coerceAtLeast(0)
                    val ratio = (netCorrect.toDouble() / expectedSet.size.toDouble()).coerceIn(0.0, 0.85)
                    false to ratio
                }
            }

            QuestionType.TRUE_FALSE_MATRIX -> {
                val expectedList = cleanExpected.split(",").map { it.trim().uppercase() }
                val userList = cleanUser.split(",").map { it.trim().uppercase() }
                if (expectedList.isEmpty() || userList.size != expectedList.size) {
                    false to 0.0
                } else {
                    val matchCount = expectedList.indices.count { i -> expectedList[i] == userList[i] }
                    if (matchCount == expectedList.size) {
                        true to 1.0
                    } else if (matchCount >= expectedList.size - 1 && expectedList.size >= 3) {
                        // Partial credit in TKA Benar/Salah matrix when only 1 statement is missed
                        false to 0.5
                    } else {
                        false to 0.0
                    }
                }
            }
        }
    }

    /**
     * Computes full IRT-style difficulty-weighted score (scale 200..1000) and subtopic weakness breakdown.
     */
    fun calculateSessionSummary(
        subject: TkaSubject,
        mode: String,
        questions: List<TkaQuestion>,
        userAnswers: Map<String, String>,
        durationSeconds: Int
    ): SessionResultSummary {
        var totalEarnedWeighted = 0.0
        var totalMaxWeighted = 0.0
        var rawFullyCorrect = 0

        val reviews = mutableListOf<QuestionReviewItem>()
        val subtopicMap = linkedMapOf<String, MutableList<QuestionReviewItem>>()

        for (q in questions) {
            val userAns = userAnswers[q.id].orEmpty()
            val (isCorrect, creditRatio) = evaluateAnswer(q, userAns)
            if (isCorrect) rawFullyCorrect++

            // Type multiplier: MCMA and TRUE_FALSE_MATRIX have slightly higher discrimination weight in TKA
            val formatMultiplier = when (q.type) {
                QuestionType.SINGLE_CHOICE -> 1.0
                QuestionType.MCMA -> 1.15
                QuestionType.TRUE_FALSE_MATRIX -> 1.2
            }
            val maxWeight = q.difficulty.basePoints * q.difficulty.irtWeight * formatMultiplier
            val earnedWeight = maxWeight * creditRatio

            totalEarnedWeighted += earnedWeight
            totalMaxWeighted += maxWeight

            val reviewItem = QuestionReviewItem(
                question = q,
                userAnswerCode = userAns,
                isFullyCorrect = isCorrect,
                partialCreditRatio = creditRatio,
                earnedWeightedScore = earnedWeight,
                maxWeightedScore = maxWeight
            )
            reviews.add(reviewItem)
            subtopicMap.getOrPut(q.subtopic) { mutableListOf() }.add(reviewItem)
        }

        val normalizedRatio = if (totalMaxWeighted <= 0.0) 0.0 else (totalEarnedWeighted / totalMaxWeighted).coerceIn(0.0, 1.0)
        // TKA / UTBK IRT scale: base 200 up to 1000
        val irtScore = if (questions.isEmpty()) 0 else (200.0 + normalizedRatio * 800.0).roundToInt().coerceIn(200, 1000)

        val subtopicPerformances = subtopicMap.map { (subtopic, items) ->
            SubtopicPerformance(
                subtopic = subtopic,
                totalQuestions = items.size,
                correctEquivalent = items.sumOf { it.partialCreditRatio },
                earnedWeightedPoints = items.sumOf { it.earnedWeightedScore },
                maxWeightedPoints = items.sumOf { it.maxWeightedScore }
            )
        }.sortedBy { it.masteryPercentage }

        val weakSubtopics = subtopicPerformances
            .filter { it.isWeak }
            .map { it.subtopic }
            .ifEmpty {
                // If the user got > 65% everywhere, still offer the lowest-scoring subtopic if < 90%
                subtopicPerformances.filter { it.masteryPercentage < 90 }.take(1).map { it.subtopic }
            }

        val strongSubtopics = subtopicPerformances
            .filter { !it.isWeak }
            .sortedByDescending { it.masteryPercentage }
            .map { it.subtopic }

        val earnedXp = (rawFullyCorrect * 12 + (irtScore / 25)).coerceAtLeast(15)
        val earnedGems = (rawFullyCorrect * 3 + if (irtScore >= 700) 25 else 10)

        return SessionResultSummary(
            subject = subject,
            mode = mode,
            irtScore = irtScore,
            rawCorrectCount = rawFullyCorrect,
            totalQuestions = questions.size,
            durationSeconds = durationSeconds,
            earnedXp = earnedXp,
            earnedGems = earnedGems,
            subtopicPerformances = subtopicPerformances,
            weakSubtopics = weakSubtopics,
            strongSubtopics = strongSubtopics,
            questionReviews = reviews
        )
    }
}
