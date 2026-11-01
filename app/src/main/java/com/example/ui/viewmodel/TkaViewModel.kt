package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.TkaAiTutorService
import com.example.data.bank.TkaSubjectCatalog
import com.example.data.model.ExamAttempt
import com.example.data.model.SavedCustomQuestion
import com.example.data.model.SessionResultSummary
import com.example.data.model.TkaQuestion
import com.example.data.model.TkaSubject
import com.example.data.model.TkaUnitNode
import com.example.data.model.UserProfile
import com.example.data.repository.TkaRepository
import com.example.data.scoring.IrtScoringEngine
import com.example.util.SoundEffectsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ActiveScreen {
    HOME_PATH,
    SIMULASI_HUB,
    WEAKNESS_CLINIC,
    AI_SEARCH_LAB,
    ACTIVE_QUIZ,
    SESSION_RESULT
}

sealed interface AiTaskState {
    data object Idle : AiTaskState
    data class Loading(val label: String) : AiTaskState
    data class Success(val title: String, val content: String) : AiTaskState
    data class Error(val message: String) : AiTaskState
}

data class ActiveQuizState(
    val subject: TkaSubject,
    val mode: String, // "SIMULASI_TKA", "UNIT_PATH", "REMEDIAL_DRILL"
    val sessionTitle: String,
    val unitId: String? = null,
    val targetSubtopic: String? = null,
    val questions: List<TkaQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswers: Map<String, String> = emptyMap(),
    val flaggedQuestionIds: Set<String> = emptySet(),
    // For UNIT_PATH & REMEDIAL_DRILL instant Duolingo-style check bar:
    val isCurrentChecked: Boolean = false,
    val isCurrentCorrect: Boolean = false,
    val currentCreditRatio: Double = 0.0,
    // Timer: for SIMULASI_TKA 75 minutes = 4500 seconds
    val remainingSeconds: Int = 4500,
    val elapsedSeconds: Int = 0,
    val heartsRemaining: Int = 5,
    val quickHintText: String? = null,
    val isHintLoading: Boolean = false
)

class TkaViewModel(
    private val repository: TkaRepository,
    private val currentUserId: String,
    initialDisplayName: String = "Pelajar TKA"
) : ViewModel() {

    private val _selectedSubject = MutableStateFlow(TkaSubjectCatalog.subjects.first())
    val selectedSubject: StateFlow<TkaSubject> = _selectedSubject.asStateFlow()

    private val _activeScreen = MutableStateFlow(ActiveScreen.HOME_PATH)
    val activeScreen: StateFlow<ActiveScreen> = _activeScreen.asStateFlow()

    private val _isSoundEnabled = MutableStateFlow(SoundEffectsManager.isSoundEnabled)
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    private val _quizState = MutableStateFlow<ActiveQuizState?>(null)
    val quizState: StateFlow<ActiveQuizState?> = _quizState.asStateFlow()

    private val _lastResult = MutableStateFlow<SessionResultSummary?>(null)
    val lastResult: StateFlow<SessionResultSummary?> = _lastResult.asStateFlow()

    private val _lastAttemptDocId = MutableStateFlow<String?>(null)

    private val _aiState = MutableStateFlow<AiTaskState>(AiTaskState.Idle)
    val aiState: StateFlow<AiTaskState> = _aiState.asStateFlow()

    private val _statusBanner = MutableStateFlow<String?>(null)
    val statusBanner: StateFlow<String?> = _statusBanner.asStateFlow()

    val userProfile: StateFlow<UserProfile> = repository.observeUserProfile(currentUserId)
        .map { it ?: UserProfile(id = currentUserId, userId = currentUserId, displayName = initialDisplayName) }
        .catch { e ->
            Log.w(TAG, "Error observing profile", e)
            emit(UserProfile(id = currentUserId, userId = currentUserId, displayName = initialDisplayName))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UserProfile(id = currentUserId, userId = currentUserId, displayName = initialDisplayName)
        )

    val examAttempts: StateFlow<List<ExamAttempt>> = repository.observeExamAttempts(currentUserId)
        .catch { e ->
            Log.w(TAG, "Error observing exam attempts", e)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val savedCustomQuestions: StateFlow<List<SavedCustomQuestion>> = repository.observeSavedCustomQuestions(currentUserId)
        .catch { e ->
            Log.w(TAG, "Error observing custom questions", e)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getOrCreateUserProfile(initialDisplayName)
        }
    }

    fun selectSubject(subject: TkaSubject) {
        _selectedSubject.value = subject
    }

    fun navigateTo(screen: ActiveScreen) {
        _activeScreen.value = screen
    }

    fun toggleSound() {
        val next = !SoundEffectsManager.isSoundEnabled
        SoundEffectsManager.isSoundEnabled = next
        _isSoundEnabled.value = next
        if (next) SoundEffectsManager.playTap()
    }

    fun dismissStatusBanner() {
        _statusBanner.value = null
    }

    fun clearAiState() {
        _aiState.value = AiTaskState.Idle
    }

    fun refillHearts() {
        val profile = userProfile.value ?: return
        viewModelScope.launch {
            val res = repository.refillHeartsWithGems(profile)
            if (res.isSuccess) {
                SoundEffectsManager.playCorrectChime()
                _statusBanner.value = "Energi Hati berhasil diisi penuh (5 ❤️)!"
            } else {
                _statusBanner.value = res.exceptionOrNull()?.localizedMessage ?: "Gagal mengisi hati."
            }
        }
    }

    /**
     * Starts a 30-Question, 75-Minute Official TKA Simulation Exam for the given subject.
     */
    fun startSimulationExam30(subject: TkaSubject = _selectedSubject.value) {
        _selectedSubject.value = subject
        val questions = TkaSubjectCatalog.sampleSimulationExam30(
            subjectId = subject.id,
            customSaved = savedCustomQuestions.value
        )
        startSessionInternal(
            subject = subject,
            mode = "SIMULASI_TKA",
            sessionTitle = "Simulasi TKA ${subject.shortName} (30 Soal • 75 Menit)",
            unitId = null,
            targetSubtopic = null,
            questions = questions,
            durationLimitSeconds = 75 * 60 // 75 minutes = 4500 seconds
        )
    }

    /**
     * Starts a 10-Question Duolingo-style Unit Node Quiz.
     */
    fun startUnitNodeSession(unit: TkaUnitNode) {
        val subject = TkaSubjectCatalog.getSubjectById(unit.subjectId)
        _selectedSubject.value = subject
        val questions = TkaSubjectCatalog.sampleUnitNodeQuiz(
            subjectId = subject.id,
            unitId = unit.id,
            customSaved = savedCustomQuestions.value
        )
        startSessionInternal(
            subject = subject,
            mode = "UNIT_PATH",
            sessionTitle = "Unit ${unit.unitNumber}: ${unit.title}",
            unitId = unit.id,
            targetSubtopic = unit.subtopics.firstOrNull(),
            questions = questions,
            durationLimitSeconds = 25 * 60
        )
    }

    /**
     * Starts a dedicated 10-Question Weakness Remedial Drill on a specific weak subtopic (e.g. "Logaritma").
     */
    fun startWeaknessRemedial10(
        subject: TkaSubject = _selectedSubject.value,
        weakSubtopic: String
    ) {
        _selectedSubject.value = subject
        val questions = TkaSubjectCatalog.sampleWeaknessRemedial10(
            subjectId = subject.id,
            targetSubtopic = weakSubtopic,
            customSaved = savedCustomQuestions.value
        )
        startSessionInternal(
            subject = subject,
            mode = "REMEDIAL_DRILL",
            sessionTitle = "Remedial 10 Soal: $weakSubtopic",
            unitId = null,
            targetSubtopic = weakSubtopic,
            questions = questions,
            durationLimitSeconds = 25 * 60
        )
    }

    private fun startSessionInternal(
        subject: TkaSubject,
        mode: String,
        sessionTitle: String,
        unitId: String?,
        targetSubtopic: String?,
        questions: List<TkaQuestion>,
        durationLimitSeconds: Int
    ) {
        timerJob?.cancel()
        val initialHearts = (userProfile.value?.hearts?.toInt() ?: 5).coerceIn(1, 5)
        _quizState.value = ActiveQuizState(
            subject = subject,
            mode = mode,
            sessionTitle = sessionTitle,
            unitId = unitId,
            targetSubtopic = targetSubtopic,
            questions = questions,
            currentIndex = 0,
            userAnswers = emptyMap(),
            flaggedQuestionIds = emptySet(),
            isCurrentChecked = false,
            isCurrentCorrect = false,
            currentCreditRatio = 0.0,
            remainingSeconds = durationLimitSeconds,
            elapsedSeconds = 0,
            heartsRemaining = initialHearts,
            quickHintText = null,
            isHintLoading = false
        )
        _activeScreen.value = ActiveScreen.ACTIVE_QUIZ

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val current = _quizState.value ?: break
                if (current.remainingSeconds <= 1) {
                    // Auto-submit when 75-minute TKA timer expires
                    submitSession()
                    break
                } else {
                    _quizState.update { state ->
                        state?.copy(
                            remainingSeconds = state.remainingSeconds - 1,
                            elapsedSeconds = state.elapsedSeconds + 1
                        )
                    }
                }
            }
        }
    }

    fun updateCurrentAnswer(answerCode: String) {
        val current = _quizState.value ?: return
        val q = current.questions.getOrNull(current.currentIndex) ?: return
        // In interactive mode (UNIT_PATH / REMEDIAL_DRILL), lock answer once checked
        if (current.mode != "SIMULASI_TKA" && current.isCurrentChecked) return

        _quizState.update { state ->
            state?.copy(
                userAnswers = state.userAnswers + (q.id to answerCode)
            )
        }
    }

    fun toggleFlagCurrentQuestion() {
        val current = _quizState.value ?: return
        val q = current.questions.getOrNull(current.currentIndex) ?: return
        val newFlags = if (current.flaggedQuestionIds.contains(q.id)) {
            current.flaggedQuestionIds - q.id
        } else {
            current.flaggedQuestionIds + q.id
        }
        _quizState.update { it?.copy(flaggedQuestionIds = newFlags) }
    }

    fun jumpToQuestion(index: Int) {
        val current = _quizState.value ?: return
        if (index !in current.questions.indices) return
        _quizState.update {
            it?.copy(
                currentIndex = index,
                isCurrentChecked = false,
                quickHintText = null,
                isHintLoading = false
            )
        }
    }

    /**
     * In Duolingo-style UNIT_PATH and REMEDIAL_DRILL modes, checks the current question immediately,
     * plays chime/wrong SFX, and shows the bottom explanation banner.
     */
    fun checkCurrentAnswerInteractive() {
        val current = _quizState.value ?: return
        val q = current.questions.getOrNull(current.currentIndex) ?: return
        val userAns = current.userAnswers[q.id].orEmpty()
        if (userAns.isBlank()) return

        val (isCorrect, creditRatio) = IrtScoringEngine.evaluateAnswer(q, userAns)
        if (isCorrect || creditRatio >= 0.5) {
            SoundEffectsManager.playCorrectChime()
        } else {
            SoundEffectsManager.playWrongSound()
        }

        val updatedHearts = if (!isCorrect && creditRatio < 0.5) {
            (current.heartsRemaining - 1).coerceAtLeast(1)
        } else {
            current.heartsRemaining
        }

        _quizState.update {
            it?.copy(
                isCurrentChecked = true,
                isCurrentCorrect = isCorrect,
                currentCreditRatio = creditRatio,
                heartsRemaining = updatedHearts
            )
        }
    }

    fun nextQuestionOrFinish() {
        val current = _quizState.value ?: return
        if (current.currentIndex + 1 < current.questions.size) {
            _quizState.update {
                it?.copy(
                    currentIndex = current.currentIndex + 1,
                    isCurrentChecked = false,
                    isCurrentCorrect = false,
                    currentCreditRatio = 0.0,
                    quickHintText = null,
                    isHintLoading = false
                )
            }
        } else {
            submitSession()
        }
    }

    fun previousQuestion() {
        val current = _quizState.value ?: return
        if (current.currentIndex > 0) {
            _quizState.update {
                it?.copy(
                    currentIndex = current.currentIndex - 1,
                    isCurrentChecked = false,
                    quickHintText = null,
                    isHintLoading = false
                )
            }
        }
    }

    fun requestQuickAiHint() {
        val current = _quizState.value ?: return
        val q = current.questions.getOrNull(current.currentIndex) ?: return
        if (current.isHintLoading) return

        _quizState.update { it?.copy(isHintLoading = true) }
        viewModelScope.launch {
            val res = TkaAiTutorService.generateQuickHint(q)
            _quizState.update {
                it?.copy(
                    isHintLoading = false,
                    quickHintText = res.getOrElse { err ->
                        "Petunjuk Konsep (${q.subtopic}): Perhatikan kata kunci soal dan gunakan rumus dasar pada ${q.subtopic}. (${err.localizedMessage ?: "AI sibuk"})"
                    }
                )
            }
        }
    }

    fun submitSession() {
        val current = _quizState.value ?: return
        timerJob?.cancel()

        val summary = IrtScoringEngine.calculateSessionSummary(
            subject = current.subject,
            mode = current.mode,
            questions = current.questions,
            userAnswers = current.userAnswers,
            durationSeconds = current.elapsedSeconds.coerceAtLeast(1)
        )

        SoundEffectsManager.playCelebrationFanfare()
        _lastResult.value = summary
        _quizState.value = null
        _activeScreen.value = ActiveScreen.SESSION_RESULT

        // Persist to Firestore
        viewModelScope.launch {
            val attemptRes = repository.recordExamAttempt(
                subjectId = summary.subject.id,
                subjectName = summary.subject.name,
                mode = summary.mode,
                irtScore = summary.irtScore,
                correctCount = summary.rawCorrectCount,
                totalQuestions = summary.totalQuestions,
                durationSeconds = summary.durationSeconds,
                weakSubtopics = summary.weakSubtopics,
                strongSubtopics = summary.strongSubtopics
            )
            _lastAttemptDocId.value = attemptRes.getOrNull()

            val profile = userProfile.value ?: UserProfile(id = currentUserId, userId = currentUserId)
            repository.updateProfileAfterSession(
                currentProfile = profile,
                addedXp = summary.earnedXp,
                addedGems = summary.earnedGems,
                heartsRemaining = current.heartsRemaining,
                isExamSimulation = current.mode == "SIMULASI_TKA",
                isRemedial = current.mode == "REMEDIAL_DRILL",
                newWeakSubtopics = summary.weakSubtopics,
                completedUnitId = current.unitId
            )
        }
    }

    fun exitQuizSession() {
        timerJob?.cancel()
        _quizState.value = null
        _activeScreen.value = ActiveScreen.HOME_PATH
    }

    /**
     * Uses gemini-4-argon with ThinkingLevel.HIGH to deeply analyze the user's session weaknesses.
     */
    fun runDeepAiWeaknessAnalysis(summary: SessionResultSummary) {
        _aiState.value = AiTaskState.Loading("Bimo sedang menganalisis kelemahanmu dengan High Thinking (gemini-4-argon)...")
        viewModelScope.launch {
            val res = TkaAiTutorService.analyzeSessionWeaknessDeep(summary)
            if (res.isSuccess) {
                val text = res.getOrThrow()
                _aiState.value = AiTaskState.Success(
                    title = "Analisis Mendalam AI Coach (High Thinking)",
                    content = text
                )
                val attemptId = _lastAttemptDocId.value
                if (!attemptId.isNullOrBlank()) {
                    repository.updateAttemptAiSummary(attemptId, text)
                }
            } else {
                _aiState.value = AiTaskState.Error(
                    res.exceptionOrNull()?.localizedMessage ?: "Gagal menjalankan analisis AI."
                )
            }
        }
    }

    /**
     * Uses gemini-4-argon with ThinkingLevel.HIGH to explain a specific question step-by-step.
     */
    fun runDeepQuestionExplanation(question: TkaQuestion, userAnswer: String, customQuestion: String = "") {
        _aiState.value = AiTaskState.Loading("Membedah langkah penyelesaian HOTS dengan High Thinking (gemini-4-argon)...")
        viewModelScope.launch {
            val res = TkaAiTutorService.explainQuestionWithHighThinking(question, userAnswer, customQuestion)
            if (res.isSuccess) {
                _aiState.value = AiTaskState.Success(
                    title = "Bedah Soal Mendalam • ${question.subtopic}",
                    content = res.getOrThrow()
                )
            } else {
                _aiState.value = AiTaskState.Error(
                    res.exceptionOrNull()?.localizedMessage ?: "Gagal memuat penjelasan mendalam."
                )
            }
        }
    }

    /**
     * Uses gemini-3.8-flash with Google Search grounding tool to answer up-to-date TKA/Science queries.
     */
    fun runSearchGroundedQuery(query: String) {
        if (query.isBlank()) return
        val subj = _selectedSubject.value
        _aiState.value = AiTaskState.Loading("Mencari referensi terkini via Google Search Grounding (gemini-3.8-flash)...")
        viewModelScope.launch {
            val res = TkaAiTutorService.searchGroundedTkaQuery(query, subj.name)
            if (res.isSuccess) {
                _aiState.value = AiTaskState.Success(
                    title = "Hasil Google Search Grounding • ${subj.shortName}",
                    content = res.getOrThrow()
                )
            } else {
                _aiState.value = AiTaskState.Error(
                    res.exceptionOrNull()?.localizedMessage ?: "Pencarian Google Search gagal."
                )
            }
        }
    }

    /**
     * Uses gemini-3.8-flash structured JSON output to generate fresh custom weakness questions,
     * saves them to Firestore, and immediately launches a 10-question weakness drill!
     */
    fun generateAndStartAiRemedialDrill(subject: TkaSubject, weakSubtopic: String) {
        _aiState.value = AiTaskState.Loading("Menyusun paket soal latihan kelemahan '$weakSubtopic' dengan AI...")
        viewModelScope.launch {
            val aiRes = TkaAiTutorService.generateCustomWeaknessQuestions(
                subjectId = subject.id,
                subjectName = subject.name,
                weakSubtopic = weakSubtopic,
                count = 4
            )
            val generated = aiRes.getOrElse { emptyList() }
            if (generated.isNotEmpty()) {
                repository.saveCustomQuestions(generated)
            }
            _aiState.value = AiTaskState.Idle
            val bankPool = TkaSubjectCatalog.sampleWeaknessRemedial10(
                subjectId = subject.id,
                targetSubtopic = weakSubtopic,
                customSaved = savedCustomQuestions.value
            )
            val combined10 = (generated + bankPool).distinctBy { it.stem }.take(10)
            startSessionInternal(
                subject = subject,
                mode = "REMEDIAL_DRILL",
                sessionTitle = "Sesi Remedial 10 Soal: $weakSubtopic",
                unitId = null,
                targetSubtopic = weakSubtopic,
                questions = combined10,
                durationLimitSeconds = 25 * 60
            )
        }
    }

    companion object {
        private const val TAG = "TkaViewModel"
    }
}
