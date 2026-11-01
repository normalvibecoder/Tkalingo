package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.QuestionReviewItem
import com.example.data.model.SessionResultSummary
import com.example.data.model.TkaQuestion
import com.example.ui.components.BimoOwlMascot
import com.example.ui.components.DuoChunkyCard
import com.example.ui.components.DuoTactileButton
import com.example.ui.components.MascotMood
import com.example.ui.theme.DuoBorderGray
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenDark
import com.example.ui.theme.DuoFeatherGreenLight
import com.example.ui.theme.DuoFlameOrange
import com.example.ui.theme.DuoFlameOrangeDark
import com.example.ui.theme.DuoHeartRed
import com.example.ui.theme.DuoHeartRedDark
import com.example.ui.theme.DuoHeartRedLight
import com.example.ui.theme.DuoInkDark
import com.example.ui.theme.DuoInkMuted
import com.example.ui.theme.DuoRoyalPurple
import com.example.ui.theme.DuoRoyalPurpleDark
import com.example.ui.theme.DuoSkyBlue
import com.example.ui.theme.DuoSkyBlueDark
import com.example.ui.theme.DuoSunGold
import com.example.ui.theme.DuoWarmBg
import com.example.ui.viewmodel.AiTaskState

@Composable
fun SessionResultScreen(
    summary: SessionResultSummary,
    aiState: AiTaskState,
    onStartWeaknessRemedial10: (String) -> Unit,
    onGenerateAiWeaknessRemedial10: (String) -> Unit,
    onRequestDeepWeaknessAnalysis: () -> Unit,
    onRequestQuestionDeepExplain: (TkaQuestion, String) -> Unit,
    onDismissAiState: () -> Unit,
    onBackToHome: () -> Unit
) {
    BackHandler {
        onBackToHome()
    }

    val subjectColor = Color(summary.subject.primaryColorHex)
    val subjectDark = Color(summary.subject.darkAccentHex)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoWarmBg)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mascot & IRT Score Trophy Header
            BimoOwlMascot(
                size = 98.dp,
                mood = if (summary.irtScore >= 650) MascotMood.EXCITED else MascotMood.ENCOURAGING,
                primaryColor = subjectColor
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = when {
                    summary.irtScore >= 750 -> "Skor TKA Sangat Memuaskan!"
                    summary.irtScore >= 600 -> " Kerja Bagus! Terus Tingkatkan!"
                    else -> "Tetap Semangat! Mari Perbaiki Kelemahanmu!"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = DuoInkDark,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${summary.subject.name} • Mode ${summary.mode.replace("_", " ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = DuoInkMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            // IRT Weighted Score & Rewards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DuoChunkyCard(
                    modifier = Modifier.weight(1.2f),
                    borderColor = DuoSunGold,
                    bottomLipColor = Color(0xFFD99B00),
                    backgroundColor = Color(0xFFFFFBEB),
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = DuoFlameOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "SKOR IRT TKA",
                                style = MaterialTheme.typography.labelMedium,
                                color = DuoFlameOrangeDark
                            )
                        }
                        Text(
                            text = "${summary.irtScore}",
                            style = MaterialTheme.typography.displayLarge,
                            color = DuoInkDark
                        )
                        Text(
                            text = "Bobot Kesulitan (200–1000)",
                            style = MaterialTheme.typography.labelMedium,
                            color = DuoInkMuted
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DuoChunkyCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = DuoFeatherGreen,
                        bottomLipColor = DuoFeatherGreenDark,
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Benar Penuh", style = MaterialTheme.typography.labelMedium, color = DuoInkMuted)
                            Text(
                                "${summary.rawCorrectCount}/${summary.totalQuestions}",
                                style = MaterialTheme.typography.titleMedium,
                                color = DuoFeatherGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    DuoChunkyCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = DuoSkyBlue,
                        bottomLipColor = DuoSkyBlueDark,
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Hadiah Sesi", style = MaterialTheme.typography.labelMedium, color = DuoInkMuted)
                            Text(
                                "+${summary.earnedXp} XP • +${summary.earnedGems} 💎",
                                style = MaterialTheme.typography.labelLarge,
                                color = DuoSkyBlueDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // WEAKNESS DIAGNOSIS & 10-QUESTION REMEDIAL LAUNCHER CARD
            DuoChunkyCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (summary.weakSubtopics.isNotEmpty()) DuoFlameOrange else DuoFeatherGreen,
                bottomLipColor = if (summary.weakSubtopics.isNotEmpty()) DuoFlameOrangeDark else DuoFeatherGreenDark,
                contentPadding = PaddingValues(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (summary.weakSubtopics.isNotEmpty()) Icons.Filled.Warning else Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = if (summary.weakSubtopics.isNotEmpty()) DuoFlameOrange else DuoFeatherGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Diagnosis Penguasaan & Kelemahan Subtopik",
                            style = MaterialTheme.typography.titleLarge,
                            color = DuoInkDark
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (summary.weakSubtopics.isNotEmpty()) {
                            "Berdasarkan bobot kesulitan soal, Anda masih kurang mahir di bagian: ${summary.weakSubtopics.joinToString(", ")}. Yuk lengkapi kelemahanmu dengan sesi khusus 10 soal!"
                        } else {
                            "Hebat! Penguasaan subtopik Anda sangat merata. Anda tetap dapat memilih subtopik di bawah untuk latihan pengayaan 10 soal."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = DuoInkMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Subtopic mastery bars
                    summary.subtopicPerformances.forEach { perf ->
                        val barColor = when {
                            perf.masteryPercentage < 65 -> DuoHeartRed
                            perf.masteryPercentage < 80 -> DuoFlameOrange
                            else -> DuoFeatherGreen
                        }
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = perf.subtopic,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DuoInkDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${perf.masteryPercentage}% (${if (perf.isWeak) "Perlu Latihan" else "Mahir"})",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = barColor
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { perf.masteryPercentage / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = barColor,
                                trackColor = DuoBorderGray,
                                strokeCap = StrokeCap.Round
                            )
                        }
                    }

                    // Action buttons for each weak subtopic: 10-question Remedial Session
                    val targetSubtopics = summary.weakSubtopics.ifEmpty {
                        summary.subtopicPerformances.take(1).map { it.subtopic }
                    }
                    if (targetSubtopics.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        targetSubtopics.forEach { weakSub ->
                            DuoTactileButton(
                                text = "Latihan 10 Soal Kelemahan: $weakSub",
                                containerColor = DuoFlameOrange,
                                lipColor = DuoFlameOrangeDark,
                                testTag = "remedial_button_$weakSub",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.FitnessCenter,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                },
                                onClick = { onStartWeaknessRemedial10(weakSub) }
                            )

                            DuoTactileButton(
                                text = "Generate + Latihan 10 Soal AI Baru: $weakSub",
                                containerColor = DuoRoyalPurple,
                                lipColor = DuoRoyalPurpleDark,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                },
                                onClick = { onGenerateAiWeaknessRemedial10(weakSub) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // High-Thinking AI Coach Analysis Card (gemini-4-argon)
            DuoChunkyCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = DuoRoyalPurple,
                bottomLipColor = DuoRoyalPurpleDark,
                contentPadding = PaddingValues(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = null,
                            tint = DuoRoyalPurple,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "AI Coach High Thinking (gemini-4-argon)",
                                style = MaterialTheme.typography.titleMedium,
                                color = DuoInkDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Analisis mendalam pola kesalahan & rumus kunci subtopik lemahmu",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DuoInkMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    when (aiState) {
                        is AiTaskState.Idle -> {
                            DuoTactileButton(
                                text = "Minta Analisis Mendalam AI Coach",
                                containerColor = DuoRoyalPurple,
                                lipColor = DuoRoyalPurpleDark,
                                testTag = "deep_ai_analysis_button",
                                modifier = Modifier.fillMaxWidth(),
                                onClick = onRequestDeepWeaknessAnalysis
                            )
                        }
                        is AiTaskState.Loading -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = DuoRoyalPurple,
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = aiState.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = DuoRoyalPurpleDark
                                )
                            }
                        }
                        is AiTaskState.Success -> {
                            Surface(
                                color = Color(0xFFF6EEFF),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = aiState.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DuoRoyalPurpleDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = aiState.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DuoInkDark
                                    )
                                }
                            }
                        }
                        is AiTaskState.Error -> {
                            Surface(
                                color = DuoHeartRedLight,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = aiState.message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DuoHeartRedDark
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DuoTactileButton(
                                        text = "Coba Lagi",
                                        containerColor = DuoRoyalPurple,
                                        lipColor = DuoRoyalPurpleDark,
                                        onClick = onRequestDeepWeaknessAnalysis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // QUESTION-BY-QUESTION EXPLANATION & REVIEW SECTION
            Text(
                text = "Pembahasan Lengkap Per Soal (${summary.questionReviews.size} Soal)",
                style = MaterialTheme.typography.titleLarge,
                color = DuoInkDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            summary.questionReviews.forEachIndexed { idx, review ->
                QuestionReviewCard(
                    index = idx + 1,
                    review = review,
                    onAskDeepAi = {
                        onRequestQuestionDeepExplain(review.question, review.userAnswerCode)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            DuoTactileButton(
                text = "Kembali ke Beranda Jalur Belajar",
                containerColor = subjectColor,
                lipColor = subjectDark,
                testTag = "back_to_home_button",
                modifier = Modifier.fillMaxWidth(),
                onClick = onBackToHome
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuestionReviewCard(
    index: Int,
    review: QuestionReviewItem,
    onAskDeepAi: () -> Unit
) {
    val q = review.question
    val statusColor = when {
        review.isFullyCorrect -> DuoFeatherGreenDark
        review.partialCreditRatio > 0.0 -> DuoFlameOrangeDark
        else -> DuoHeartRedDark
    }
    val statusBg = when {
        review.isFullyCorrect -> DuoFeatherGreenLight
        review.partialCreditRatio > 0.0 -> Color(0xFFFFF3D6)
        else -> DuoHeartRedLight
    }

    DuoChunkyCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = statusColor.copy(alpha = 0.45f),
        bottomLipColor = statusColor.copy(alpha = 0.30f),
        contentPadding = PaddingValues(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Soal #$index • ${q.subtopic} (${q.difficulty.label})",
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "+${"%.1f".format(review.earnedWeightedScore)} / ${"%.1f".format(review.maxWeightedScore)} pt IRT",
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = q.stem,
                style = MaterialTheme.typography.bodyLarge,
                color = DuoInkDark
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Jawabanmu: ${review.userAnswerCode.ifBlank { "(Kosong)" }}  |  Kunci TKA: ${formatAnswerKeyReadable(q)}",
                style = MaterialTheme.typography.labelLarge,
                color = statusColor
            )

            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = Color(0xFFF3F6FA),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Pembahasan:",
                        style = MaterialTheme.typography.labelLarge,
                        color = DuoSkyBlueDark
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = q.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DuoInkDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            DuoTactileButton(
                text = "Bedah Soal Ini dengan AI (High Thinking)",
                containerColor = DuoSkyBlue,
                lipColor = DuoSkyBlueDark,
                modifier = Modifier.fillMaxWidth(),
                onClick = onAskDeepAi
            )
        }
    }
}
