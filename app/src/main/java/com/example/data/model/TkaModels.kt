package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "Pelajar TKA",
    val xp: Long = 0L,
    val streakDays: Long = 1L,
    val hearts: Long = 5L,
    val gems: Long = 250L,
    val totalExamsCompleted: Long = 0L,
    val totalRemedialsCompleted: Long = 0L,
    val weakSubtopics: List<String> = emptyList(),
    val masteredUnits: List<String> = emptyList(),
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ExamAttempt(
    @DocumentId val id: String = "",
    val userId: String = "",
    val subjectId: String = "",
    val subjectName: String = "",
    val mode: String = "SIMULASI_TKA", // SIMULASI_TKA, UNIT_PATH, REMEDIAL_DRILL
    val irtScore: Long = 0L,
    val correctCount: Long = 0L,
    val totalQuestions: Long = 30L,
    val durationSeconds: Long = 0L,
    val weakSubtopics: List<String> = emptyList(),
    val strongSubtopics: List<String> = emptyList(),
    val aiAnalysisSummary: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class SavedCustomQuestion(
    @DocumentId val id: String = "",
    val userId: String = "",
    val subjectId: String = "",
    val subtopic: String = "",
    val difficulty: Long = 2L,
    val questionType: String = "SINGLE_CHOICE",
    val stem: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerCode: String = "0",
    val explanation: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

enum class QuestionType(val label: String, val badgeText: String) {
    SINGLE_CHOICE("Pilihan Ganda (1 Jawaban)", "PG Tunggal"),
    MCMA("Pilihan Ganda Kompleks (MCMA - Lebih dari 1 Benar)", "PG Kompleks (MCMA)"),
    TRUE_FALSE_MATRIX("Benar / Salah Pernyataan Majemuk", "Benar / Salah")
}

enum class DifficultyTier(
    val level: Int,
    val label: String,
    val irtWeight: Double,
    val basePoints: Int
) {
    EASY(1, "Level 1 • Mudah", 1.0, 15),
    MEDIUM(2, "Level 2 • Sedang", 1.65, 28),
    HARD(3, "Level 3 • Sulit (HOTS)", 2.45, 45);

    companion object {
        fun fromLevel(level: Int): DifficultyTier = when (level) {
            1 -> EASY
            2 -> MEDIUM
            else -> HARD
        }
    }
}

data class TkaQuestion(
    val id: String,
    val subjectId: String,
    val unitId: String,
    val subtopic: String,
    val difficulty: DifficultyTier,
    val type: QuestionType,
    val stem: String,
    val options: List<String>,
    // For SINGLE_CHOICE: "0".."4"
    // For MCMA: comma-separated indices e.g. "0,2" or "1,3,4"
    // For TRUE_FALSE_MATRIX: comma-separated "B" or "S" for each statement in options e.g. "B,S,B"
    val correctAnswerCode: String,
    val explanation: String
)

data class TkaUnitNode(
    val id: String,
    val subjectId: String,
    val unitNumber: Int,
    val title: String,
    val subtitle: String,
    val subtopics: List<String>,
    val iconName: String
)

data class TkaSubject(
    val id: String,
    val name: String,
    val shortName: String,
    val category: String, // "Wajib" or "Peminatan / Lanjut"
    val primaryColorHex: Long,
    val darkAccentHex: Long,
    val description: String,
    val units: List<TkaUnitNode>
)

data class SubtopicPerformance(
    val subtopic: String,
    val totalQuestions: Int,
    val correctEquivalent: Double,
    val earnedWeightedPoints: Double,
    val maxWeightedPoints: Double
) {
    val masteryPercentage: Int
        get() = if (maxWeightedPoints <= 0.0) 0 else ((earnedWeightedPoints / maxWeightedPoints) * 100.0).toInt().coerceIn(0, 100)

    val isWeak: Boolean
        get() = masteryPercentage < 65
}

data class SessionResultSummary(
    val subject: TkaSubject,
    val mode: String,
    val irtScore: Int, // 0..1000 scale like UTBK/TKA
    val rawCorrectCount: Int,
    val totalQuestions: Int,
    val durationSeconds: Int,
    val earnedXp: Int,
    val earnedGems: Int,
    val subtopicPerformances: List<SubtopicPerformance>,
    val weakSubtopics: List<String>,
    val strongSubtopics: List<String>,
    val questionReviews: List<QuestionReviewItem>
)

data class QuestionReviewItem(
    val question: TkaQuestion,
    val userAnswerCode: String,
    val isFullyCorrect: Boolean,
    val partialCreditRatio: Double,
    val earnedWeightedScore: Double,
    val maxWeightedScore: Double
)
