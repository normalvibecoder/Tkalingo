package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.bank.TkaSubjectCatalog
import com.example.data.repository.TkaRepository
import com.example.ui.auth.AuthScreen
import com.example.ui.auth.signOut
import com.example.ui.components.DuoTopStatsBar
import com.example.ui.screens.AiSearchLabScreen
import com.example.ui.screens.HomePathScreen
import com.example.ui.screens.QuizSessionScreen
import com.example.ui.screens.SessionResultScreen
import com.example.ui.screens.SimulasiHubScreen
import com.example.ui.screens.SubjectPickerDialog
import com.example.ui.screens.WeaknessClinicScreen
import com.example.ui.theme.DuoCardSurface
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoInkDark
import com.example.ui.theme.DuoInkMuted
import com.example.ui.theme.DuoWarmBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ActiveScreen
import com.example.ui.viewmodel.TkaViewModel
import com.example.util.SoundEffectsManager
import com.example.util.configureAppCheck
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun FirebaseAuth.authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { auth ->
        trySend(auth.currentUser)
    }
    addAuthStateListener(listener)
    awaitClose { removeAuthStateListener(listener) }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mandatory Firebase App Check initialization before any AI or Firestore calls
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TkaLingoRootApp()
            }
        }
    }
}

@Composable
fun TkaLingoRootApp() {
    val currentUser by remember { Firebase.auth.authStateFlow() }
        .collectAsStateWithLifecycle(initialValue = Firebase.auth.currentUser)

    val user = currentUser
    if (user == null) {
        AuthScreen(onAuthSuccess = { /* authStateFlow automatically transitions to MainAppShell */ })
    } else {
        key(user.uid) {
            AuthenticatedTkaShell(
                userId = user.uid,
                displayName = user.displayName?.takeIf { it.isNotBlank() } ?: "Pelajar TKA"
            )
        }
    }
}

@Composable
fun AuthenticatedTkaShell(
    userId: String,
    displayName: String
) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val viewModel: TkaViewModel = viewModel(
        key = "tka_vm_$userId",
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = TkaRepository(context = appContext)
                return TkaViewModel(
                    repository = repo,
                    currentUserId = userId,
                    initialDisplayName = displayName
                ) as T
            }
        }
    )

    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val activeScreen by viewModel.activeScreen.collectAsStateWithLifecycle()
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val examAttempts by viewModel.examAttempts.collectAsStateWithLifecycle()
    val savedCustomQuestions by viewModel.savedCustomQuestions.collectAsStateWithLifecycle()
    val quizState by viewModel.quizState.collectAsStateWithLifecycle()
    val lastResult by viewModel.lastResult.collectAsStateWithLifecycle()
    val aiState by viewModel.aiState.collectAsStateWithLifecycle()
    val statusBanner by viewModel.statusBanner.collectAsStateWithLifecycle()

    var showSubjectDialog by remember { mutableStateOf(false) }

    // BackHandler for secondary tabs to return to HOME_PATH
    BackHandler(
        enabled = activeScreen == ActiveScreen.SIMULASI_HUB ||
            activeScreen == ActiveScreen.WEAKNESS_CLINIC ||
            activeScreen == ActiveScreen.AI_SEARCH_LAB
    ) {
        viewModel.navigateTo(ActiveScreen.HOME_PATH)
    }

    if (showSubjectDialog) {
        SubjectPickerDialog(
            subjects = TkaSubjectCatalog.subjects,
            selectedSubjectId = selectedSubject.id,
            onSelect = { subj -> viewModel.selectSubject(subj) },
            onDismiss = { showSubjectDialog = false }
        )
    }

    // Full-screen modes: Active Quiz or Session Result
    when (activeScreen) {
        ActiveScreen.ACTIVE_QUIZ -> {
            val activeQuiz = quizState
            if (activeQuiz != null) {
                QuizSessionScreen(
                    quizState = activeQuiz,
                    onSelectAnswer = { ans -> viewModel.updateCurrentAnswer(ans) },
                    onCheckAnswerInteractive = { viewModel.checkCurrentAnswerInteractive() },
                    onNextQuestion = { viewModel.nextQuestionOrFinish() },
                    onPrevQuestion = { viewModel.previousQuestion() },
                    onJumpToQuestion = { idx -> viewModel.jumpToQuestion(idx) },
                    onToggleFlag = { viewModel.toggleFlagCurrentQuestion() },
                    onRequestHint = { viewModel.requestQuickAiHint() },
                    onSubmitExam = { viewModel.submitSession() },
                    onExitQuiz = { viewModel.exitQuizSession() }
                )
                return
            }
        }

        ActiveScreen.SESSION_RESULT -> {
            val resultSummary = lastResult
            if (resultSummary != null) {
                SessionResultScreen(
                    summary = resultSummary,
                    aiState = aiState,
                    onStartWeaknessRemedial10 = { weakSub ->
                        viewModel.startWeaknessRemedial10(
                            subject = resultSummary.subject,
                            weakSubtopic = weakSub
                        )
                    },
                    onGenerateAiWeaknessRemedial10 = { weakSub ->
                        viewModel.generateAndStartAiRemedialDrill(
                            subject = resultSummary.subject,
                            weakSubtopic = weakSub
                        )
                    },
                    onRequestDeepWeaknessAnalysis = {
                        viewModel.runDeepAiWeaknessAnalysis(resultSummary)
                    },
                    onRequestQuestionDeepExplain = { question, userAnswer ->
                        viewModel.runDeepQuestionExplanation(question, userAnswer)
                    },
                    onDismissAiState = { viewModel.clearAiState() },
                    onBackToHome = {
                        viewModel.clearAiState()
                        viewModel.navigateTo(ActiveScreen.HOME_PATH)
                    }
                )
                return
            }
        }

        else -> Unit
    }

    val subjectBankCount = remember(selectedSubject.id, savedCustomQuestions.size) {
        TkaSubjectCatalog.getAllQuestionsForSubject(
            subjectId = selectedSubject.id,
            customSaved = savedCustomQuestions
        ).size
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = DuoWarmBg,
        topBar = {
            Column {
                DuoTopStatsBar(
                    subjectShortName = selectedSubject.shortName,
                    subjectColor = Color(selectedSubject.primaryColorHex),
                    streakDays = userProfile.streakDays,
                    gems = userProfile.gems,
                    hearts = userProfile.hearts,
                    isSoundEnabled = isSoundEnabled,
                    onSubjectSelectorClick = { showSubjectDialog = true },
                    onToggleSound = { viewModel.toggleSound() },
                    onRefillHeartsClick = { viewModel.refillHearts() }
                )

                if (statusBanner != null) {
                    Surface(
                        color = DuoFeatherGreen.copy(alpha = 0.16f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = statusBanner.orEmpty(),
                                style = MaterialTheme.typography.labelLarge,
                                color = DuoInkDark,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { viewModel.dismissStatusBanner() }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Tutup Notifikasi",
                                    tint = DuoInkDark
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = DuoCardSurface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = activeScreen == ActiveScreen.HOME_PATH,
                    onClick = {
                        SoundEffectsManager.playTap()
                        viewModel.navigateTo(ActiveScreen.HOME_PATH)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Route,
                            contentDescription = "Jalur Belajar"
                        )
                    },
                    label = {
                        Text(
                            text = "Jalur TKA",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DuoFeatherGreen,
                        selectedTextColor = DuoFeatherGreen,
                        indicatorColor = DuoFeatherGreen.copy(alpha = 0.15f),
                        unselectedIconColor = DuoInkMuted,
                        unselectedTextColor = DuoInkMuted
                    ),
                    modifier = Modifier.testTag("nav_home_path")
                )

                NavigationBarItem(
                    selected = activeScreen == ActiveScreen.SIMULASI_HUB,
                    onClick = {
                        SoundEffectsManager.playTap()
                        viewModel.navigateTo(ActiveScreen.SIMULASI_HUB)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "Simulasi 30 Soal 75 Menit"
                        )
                    },
                    label = {
                        Text(
                            text = "Simulasi 30",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DuoFeatherGreen,
                        selectedTextColor = DuoFeatherGreen,
                        indicatorColor = DuoFeatherGreen.copy(alpha = 0.15f),
                        unselectedIconColor = DuoInkMuted,
                        unselectedTextColor = DuoInkMuted
                    ),
                    modifier = Modifier.testTag("nav_simulasi_hub")
                )

                NavigationBarItem(
                    selected = activeScreen == ActiveScreen.WEAKNESS_CLINIC,
                    onClick = {
                        SoundEffectsManager.playTap()
                        viewModel.navigateTo(ActiveScreen.WEAKNESS_CLINIC)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.FitnessCenter,
                            contentDescription = "Klinik Kelemahan 10 Soal"
                        )
                    },
                    label = {
                        Text(
                            text = "Remedial 10",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DuoFeatherGreen,
                        selectedTextColor = DuoFeatherGreen,
                        indicatorColor = DuoFeatherGreen.copy(alpha = 0.15f),
                        unselectedIconColor = DuoInkMuted,
                        unselectedTextColor = DuoInkMuted
                    ),
                    modifier = Modifier.testTag("nav_weakness_clinic")
                )

                NavigationBarItem(
                    selected = activeScreen == ActiveScreen.AI_SEARCH_LAB,
                    onClick = {
                        SoundEffectsManager.playTap()
                        viewModel.navigateTo(ActiveScreen.AI_SEARCH_LAB)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "Lab AI & Profil"
                        )
                    },
                    label = {
                        Text(
                            text = "Riset AI",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DuoFeatherGreen,
                        selectedTextColor = DuoFeatherGreen,
                        indicatorColor = DuoFeatherGreen.copy(alpha = 0.15f),
                        unselectedIconColor = DuoInkMuted,
                        unselectedTextColor = DuoInkMuted
                    ),
                    modifier = Modifier.testTag("nav_ai_search_lab")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeScreen) {
                ActiveScreen.HOME_PATH -> {
                    HomePathScreen(
                        selectedSubject = selectedSubject,
                        userProfile = userProfile,
                        totalBankCount = subjectBankCount,
                        onSelectSubject = { subj -> viewModel.selectSubject(subj) },
                        onStartUnitQuiz = { unit -> viewModel.startUnitNodeSession(unit) },
                        onStartSimulasi30 = { viewModel.startSimulationExam30(selectedSubject) },
                        onStartQuickWeaknessDrill = { weakSubtopic ->
                            viewModel.startWeaknessRemedial10(selectedSubject, weakSubtopic)
                        }
                    )
                }

                ActiveScreen.SIMULASI_HUB -> {
                    SimulasiHubScreen(
                        attempts = examAttempts,
                        customQuestionsCount = savedCustomQuestions.size,
                        onStartSimulasiForSubject = { subj ->
                            viewModel.startSimulationExam30(subj)
                        }
                    )
                }

                ActiveScreen.WEAKNESS_CLINIC -> {
                    WeaknessClinicScreen(
                        selectedSubject = selectedSubject,
                        userProfile = userProfile,
                        aiState = aiState,
                        onSelectSubject = { subj -> viewModel.selectSubject(subj) },
                        onStartWeaknessDrill10 = { subj, subtopic ->
                            viewModel.startWeaknessRemedial10(subj, subtopic)
                        },
                        onGenerateAiWeaknessDrill10 = { subj, subtopic ->
                            viewModel.generateAndStartAiRemedialDrill(subj, subtopic)
                        }
                    )
                }

                ActiveScreen.AI_SEARCH_LAB -> {
                    AiSearchLabScreen(
                        selectedSubject = selectedSubject,
                        userProfile = userProfile,
                        aiState = aiState,
                        onRunSearchQuery = { q -> viewModel.runSearchGroundedQuery(q) },
                        onSignOut = {
                            signOut(
                                credentialManager = credentialManager,
                                onSignOutComplete = {},
                                scope = scope
                            )
                        }
                    )
                }

                else -> Unit
            }
        }
    }
}
