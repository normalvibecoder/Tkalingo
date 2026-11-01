package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.bank.TkaSubjectCatalog
import com.example.data.model.ExamAttempt
import com.example.data.model.TkaSubject
import com.example.data.model.UserProfile
import com.example.ui.components.DuoChunkyCard
import com.example.ui.components.DuoTactileButton
import com.example.ui.components.MascotMood
import com.example.ui.components.MascotSpeechBanner
import com.example.ui.theme.DuoBorderGray
import com.example.ui.theme.DuoCardSurface
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenDark
import com.example.ui.theme.DuoFlameOrange
import com.example.ui.theme.DuoFlameOrangeDark
import com.example.ui.theme.DuoHeartRed
import com.example.ui.theme.DuoHeartRedDark
import com.example.ui.theme.DuoInkDark
import com.example.ui.theme.DuoInkMuted
import com.example.ui.theme.DuoRoyalPurple
import com.example.ui.theme.DuoRoyalPurpleDark
import com.example.ui.theme.DuoSkyBlue
import com.example.ui.theme.DuoSkyBlueDark
import com.example.ui.theme.DuoSunGold
import com.example.ui.theme.DuoWarmBg
import com.example.ui.viewmodel.AiTaskState
import com.example.util.SoundEffectsManager

/**
 * Tab 2: Arena Simulasi TKA (30 Soal Permanen • 75 Menit • 108+ Bank Soal per Mapel • Bobot IRT)
 */
@Composable
fun SimulasiHubScreen(
    attempts: List<ExamAttempt>,
    customQuestionsCount: Int,
    onStartSimulasiForSubject: (TkaSubject) -> Unit
) {
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
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            MascotSpeechBanner(
                title = "Arena Simulasi TKA Resmi (30 Soal • 75 Menit)",
                message = "Setiap mapel punya 108+ soal di bank data! Tiap simulasi mengambil 30 soal acak berstrata (Mudah, Sedang, HOTS) dengan format PG, MCMA, & Benar/Salah serta penilaian bobot IRT.",
                mood = MascotMood.EXCITED,
                accentColor = DuoSkyBlue
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pilih Mapel untuk Ujian Simulasi (30 Soal / 75 Menit)",
                style = MaterialTheme.typography.titleLarge,
                color = DuoInkDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            TkaSubjectCatalog.subjects.forEach { subj ->
                val sColor = Color(subj.primaryColorHex)
                val sDark = Color(subj.darkAccentHex)
                DuoChunkyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    borderColor = sColor.copy(alpha = 0.55f),
                    bottomLipColor = sDark.copy(alpha = 0.40f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = subj.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DuoInkDark
                                )
                                Text(
                                    text = "Bank Soal: 108+ Soal • Durasi: 75 Menit • Paket: 30 Soal",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = sDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                color = sColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = subj.category,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = sDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        DuoTactileButton(
                            text = "Mulai 30 Soal ${subj.shortName} (75 Menit)",
                            containerColor = sColor,
                            lipColor = sDark,
                            testTag = "simulasi_subject_btn_${subj.id}",
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Timer,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            },
                            onClick = { onStartSimulasiForSubject(subj) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Exam Attempt History from Firestore
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = DuoInkDark
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Riwayat Simulasi & Remedial Tersimpan (${attempts.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = DuoInkDark
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (attempts.isEmpty()) {
                DuoChunkyCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Belum ada riwayat ujian. Selesaikan simulasi 30 soal atau latihan unit untuk melihat rekam jejak skor IRT dan kelemahanmu!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DuoInkMuted
                    )
                }
            } else {
                attempts.take(10).forEach { att ->
                    DuoChunkyCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${att.subjectName} (${att.mode.replace("_", " ")})",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DuoInkDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Benar: ${att.correctCount}/${att.totalQuestions} soal • Durasi: ${att.durationSeconds / 60}m ${att.durationSeconds % 60}s",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = DuoInkMuted
                                    )
                                }
                                Surface(
                                    color = DuoSunGold.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "IRT ${att.irtScore}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF8A5A00),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            if (att.weakSubtopics.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Kelemahan Terdeteksi: ${att.weakSubtopics.joinToString(", ")}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DuoHeartRedDark
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Tab 3: Klinik Kelemahan (Sesi Khusus 10 Soal per Subtopik Kelemahan, misal Logaritma, Stoikiometri, dll.)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeaknessClinicScreen(
    selectedSubject: TkaSubject,
    userProfile: UserProfile,
    aiState: AiTaskState,
    onSelectSubject: (TkaSubject) -> Unit,
    onStartWeaknessDrill10: (TkaSubject, String) -> Unit,
    onGenerateAiWeaknessDrill10: (TkaSubject, String) -> Unit
) {
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
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            MascotSpeechBanner(
                title = "Klinik Kelemahan (Sesi 10 Soal Fokus)",
                message = "Kurang mahir di Logaritma, Stoikiometri, atau Bioteknologi? Pilih subtopik kelemahanmu di bawah dan Bimo akan menyiapkan 10 soal terfokus lengkap dengan pembahasan!",
                mood = MascotMood.ENCOURAGING,
                accentColor = DuoFlameOrange
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (aiState is AiTaskState.Loading) {
                DuoChunkyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    borderColor = DuoRoyalPurple,
                    bottomLipColor = DuoRoyalPurpleDark
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = DuoRoyalPurple,
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = aiState.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DuoInkDark
                        )
                    }
                }
            }

            // Detected Weaknesses from User Profile
            DuoChunkyCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = DuoHeartRed,
                bottomLipColor = DuoHeartRedDark,
                backgroundColor = Color(0xFFFFF5F5),
                contentPadding = PaddingValues(16.dp)
            ) {
                Column {
                    Text(
                        text = "📌 Daftar Subtopik Kelemahanmu Saat Ini",
                        style = MaterialTheme.typography.titleLarge,
                        color = DuoHeartRedDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val weakList = userProfile.weakSubtopics.ifEmpty {
                        listOf("Logaritma", "Trigonometri Dasar", "Stoikiometri", "Bioteknologi & Rekayasa Genetika")
                    }
                    Text(
                        text = if (userProfile.weakSubtopics.isEmpty()) {
                            "Rekomendasi latihan subtopik menantang TKA (klik untuk mulai 10 soal):"
                        } else {
                            "Berdasarkan evaluasi bobot soal TKA terakhirmu, kamu disarankan melengkapi latihan 10 soal pada:"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = DuoInkMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    weakList.forEach { weakSub ->
                        val matchedSubject = TkaSubjectCatalog.subjects.find { s ->
                            s.units.any { u -> u.subtopics.any { it.equals(weakSub, ignoreCase = true) } }
                        } ?: selectedSubject

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DuoTactileButton(
                                text = "10 Soal: $weakSub",
                                containerColor = DuoFlameOrange,
                                lipColor = DuoFlameOrangeDark,
                                modifier = Modifier.weight(1f),
                                onClick = { onStartWeaknessDrill10(matchedSubject, weakSub) }
                            )
                            DuoTactileButton(
                                text = "+AI",
                                containerColor = DuoRoyalPurple,
                                lipColor = DuoRoyalPurpleDark,
                                modifier = Modifier.width(86.dp),
                                onClick = { onGenerateAiWeaknessDrill10(matchedSubject, weakSub) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Subject Filter to pick ANY Subtopic for 10-Question Drill
            Text(
                text = "Pilih Subtopik Spesifik per Mata Pelajaran (10 Soal Fokus)",
                style = MaterialTheme.typography.titleLarge,
                color = DuoInkDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TkaSubjectCatalog.subjects.forEach { subj ->
                    val isSelected = subj.id == selectedSubject.id
                    val sColor = Color(subj.primaryColorHex)
                    Surface(
                        color = if (isSelected) sColor else DuoCardSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, if (isSelected) sColor else DuoBorderGray),
                        modifier = Modifier.clickable {
                            SoundEffectsManager.playTap()
                            onSelectSubject(subj)
                        }
                    ) {
                        Text(
                            text = subj.shortName,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Color.White else DuoInkDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val allSubtopics = selectedSubject.units.flatMap { it.subtopics }.distinct()
            allSubtopics.forEach { subtopic ->
                DuoChunkyCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Column {
                        Text(
                            text = "$subtopic (${selectedSubject.shortName})",
                            style = MaterialTheme.typography.titleMedium,
                            color = DuoInkDark,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DuoTactileButton(
                                text = "Latihan 10 Soal",
                                containerColor = Color(selectedSubject.primaryColorHex),
                                lipColor = Color(selectedSubject.darkAccentHex),
                                modifier = Modifier.weight(1f),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.FitnessCenter,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                },
                                onClick = { onStartWeaknessDrill10(selectedSubject, subtopic) }
                            )
                            DuoTactileButton(
                                text = "10 Soal AI",
                                containerColor = DuoRoyalPurple,
                                lipColor = DuoRoyalPurpleDark,
                                modifier = Modifier.weight(1f),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                },
                                onClick = { onGenerateAiWeaknessDrill10(selectedSubject, subtopic) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Tab 4: Lab AI Tutor (High Thinking gemini-4-argon + Google Search Grounding gemini-3.8-flash) & Profil
 */
@Composable
fun AiSearchLabScreen(
    selectedSubject: TkaSubject,
    userProfile: UserProfile,
    aiState: AiTaskState,
    onRunSearchQuery: (String) -> Unit,
    onSignOut: () -> Unit
) {
    var searchInput by remember { mutableStateOf("") }

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
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            MascotSpeechBanner(
                title = "Riset TKA & Biosains dengan Google Search Grounding",
                message = "Tanyakan materi TKA terbaru, fakta Biosains (CRISPR, PCR, Sintesis Protein), rumus Fisika/Kimia, atau kebijakan ujian menggunakan gemini-3.8-flash + Google Search!",
                mood = MascotMood.THINKING,
                accentColor = DuoRoyalPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            DuoChunkyCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = DuoSkyBlue,
                bottomLipColor = DuoSkyBlueDark,
                contentPadding = PaddingValues(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Public,
                            contentDescription = null,
                            tint = DuoSkyBlueDark
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tanya Fakta & Konsep TKA (Google Search Grounded)",
                            style = MaterialTheme.typography.titleMedium,
                            color = DuoInkDark,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = searchInput,
                        onValueChange = { searchInput = it },
                        placeholder = {
                            Text("Contoh: Jelaskan sifat-sifat logaritma TKA atau mekanisme CRISPR-Cas9 pada Biosains...")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_grounding_input"),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick suggestion pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val quickQueries = listOf(
                            "Rumus Cepat Logaritma TKA",
                            "Tahapan PCR & CRISPR Biosains",
                            "Tips Skor IRT TKA Tinggi"
                        )
                        quickQueries.forEach { q ->
                            Surface(
                                color = DuoSkyBlue.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        SoundEffectsManager.playTap()
                                        searchInput = q
                                        onRunSearchQuery(q)
                                    }
                            ) {
                                Text(
                                    text = q,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = DuoSkyBlueDark,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DuoTactileButton(
                        text = "Cari & Analisis dengan Google Search",
                        enabled = searchInput.isNotBlank() && aiState !is AiTaskState.Loading,
                        containerColor = DuoSkyBlue,
                        lipColor = DuoSkyBlueDark,
                        testTag = "run_search_grounding_button",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        onClick = { onRunSearchQuery(searchInput) }
                    )

                    when (aiState) {
                        is AiTaskState.Loading -> {
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = DuoSkyBlue,
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = aiState.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = DuoInkDark
                                )
                            }
                        }
                        is AiTaskState.Success -> {
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                color = Color(0xFFF0F8FF),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = aiState.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = DuoSkyBlueDark,
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
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = aiState.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = DuoHeartRedDark
                            )
                        }
                        AiTaskState.Idle -> {}
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Learner Profile Card & Sign Out
            DuoChunkyCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                Column {
                    Text(
                        text = "Profil Pelajar: ${userProfile.displayName}",
                        style = MaterialTheme.typography.titleLarge,
                        color = DuoInkDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Total XP: ${userProfile.xp} XP\n" +
                            "• Simulasi 30 Soal Selesai: ${userProfile.totalExamsCompleted}x\n" +
                            "• Sesi Remedial 10 Soal Selesai: ${userProfile.totalRemedialsCompleted}x\n" +
                            "• Unit Dikuasai: ${userProfile.masteredUnits.size} Unit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DuoInkMuted
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    DuoTactileButton(
                        text = "Keluar Akun (Sign Out)",
                        containerColor = DuoHeartRed,
                        lipColor = DuoHeartRedDark,
                        testTag = "sign_out_button",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Logout,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        onClick = onSignOut
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
