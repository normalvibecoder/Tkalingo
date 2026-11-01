package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.bank.TkaSubjectCatalog
import com.example.data.model.TkaSubject
import com.example.data.model.TkaUnitNode
import com.example.data.model.UserProfile
import com.example.ui.components.BimoOwlMascot
import com.example.ui.components.DuoChunkyCard
import com.example.ui.components.DuoTactileButton
import com.example.ui.components.MascotMood
import com.example.ui.theme.DuoBorderGray
import com.example.ui.theme.DuoCardSurface
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenDark
import com.example.ui.theme.DuoFlameOrange
import com.example.ui.theme.DuoFlameOrangeDark
import com.example.ui.theme.DuoInkDark
import com.example.ui.theme.DuoInkMuted
import com.example.ui.theme.DuoSunGold
import com.example.ui.theme.DuoWarmBg
import com.example.util.SoundEffectsManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomePathScreen(
    selectedSubject: TkaSubject,
    userProfile: UserProfile,
    totalBankCount: Int,
    onSelectSubject: (TkaSubject) -> Unit,
    onStartUnitQuiz: (TkaUnitNode) -> Unit,
    onStartSimulasi30: () -> Unit,
    onStartQuickWeaknessDrill: (String) -> Unit
) {
    val subjectColor = Color(selectedSubject.primaryColorHex)
    val subjectDark = Color(selectedSubject.darkAccentHex)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoWarmBg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Horizontal Subject Switcher Chips (8 Subjects)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TkaSubjectCatalog.subjects.forEach { subj ->
                    val isSelected = subj.id == selectedSubject.id
                    val sColor = Color(subj.primaryColorHex)
                    val sDark = Color(subj.darkAccentHex)
                    Surface(
                        color = if (isSelected) sColor else DuoCardSurface,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(2.dp, if (isSelected) sDark else DuoBorderGray),
                        modifier = Modifier
                            .testTag("subject_chip_${subj.id}")
                            .clickable {
                                SoundEffectsManager.playTap()
                                onSelectSubject(subj)
                            }
                    ) {
                        Text(
                            text = subj.shortName,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Color.White else DuoInkDark,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Duolingo-style Colorful Unit Banner Header
            DuoChunkyCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = subjectColor,
                borderColor = subjectDark,
                bottomLipColor = subjectDark,
                contentPadding = PaddingValues(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = 0.24f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "${selectedSubject.category.uppercase()} • $totalBankCount SOAL DI BANK",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = DuoSunGold,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "${userProfile.xp} XP",
                                style = MaterialTheme.typography.labelLarge,
                                color = DuoInkDark,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = selectedSubject.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedSubject.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.92f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    DuoTactileButton(
                        text = "Mulai Simulasi TKA (30 Soal • 75 Menit)",
                        containerColor = DuoSunGold,
                        lipColor = Color(0xFFD69900),
                        contentColor = DuoInkDark,
                        testTag = "start_simulasi_30_button",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = null,
                                tint = DuoInkDark
                            )
                        },
                        onClick = onStartSimulasi30
                    )
                }
            }

            // If user has detected weak subtopics, show a high-priority Weakness Alert Strip
            if (userProfile.weakSubtopics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                val topWeak = userProfile.weakSubtopics.first()
                DuoChunkyCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = DuoFlameOrange,
                    bottomLipColor = DuoFlameOrangeDark,
                    backgroundColor = Color(0xFFFFF8EC),
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🎯 Rekomendasi Remedial Kelemahan",
                                style = MaterialTheme.typography.labelLarge,
                                color = DuoFlameOrangeDark
                            )
                            Text(
                                text = "Perkuat subtopik \"$topWeak\" dengan sesi khusus 10 soal!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DuoInkDark
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            color = DuoFlameOrange,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .testTag("quick_weakness_drill_button")
                                .clickable {
                                    SoundEffectsManager.playTap()
                                    onStartQuickWeaknessDrill(topWeak)
                                }
                        ) {
                            Text(
                                text = "GAS 10 SOAL",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Winding Duolingo Skill Tree Path of Units
            Text(
                text = "Jalur Petualangan Unit (${selectedSubject.units.size} Unit • PG, MCMA & Benar/Salah)",
                style = MaterialTheme.typography.titleMedium,
                color = DuoInkMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Winding offsets (-48dp, 0dp, +48dp, 0dp...)
            val horizontalOffsets = listOf(0.dp, (-44).dp, (-20).dp, 38.dp, 46.dp, 0.dp)

            selectedSubject.units.forEachIndexed { idx, unit ->
                val isMastered = userProfile.masteredUnits.contains(unit.id)
                val nodeOffset = horizontalOffsets[idx % horizontalOffsets.size]

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Show Bimo mascot cheering next to Unit 2 and Unit 5
                    if (idx == 1) {
                        BimoOwlMascot(
                            size = 74.dp,
                            mood = MascotMood.CHEERFUL,
                            primaryColor = subjectColor,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 18.dp)
                        )
                    } else if (idx == 4) {
                        BimoOwlMascot(
                            size = 70.dp,
                            mood = MascotMood.EXCITED,
                            primaryColor = DuoFlameOrange,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 18.dp)
                        )
                    }

                    SkillTreeNodeItem(
                        unit = unit,
                        isMastered = isMastered,
                        subjectColor = subjectColor,
                        subjectDark = subjectDark,
                        modifier = Modifier.offset(x = nodeOffset),
                        onClick = { onStartUnitQuiz(unit) }
                    )
                }

                // Connector dots between nodes
                if (idx < selectedSubject.units.lastIndex) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(2) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(subjectColor.copy(alpha = 0.35f))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun SkillTreeNodeItem(
    unit: TkaUnitNode,
    isMastered: Boolean,
    subjectColor: Color,
    subjectDark: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .widthIn(max = 260.dp)
            .testTag("unit_node_${unit.id}")
            .clickable {
                SoundEffectsManager.playTap()
                onClick()
            }
    ) {
        // 3D Circular Pedestal Button
        Box(
            modifier = Modifier.size(86.dp),
            contentAlignment = Alignment.Center
        ) {
            // Bottom 3D lip
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .align(Alignment.BottomCenter)
                    .clip(CircleShape)
                    .background(if (isMastered) Color(0xFFD69900) else subjectDark)
            )
            // Top circle
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .align(Alignment.TopCenter)
                    .clip(CircleShape)
                    .background(if (isMastered) DuoSunGold else subjectColor)
                    .border(3.dp, Color.White.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = resolveUnitIcon(unit.iconName),
                    contentDescription = unit.title,
                    tint = if (isMastered) DuoInkDark else Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }

            // Crown badge on top-right
            Surface(
                color = if (isMastered) DuoFeatherGreen else DuoSunGold,
                shape = CircleShape,
                border = BorderStroke(2.dp, Color.White),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isMastered) Icons.Filled.Check else Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (isMastered) Color.White else DuoInkDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            color = DuoCardSurface,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.5.dp, DuoBorderGray),
            shadowElevation = 1.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Unit ${unit.unitNumber}: ${unit.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = DuoInkDark,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = unit.subtopics.joinToString(" • "),
                    style = MaterialTheme.typography.labelMedium,
                    color = subjectDark,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun resolveUnitIcon(iconName: String): ImageVector {
    return when (iconName) {
        "functions" -> Icons.Filled.Functions
        "calculate" -> Icons.Filled.Calculate
        "menu_book" -> Icons.Filled.MenuBook
        "science" -> Icons.Filled.Science
        "biotech" -> Icons.Filled.Biotech
        "bolt" -> Icons.Filled.Bolt
        "analytics" -> Icons.Filled.Analytics
        "public" -> Icons.Filled.Public
        else -> Icons.Filled.School
    }
}

@Composable
fun SubjectPickerDialog(
    subjects: List<TkaSubject>,
    selectedSubjectId: String,
    onSelect: (TkaSubject) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pilih Mata Pelajaran TKA",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                subjects.forEach { subj ->
                    val sColor = Color(subj.primaryColorHex)
                    val isSelected = subj.id == selectedSubjectId
                    Surface(
                        color = if (isSelected) sColor.copy(alpha = 0.16f) else DuoCardSurface,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(2.dp, if (isSelected) sColor else DuoBorderGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                SoundEffectsManager.playTap()
                                onSelect(subj)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = subj.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DuoInkDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${subj.category} • 108+ Soal TKA",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DuoInkMuted
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.EmojiEvents,
                                    contentDescription = null,
                                    tint = sColor
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = DuoInkMuted
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}
