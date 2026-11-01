package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.DifficultyTier
import com.example.data.model.QuestionType
import com.example.data.model.TkaQuestion
import com.example.ui.components.DuoChunkyCard
import com.example.ui.components.DuoTactileButton
import com.example.ui.theme.DuoBorderGray
import com.example.ui.theme.DuoBorderGrayDark
import com.example.ui.theme.DuoCardSurface
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenDark
import com.example.ui.theme.DuoFeatherGreenLight
import com.example.ui.theme.DuoFlameOrange
import com.example.ui.theme.DuoHeartRed
import com.example.ui.theme.DuoHeartRedDark
import com.example.ui.theme.DuoHeartRedLight
import com.example.ui.theme.DuoInkDark
import com.example.ui.theme.DuoInkMuted
import com.example.ui.theme.DuoRoyalPurple
import com.example.ui.theme.DuoSkyBlue
import com.example.ui.theme.DuoSkyBlueDark
import com.example.ui.theme.DuoSkyBlueLight
import com.example.ui.theme.DuoSunGold
import com.example.ui.theme.DuoWarmBg
import com.example.ui.viewmodel.ActiveQuizState
import com.example.util.SoundEffectsManager

@Composable
fun QuizSessionScreen(
    quizState: ActiveQuizState,
    onSelectAnswer: (String) -> Unit,
    onCheckAnswerInteractive: () -> Unit,
    onNextQuestion: () -> Unit,
    onPrevQuestion: () -> Unit,
    onJumpToQuestion: (Int) -> Unit,
    onToggleFlag: () -> Unit,
    onRequestHint: () -> Unit,
    onSubmitExam: () -> Unit,
    onExitQuiz: () -> Unit
) {
    BackHandler {
        onExitQuiz()
    }

    val currentQuestion = quizState.questions.getOrNull(quizState.currentIndex) ?: return
    val currentAnswer = quizState.userAnswers[currentQuestion.id].orEmpty()
    val isSimulationMode = quizState.mode == "SIMULASI_TKA"
    val progressTarget = (quizState.currentIndex + 1).toFloat() / quizState.questions.size.coerceAtLeast(1).toFloat()
    val animatedProgress by animateFloatAsState(targetValue = progressTarget, label = "quiz_progress")

    val subjectColor = Color(quizState.subject.primaryColorHex)
    val subjectDarkColor = Color(quizState.subject.darkAccentHex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoWarmBg)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Top Header: Close button, Duolingo progress bar, Timer / Hearts
        Surface(
            color = DuoCardSurface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onExitQuiz,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("close_quiz_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Keluar Sesi",
                            tint = DuoInkMuted
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Animated chunky progress bar
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .weight(1f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(50)),
                        color = subjectColor,
                        trackColor = DuoBorderGray,
                        strokeCap = StrokeCap.Round
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    if (isSimulationMode) {
                        // 75-minute official countdown badge
                        val mins = quizState.remainingSeconds / 60
                        val secs = quizState.remainingSeconds % 60
                        val isLowTime = quizState.remainingSeconds < 300
                        Surface(
                            color = if (isLowTime) DuoHeartRedLight else DuoSkyBlueLight,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Timer,
                                    contentDescription = "Sisa Waktu",
                                    tint = if (isLowTime) DuoHeartRed else DuoSkyBlueDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "%02d:%02d".format(mins, secs),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isLowTime) DuoHeartRed else DuoSkyBlueDark
                                )
                            }
                        }
                    } else {
                        // Hearts indicator in interactive Duolingo mode
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Hati",
                                tint = DuoHeartRed,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${quizState.heartsRemaining}",
                                style = MaterialTheme.typography.titleMedium,
                                color = DuoHeartRed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // If Simulasi 30 Soal: show horizontal scrollable Question Number Strip (1..30)
                if (isSimulationMode) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quizState.questions.forEachIndexed { idx, q ->
                            val isCurrent = idx == quizState.currentIndex
                            val isAnswered = !quizState.userAnswers[q.id].isNullOrBlank()
                            val isFlagged = quizState.flaggedQuestionIds.contains(q.id)

                            val bg = when {
                                isCurrent -> subjectColor
                                isFlagged -> DuoSunGold
                                isAnswered -> DuoFeatherGreenLight
                                else -> Color(0xFFF0F2F5)
                            }
                            val textCol = when {
                                isCurrent -> Color.White
                                isFlagged -> DuoInkDark
                                isAnswered -> DuoFeatherGreenDark
                                else -> DuoInkMuted
                            }

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bg)
                                    .border(
                                        width = if (isCurrent) 2.dp else 1.dp,
                                        color = if (isCurrent) subjectDarkColor else DuoBorderGray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        SoundEffectsManager.playTap()
                                        onJumpToQuestion(idx)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = textCol,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Main Question Content Scroll Area
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
            ) {
                // Question Metadata Chips: Number, Subtopic, Difficulty Weight, and Type
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = subjectColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Soal ${quizState.currentIndex + 1}/${quizState.questions.size} • ${currentQuestion.subtopic}",
                                style = MaterialTheme.typography.labelMedium,
                                color = subjectDarkColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        val diffColor = when (currentQuestion.difficulty) {
                            DifficultyTier.EASY -> DuoFeatherGreen
                            DifficultyTier.MEDIUM -> DuoFlameOrange
                            DifficultyTier.HARD -> DuoRoyalPurple
                        }
                        Surface(
                            color = diffColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${currentQuestion.difficulty.label} (${currentQuestion.difficulty.basePoints} pt)",
                                style = MaterialTheme.typography.labelMedium,
                                color = diffColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    if (isSimulationMode) {
                        val isFlagged = quizState.flaggedQuestionIds.contains(currentQuestion.id)
                        Surface(
                            color = if (isFlagged) DuoSunGold else Color(0xFFEDF0F5),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable {
                                SoundEffectsManager.playTap()
                                onToggleFlag()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Flag,
                                    contentDescription = "Ragu-ragu",
                                    tint = if (isFlagged) DuoInkDark else DuoInkMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isFlagged) "Ragu-ragu" else "Tandai",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isFlagged) DuoInkDark else DuoInkMuted
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Format instruction pill
                Surface(
                    color = DuoSkyBlue.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Bentuk Soal TKA: ${currentQuestion.type.label}",
                        style = MaterialTheme.typography.labelMedium,
                        color = DuoSkyBlueDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Question Stem Card
                DuoChunkyCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = subjectColor.copy(alpha = 0.4f),
                    bottomLipColor = subjectColor.copy(alpha = 0.25f),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    Column {
                        Text(
                            text = currentQuestion.stem,
                            style = MaterialTheme.typography.bodyLarge,
                            color = DuoInkDark
                        )

                        if (!isSimulationMode) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        SoundEffectsManager.playTap()
                                        onRequestHint()
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                if (quizState.isHintLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = DuoFlameOrange
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.Lightbulb,
                                        contentDescription = "Petunjuk AI",
                                        tint = DuoFlameOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (quizState.isHintLoading) "Meminta petunjuk Bimo..." else "Butuh Petunjuk Konsep? Tanya Bimo (AI Hint)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DuoFlameOrange
                                )
                            }

                            if (!quizState.quickHintText.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    color = DuoSunGold.copy(alpha = 0.18f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "💡 ${quizState.quickHintText}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DuoInkDark,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Render Options according to TKA Question Type
                when (currentQuestion.type) {
                    QuestionType.SINGLE_CHOICE -> {
                        SingleChoiceOptionsView(
                            question = currentQuestion,
                            selectedCode = currentAnswer,
                            isChecked = quizState.isCurrentChecked,
                            onSelect = onSelectAnswer
                        )
                    }
                    QuestionType.MCMA -> {
                        McmaOptionsView(
                            question = currentQuestion,
                            selectedCode = currentAnswer,
                            isChecked = quizState.isCurrentChecked,
                            onSelect = onSelectAnswer
                        )
                    }
                    QuestionType.TRUE_FALSE_MATRIX -> {
                        TrueFalseMatrixView(
                            question = currentQuestion,
                            selectedCode = currentAnswer,
                            isChecked = quizState.isCurrentChecked,
                            onSelect = onSelectAnswer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Bottom Action Area / Duolingo Feedback Sheet
        Surface(
            color = when {
                !isSimulationMode && quizState.isCurrentChecked && (quizState.isCurrentCorrect || quizState.currentCreditRatio >= 0.5) -> DuoFeatherGreenLight
                !isSimulationMode && quizState.isCurrentChecked -> DuoHeartRedLight
                else -> DuoCardSurface
            },
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                ) {
                    // Instant Explanation Sheet when Checked in UNIT_PATH or REMEDIAL_DRILL
                    AnimatedVisibility(
                        visible = !isSimulationMode && quizState.isCurrentChecked,
                        enter = slideInVertically { it / 2 },
                        exit = slideOutVertically { it / 2 }
                    ) {
                        Column(modifier = Modifier.padding(bottom = 12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (quizState.isCurrentCorrect) Icons.Filled.CheckCircle else Icons.Filled.Lightbulb,
                                    contentDescription = null,
                                    tint = if (quizState.isCurrentCorrect) DuoFeatherGreenDark else DuoHeartRedDark,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = when {
                                        quizState.isCurrentCorrect -> "Luar Biasa! Jawabanmu Tepat 100%!"
                                        quizState.currentCreditRatio >= 0.5 -> "Hampir Sempurna! Skor Parsial (${(quizState.currentCreditRatio * 100).toInt()}%)"
                                        else -> "Kurang Tepat • Kunci: ${formatAnswerKeyReadable(currentQuestion)}"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (quizState.isCurrentCorrect || quizState.currentCreditRatio >= 0.5) DuoFeatherGreenDark else DuoHeartRedDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Pembahasan TKA: ${currentQuestion.explanation}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DuoInkDark
                            )
                        }
                    }

                    if (isSimulationMode) {
                        // Simulation navigation bar: Prev, Next, Submit
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (quizState.currentIndex > 0) {
                                DuoTactileButton(
                                    text = "Sebelumnya",
                                    containerColor = DuoSkyBlue,
                                    lipColor = DuoSkyBlueDark,
                                    modifier = Modifier.weight(1f),
                                    onClick = onPrevQuestion
                                )
                            }
                            if (quizState.currentIndex + 1 < quizState.questions.size) {
                                DuoTactileButton(
                                    text = "Soal Berikutnya",
                                    containerColor = DuoFeatherGreen,
                                    lipColor = DuoFeatherGreenDark,
                                    modifier = Modifier.weight(1f),
                                    onClick = onNextQuestion
                                )
                            } else {
                                DuoTactileButton(
                                    text = "Kumpulkan Ujian (30 Soal)",
                                    containerColor = DuoSunGold,
                                    lipColor = Color(0xFFD99B00),
                                    contentColor = DuoInkDark,
                                    testTag = "submit_exam_button",
                                    modifier = Modifier.weight(1.3f),
                                    onClick = onSubmitExam
                                )
                            }
                        }

                        if (quizState.currentIndex + 1 < quizState.questions.size) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Terjawab: ${quizState.userAnswers.count { it.value.isNotBlank() }}/${quizState.questions.size} soal",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DuoInkMuted
                                )
                                Text(
                                    text = "Selesai & Hitung Skor IRT Sekarang →",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DuoSkyBlueDark,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            SoundEffectsManager.playTap()
                                            onSubmitExam()
                                        }
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }
                    } else {
                        // Interactive Duolingo Check / Continue button
                        if (!quizState.isCurrentChecked) {
                            DuoTactileButton(
                                text = "Periksa Jawaban",
                                enabled = currentAnswer.isNotBlank(),
                                containerColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenDark,
                                testTag = "check_answer_button",
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onCheckAnswerInteractive
                            )
                        } else {
                            val isLast = quizState.currentIndex + 1 >= quizState.questions.size
                            DuoTactileButton(
                                text = if (isLast) "Lihat Hasil & Analisis Kelemahan" else "Lanjut Soal Berikutnya",
                                containerColor = if (quizState.isCurrentCorrect || quizState.currentCreditRatio >= 0.5) DuoFeatherGreen else DuoHeartRed,
                                lipColor = if (quizState.isCurrentCorrect || quizState.currentCreditRatio >= 0.5) DuoFeatherGreenDark else DuoHeartRedDark,
                                testTag = "next_question_button",
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onNextQuestion
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleChoiceOptionsView(
    question: TkaQuestion,
    selectedCode: String,
    isChecked: Boolean,
    onSelect: (String) -> Unit
) {
    val letters = listOf("A", "B", "C", "D", "E")
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        question.options.forEachIndexed { idx, optionText ->
            val idxStr = idx.toString()
            val isSelected = selectedCode == idxStr
            val isCorrectOption = question.correctAnswerCode.trim() == idxStr

            val borderColor = when {
                isChecked && isCorrectOption -> DuoFeatherGreen
                isChecked && isSelected && !isCorrectOption -> DuoHeartRed
                isSelected -> DuoSkyBlue
                else -> DuoBorderGray
            }
            val lipColor = when {
                isChecked && isCorrectOption -> DuoFeatherGreenDark
                isChecked && isSelected && !isCorrectOption -> DuoHeartRedDark
                isSelected -> DuoSkyBlueDark
                else -> DuoBorderGrayDark
            }
            val bgColor = when {
                isChecked && isCorrectOption -> DuoFeatherGreenLight.copy(alpha = 0.55f)
                isChecked && isSelected && !isCorrectOption -> DuoHeartRedLight.copy(alpha = 0.55f)
                isSelected -> DuoSkyBlueLight.copy(alpha = 0.65f)
                else -> DuoCardSurface
            }

            DuoChunkyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("option_card_$idx"),
                backgroundColor = bgColor,
                borderColor = borderColor,
                bottomLipColor = lipColor,
                onClick = { onSelect(idxStr) },
                contentPadding = PaddingValues(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) DuoSkyBlue else Color(0xFFF0F3F8))
                            .border(1.dp, borderColor, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letters.getOrElse(idx) { "${idx + 1}" },
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isSelected) Color.White else DuoInkDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DuoInkDark,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun McmaOptionsView(
    question: TkaQuestion,
    selectedCode: String,
    isChecked: Boolean,
    onSelect: (String) -> Unit
) {
    val selectedSet = selectedCode.split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
    val correctSet = question.correctAnswerCode.split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "☑ Pilih LEBIH DARI SATU pernyataan yang benar (MCMA):",
            style = MaterialTheme.typography.labelLarge,
            color = DuoInkMuted
        )
        question.options.forEachIndexed { idx, optionText ->
            val idxStr = idx.toString()
            val isSelected = selectedSet.contains(idxStr)
            val isCorrectOption = correctSet.contains(idxStr)

            val borderColor = when {
                isChecked && isCorrectOption -> DuoFeatherGreen
                isChecked && isSelected && !isCorrectOption -> DuoHeartRed
                isSelected -> DuoSkyBlue
                else -> DuoBorderGray
            }
            val lipColor = when {
                isChecked && isCorrectOption -> DuoFeatherGreenDark
                isChecked && isSelected && !isCorrectOption -> DuoHeartRedDark
                isSelected -> DuoSkyBlueDark
                else -> DuoBorderGrayDark
            }
            val bgColor = when {
                isChecked && isCorrectOption -> DuoFeatherGreenLight.copy(alpha = 0.55f)
                isChecked && isSelected && !isCorrectOption -> DuoHeartRedLight.copy(alpha = 0.55f)
                isSelected -> DuoSkyBlueLight.copy(alpha = 0.65f)
                else -> DuoCardSurface
            }

            DuoChunkyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mcma_option_$idx"),
                backgroundColor = bgColor,
                borderColor = borderColor,
                bottomLipColor = lipColor,
                onClick = {
                    val nextSet = if (isSelected) selectedSet - idxStr else selectedSet + idxStr
                    val sortedCode = nextSet.mapNotNull { it.toIntOrNull() }.sorted().joinToString(",")
                    onSelect(sortedCode)
                },
                contentPadding = PaddingValues(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) DuoSkyBlue else Color.White)
                            .border(2.dp, if (isSelected) DuoSkyBlueDark else DuoBorderGrayDark, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Text(
                                text = "✓",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DuoInkDark,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TrueFalseMatrixView(
    question: TkaQuestion,
    selectedCode: String,
    isChecked: Boolean,
    onSelect: (String) -> Unit
) {
    val currentSelections = selectedCode.split(",")
        .map { it.trim().uppercase() }
        .let { list ->
            List(question.options.size) { i -> list.getOrNull(i).orEmpty() }
        }
    val expectedSelections = question.correctAnswerCode.split(",")
        .map { it.trim().uppercase() }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Tentukan BENAR atau SALAH untuk setiap pernyataan di bawah ini:",
            style = MaterialTheme.typography.labelLarge,
            color = DuoInkMuted
        )

        question.options.forEachIndexed { idx, statementText ->
            val currentVal = currentSelections.getOrElse(idx) { "" }
            val expectedVal = expectedSelections.getOrElse(idx) { "B" }

            DuoChunkyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tf_statement_$idx"),
                borderColor = if (isChecked) {
                    if (currentVal == expectedVal) DuoFeatherGreen else DuoHeartRed
                } else {
                    DuoBorderGray
                },
                contentPadding = PaddingValues(14.dp)
            ) {
                Column {
                    Text(
                        text = "Pernyataan ${idx + 1}: $statementText",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DuoInkDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val isBenar = currentVal == "B"
                        val isSalah = currentVal == "S"

                        // BENAR Button
                        Surface(
                            color = if (isBenar) DuoFeatherGreen else Color(0xFFF1F4F9),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                2.dp,
                                if (isBenar) DuoFeatherGreenDark else DuoBorderGray
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clickable {
                                    SoundEffectsManager.playTap()
                                    val updated = currentSelections.toMutableList()
                                    updated[idx] = "B"
                                    val code = if (updated.all { it == "B" || it == "S" }) {
                                        updated.joinToString(",")
                                    } else {
                                        updated.map { if (it.isBlank()) "?" else it }.joinToString(",")
                                    }
                                    onSelect(code)
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "BENAR (B)",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isBenar) Color.White else DuoInkDark
                                )
                            }
                        }

                        // SALAH Button
                        Surface(
                            color = if (isSalah) DuoHeartRed else Color(0xFFF1F4F9),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                2.dp,
                                if (isSalah) DuoHeartRedDark else DuoBorderGray
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clickable {
                                    SoundEffectsManager.playTap()
                                    val updated = currentSelections.toMutableList()
                                    updated[idx] = "S"
                                    val code = if (updated.all { it == "B" || it == "S" }) {
                                        updated.joinToString(",")
                                    } else {
                                        updated.map { if (it.isBlank()) "?" else it }.joinToString(",")
                                    }
                                    onSelect(code)
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "SALAH (S)",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSalah) Color.White else DuoInkDark
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatAnswerKeyReadable(question: TkaQuestion): String {
    val letters = listOf("A", "B", "C", "D", "E")
    return when (question.type) {
        QuestionType.SINGLE_CHOICE -> {
            val idx = question.correctAnswerCode.trim().toIntOrNull() ?: 0
            "Opsi ${letters.getOrElse(idx) { "${idx + 1}" }}"
        }
        QuestionType.MCMA -> {
            val indices = question.correctAnswerCode.split(",").mapNotNull { it.trim().toIntOrNull() }
            "Pernyataan ${indices.joinToString(", ") { "${it + 1}" }}"
        }
        QuestionType.TRUE_FALSE_MATRIX -> {
            question.correctAnswerCode
        }
    }
}
