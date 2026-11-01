package com.example.data.bank

import com.example.data.model.SavedCustomQuestion
import com.example.data.model.DifficultyTier
import com.example.data.model.QuestionType
import com.example.data.model.TkaQuestion
import com.example.data.model.TkaSubject
import com.example.data.model.TkaUnitNode

object TkaSubjectCatalog {

    val subjects: List<TkaSubject> = listOf(
        TkaSubject(
            id = "mat_wajib",
            name = "Matematika Wajib",
            shortName = "Mat Wajib",
            category = "TKA Wajib",
            primaryColorHex = 0xFF58CC02,
            darkAccentHex = 0xFF46A302,
            description = "Aljabar, Eksponen & Logaritma, Fungsi Kuadrat, Barisan Deret, Trigonometri Dasar, dan Peluang.",
            units = listOf(
                TkaUnitNode("mat_wajib_u1", "mat_wajib", 1, "Eksponen & Logaritma", "Sifat pangkat, persamaan eksponen & basis logaritma", listOf("Logaritma", "Eksponen"), "functions"),
                TkaUnitNode("mat_wajib_u2", "mat_wajib", 2, "Fungsi & Persamaan Kuadrat", "Akar-akar, diskriminan, dan titik puncak parabola", listOf("Fungsi Kuadrat", "Sistem Persamaan Linear"), "timeline"),
                TkaUnitNode("mat_wajib_u3", "mat_wajib", 3, "Barisan & Deret", "Aritmetika, geometri, dan deret tak hingga", listOf("Barisan & Deret"), "trending_up"),
                TkaUnitNode("mat_wajib_u4", "mat_wajib", 4, "Trigonometri Dasar", "Perbandingan segitiga, sudut berelasi & aturan sinus-kosinus", listOf("Trigonometri Dasar"), "change_history"),
                TkaUnitNode("mat_wajib_u5", "mat_wajib", 5, "Statistika & Peluang", "Ukuran pemusatan, penyebaran, permutasi & kombinasi", listOf("Statistika", "Peluang & Kombinatorika"), "bar_chart"),
                TkaUnitNode("mat_wajib_u6", "mat_wajib", 6, "Matriks & Transformasi", "Determinan, invers matriks, translasi & rotasi", listOf("Matriks & Transformasi"), "grid_on")
            )
        ),
        TkaSubject(
            id = "mat_lanjut",
            name = "Matematika Lanjut",
            shortName = "Mat Lanjut",
            category = "TKA Peminatan",
            primaryColorHex = 0xFF1CB0F6,
            darkAccentHex = 0xFF1899D6,
            description = "Polinomial, Limit & Turunan Kalkulus, Integral, Vektor 3D, Trigonometri Analitik, dan Distribusi Peluang.",
            units = listOf(
                TkaUnitNode("mat_lanjut_u1", "mat_lanjut", 1, "Polinomial (Suku Banyak)", "Teorema sisa, teorema faktor & akar rasional", listOf("Polinomial & Teorema Sisa"), "calculate"),
                TkaUnitNode("mat_lanjut_u2", "mat_lanjut", 2, "Limit & Turunan Fungsi", "Limit aljabar/trigonometri, aturan rantai & titik stasioner", listOf("Limit Fungsi", "Turunan & Optimasi"), "show_chart"),
                TkaUnitNode("mat_lanjut_u3", "mat_lanjut", 3, "Integral & Luas Daerah", "Integral tentu, substitusi, parsial & volume benda putar", listOf("Integral Kalkulus"), "area_chart"),
                TkaUnitNode("mat_lanjut_u4", "mat_lanjut", 4, "Vektor & Geometri Analitik", "Hasil kali titik, proyeksi vektor & persamaan lingkaran", listOf("Vektor & Proyeksi", "Irisan Kerucut & Lingkaran"), "explore"),
                TkaUnitNode("mat_lanjut_u5", "mat_lanjut", 5, "Trigonometri Analitik", "Rumus jumlah selisih sudut & sudut rangkap", listOf("Trigonometri Analitik"), "waves"),
                TkaUnitNode("mat_lanjut_u6", "mat_lanjut", 6, "Distribusi Binomial & Normal", "Variabel acak diskrit, peluang binomial & uji hipotesis", listOf("Distribusi Peluang"), "analytics")
            )
        ),
        TkaSubject(
            id = "bindo",
            name = "Bahasa Indonesia",
            shortName = "B. Indonesia",
            category = "TKA Wajib",
            primaryColorHex = 0xFFFF4B4B,
            darkAccentHex = 0xFFEA2B2B,
            description = "Literasi Membaca TKA: Ide Pokok, Simpulan Kritis, PUEBI/EYD V, Kalimat Efektif, dan Evaluasi Argumen.",
            units = listOf(
                TkaUnitNode("bindo_u1", "bindo", 1, "Ide Pokok & Simpulan", "Menentukan gagasan utama, ringkasan, dan simpulan logis", listOf("Ide Pokok & Simpulan"), "menu_book"),
                TkaUnitNode("bindo_u2", "bindo", 2, "Kalimat Efektif & EYD V", "Kesejajaran bentuk, kehematan kata, dan tanda baca baku", listOf("Kalimat Efektif & EYD"), "spellcheck"),
                TkaUnitNode("bindo_u3", "bindo", 3, "Penalaran Wacana Kritis", "Menganalisis sebab-akibat, asumsi penulis, dan koherensi", listOf("Penalaran Wacana"), "psychology"),
                TkaUnitNode("bindo_u4", "bindo", 4, "Makna Kata & Konjungsi", "Makna leksikal/kontekstual, kata serapan, dan konjungsi antarkalimat", listOf("Makna Kata & Konjungsi"), "translate"),
                TkaUnitNode("bindo_u5", "bindo", 5, "Teks Argumentasi & Eksposisi", "Memperkuat/memperlemah argumen dan struktur teks ilmiah", listOf("Evaluasi Argumen"), "record_voice_over"),
                TkaUnitNode("bindo_u6", "bindo", 6, "Fakta, Opini & Infografik", "Membedakan klaim faktual, opini subjektif, dan sintesis data", listOf("Fakta vs Opini"), "fact_check")
            )
        ),
        TkaSubject(
            id = "bing_wajib",
            name = "Bahasa Inggris Wajib",
            shortName = "B. Inggris",
            category = "TKA Wajib",
            primaryColorHex = 0xFFFF9600,
            darkAccentHex = 0xFFD97F00,
            description = "Reading Comprehension TKA: Main Idea, Inference, Detail Retrieval, Contextual Vocabulary, and Author Purpose.",
            units = listOf(
                TkaUnitNode("bing_wajib_u1", "bing_wajib", 1, "Main Idea & Purpose", "Identifying primary thesis and author's communicative intent", listOf("Main Idea & Purpose"), "lightbulb"),
                TkaUnitNode("bing_wajib_u2", "bing_wajib", 2, "Inference & Implied Meaning", "Drawing logical conclusions from academic passages", listOf("Inference & Implied Detail"), "search"),
                TkaUnitNode("bing_wajib_u3", "bing_wajib", 3, "Vocabulary in Context", "Synonyms, semantic nuance, and contextual word meaning", listOf("Vocabulary in Context"), "abc"),
                TkaUnitNode("bing_wajib_u4", "bing_wajib", 4, "Reference & Restatement", "Pronoun referents and accurate sentence paraphrasing", listOf("Paraphrasing & Reference"), "swap_horiz"),
                TkaUnitNode("bing_wajib_u5", "bing_wajib", 5, "Author's Tone & Attitude", "Detecting objective, critical, optimistic, or cautionary tone", listOf("Tone & Attitude"), "mood"),
                TkaUnitNode("bing_wajib_u6", "bing_wajib", 6, "Text Organization & Thread", "Transition logic, preceding/following paragraphs & multi-author threads", listOf("Text Coherence"), "forum")
            )
        ),
        TkaSubject(
            id = "bing_lanjut",
            name = "Bahasa Inggris Lanjut",
            shortName = "Eng Lanjut",
            category = "TKA Peminatan",
            primaryColorHex = 0xFFCE82FF,
            darkAccentHex = 0xFFA559D9,
            description = "Advanced Academic Reading, Critical Fallacies, Comparative Synthesis, Complex Structure, and Rhetorical Function.",
            units = listOf(
                TkaUnitNode("bing_lanjut_u1", "bing_lanjut", 1, "Critical Reasoning & Fallacies", "Evaluating logical flaws, assumptions, and counterarguments", listOf("Critical Fallacies"), "gavel"),
                TkaUnitNode("bing_lanjut_u2", "bing_lanjut", 2, "Paired Passage Synthesis", "Comparing perspectives, methodologies, and conflicting claims", listOf("Comparative Synthesis"), "compare_arrows"),
                TkaUnitNode("bing_lanjut_u3", "bing_lanjut", 3, "Advanced Academic Structure", "Inversion, subjunctive mood, reduced clauses & parallelism", listOf("Advanced Grammar & Inversion"), "architecture"),
                TkaUnitNode("bing_lanjut_u4", "bing_lanjut", 4, "Rhetorical Strategy", "Analyzing why an author cites evidence, metaphors, or concessions", listOf("Rhetorical Strategy"), "campaign"),
                TkaUnitNode("bing_lanjut_u5", "bing_lanjut", 5, "Data-Integrated Reading", "Synthesizing complex academic prose with quantitative trends", listOf("Academic Data Synthesis"), "insights"),
                TkaUnitNode("bing_lanjut_u6", "bing_lanjut", 6, "Academic Register & Collocation", "C1/C2 academic discourse markers and idiomatic precision", listOf("Academic Lexicon"), "school")
            )
        ),
        TkaSubject(
            id = "fisika",
            name = "Fisika",
            shortName = "Fisika",
            category = "TKA Sains",
            primaryColorHex = 0xFF00CD9C,
            darkAccentHex = 0xFF00A37B,
            description = "Mekanika Klasik, Usaha & Energi, Fluida & Termodinamika, Gelombang Optik, Listrik Magnet, dan Fisika Kuantum.",
            units = listOf(
                TkaUnitNode("fisika_u1", "fisika", 1, "Kinematika & Dinamika", "Gerak parabola, hukum Newton, gaya gesek & dinamika rotasi", listOf("Kinematika & Dinamika", "Dinamika Rotasi"), "speed"),
                TkaUnitNode("fisika_u2", "fisika", 2, "Usaha, Energi & Momentum", "Hukum kekekalan energi mekanik, impuls & tumbukan", listOf("Usaha & Energi", "Impuls & Momentum"), "bolt"),
                TkaUnitNode("fisika_u3", "fisika", 3, "Fluida & Termodinamika", "Asas Bernoulli, hukum Archimedes, gas ideal & mesin Carnot", listOf("Fluida Statis & Dinamis", "Termodinamika"), "thermostat"),
                TkaUnitNode("fisika_u4", "fisika", 4, "Gelombang, Bunyi & Optik", "Efek Doppler, interferensi celah ganda & alat optik", listOf("Gelombang & Optik"), "graphic_eq"),
                TkaUnitNode("fisika_u5", "fisika", 5, "Listrik & Kemagnetan", "Hukum Kirchhoff, kapasitor, gaya Lorentz & induksi Faraday", listOf("Listrik Arus Searah & Bolak-Balik", "Medan Magnet & Induksi"), "electrical_services"),
                TkaUnitNode("fisika_u6", "fisika", 6, "Fisika Modern & Inti", "Efek fotolistrik, relativitas khusus & peluruhan radioaktif", listOf("Fisika Modern & Kuantum"), "flare")
            )
        ),
        TkaSubject(
            id = "kimia",
            name = "Kimia",
            shortName = "Kimia",
            category = "TKA Sains",
            primaryColorHex = 0xFFFF6B9D,
            darkAccentHex = 0xFFD84B7C,
            description = "Stoikiometri, Ikatan Kimia, Termokimia, Laju & Kesetimbangan, Larutan Asam-Basa/Penyangga, Redoks, dan Kimia Organik.",
            units = listOf(
                TkaUnitNode("kimia_u1", "kimia", 1, "Stoikiometri & Konsep Mol", "Pereaksi pembatas, rumus empiris, kadar zat & hukum dasar kimia", listOf("Stoikiometri"), "science"),
                TkaUnitNode("kimia_u2", "kimia", 2, "Struktur Atom & Ikatan Kimia", "Konfigurasi elektron, sifat periodik, hibridisasi & gaya antarmolekul", listOf("Struktur Atom & Ikatan Kimia"), "hub"),
                TkaUnitNode("kimia_u3", "kimia", 3, "Termokimia & Laju Reaksi", "Perubahan entalpi, hukum Hess, energi ikatan & orde reaksi", listOf("Termokimia", "Laju Reaksi"), "local_fire_department"),
                TkaUnitNode("kimia_u4", "kimia", 4, "Kesetimbangan & Asam Basa", "Pergeseran Le Chatelier, pH buffer, hidrolisis & Ksp", listOf("Kesetimbangan Kimia", "Asam Basa & Buffer"), "balance"),
                TkaUnitNode("kimia_u5", "kimia", 5, "Redoks & Elektrokimia", "Sel Volta, potensial reduksi standar, elektrolisis & hukum Faraday", listOf("Redoks & Elektrokimia"), "battery_charging_full"),
                TkaUnitNode("kimia_u6", "kimia", 6, "Kimia Organik & Makromolekul", "Gugus fungsi, isomer, reaksi senyawa karbon & polimer", listOf("Kimia Organik"), "hexagon")
            )
        ),
        TkaSubject(
            id = "biosains",
            name = "Biologi & Biosains",
            shortName = "Biosains",
            category = "TKA Sains",
            primaryColorHex = 0xFF2ECC71,
            darkAccentHex = 0xFF239B56,
            description = "Biologi Sel & Molekuler, Genetika & Sintesis Protein, Bioteknologi CRISPR/PCR, Fisiologi Manusia, dan Ekologi.",
            units = listOf(
                TkaUnitNode("biosains_u1", "biosains", 1, "Biologi Sel & Enzim", "Transpor membran, organel sel, respirasi aerob & kinetika enzim", listOf("Biologi Sel & Metabolisme"), "biotech"),
                TkaUnitNode("biosains_u2", "biosains", 2, "Genetika & Sintesis Protein", "Replikasi DNA, transkripsi-translasi, hukum Mendel & pautan gen", listOf("Genetika & Molekuler"), "fingerprint"),
                TkaUnitNode("biosains_u3", "biosains", 3, "Bioteknologi Modern", "DNA rekombinan, PCR, CRISPR-Cas9, hibridoma & bioetika", listOf("Bioteknologi & Rekayasa Genetika"), "precision_manufacturing"),
                TkaUnitNode("biosains_u4", "biosains", 4, "Fisiologi & Imunologi", "Sistem saraf-endokrin, sirkulasi, ekskresi & respons imun spesifik", listOf("Fisiologi & Imunologi"), "favorite"),
                TkaUnitNode("biosains_u5", "biosains", 5, "Struktur & Fisiologi Tumbuhan", "Fotosintesis C3/C4/CAM, jaringan pengangkut & fitohormon", listOf("Botani & Fotosintesis"), "eco"),
                TkaUnitNode("biosains_u6", "biosains", 6, "Ekologi & Evolusi", "Daur biogeokimia, dinamika populasi & kesetimbangan Hardy-Weinberg", listOf("Ekologi & Evolusi"), "public")
            )
        )
    )

    fun getSubjectById(subjectId: String): TkaSubject {
        return subjects.find { it.id == subjectId } ?: subjects.first()
    }

    /**
     * Returns all 108+ questions in the database for a given subject, plus any user-saved custom AI questions.
     */
    fun getAllQuestionsForSubject(
        subjectId: String,
        customSaved: List<SavedCustomQuestion> = emptyList()
    ): List<TkaQuestion> {
        val baseQuestions = when (subjectId) {
            "mat_wajib" -> MathQuestionBank.getMatWajibQuestions()
            "mat_lanjut" -> MathQuestionBank.getMatLanjutQuestions()
            "bindo" -> LanguageQuestionBank.getBindoQuestions()
            "bing_wajib" -> LanguageQuestionBank.getBingWajibQuestions()
            "bing_lanjut" -> LanguageQuestionBank.getBingLanjutQuestions()
            "fisika" -> ScienceQuestionBank.getFisikaQuestions()
            "kimia" -> ScienceQuestionBank.getKimiaQuestions()
            "biosains" -> ScienceQuestionBank.getBiosainsQuestions()
            else -> MathQuestionBank.getMatWajibQuestions()
        }

        val mappedCustom = customSaved
            .filter { it.subjectId == subjectId }
            .map { sq ->
                val qType = try {
                    QuestionType.valueOf(sq.questionType)
                } catch (_: Exception) {
                    QuestionType.SINGLE_CHOICE
                }
                TkaQuestion(
                    id = sq.id.ifBlank { "custom_${sq.stem.hashCode()}" },
                    subjectId = sq.subjectId,
                    unitId = "${sq.subjectId}_u1",
                    subtopic = sq.subtopic,
                    difficulty = DifficultyTier.fromLevel(sq.difficulty.toInt()),
                    type = qType,
                    stem = sq.stem,
                    options = sq.options,
                    correctAnswerCode = sq.correctAnswerCode,
                    explanation = sq.explanation
                )
            }

        return mappedCustom + baseQuestions
    }

    /**
     * Draws a stratified 30-question TKA Simulation Exam (75 minutes) from the 108+ question bank
     * so every attempt offers a fresh, balanced mix of subtopics, difficulty levels, and formats
     * (Single Choice, MCMA, and True/False Matrix).
     */
    fun sampleSimulationExam30(
        subjectId: String,
        customSaved: List<SavedCustomQuestion> = emptyList()
    ): List<TkaQuestion> {
        val pool = getAllQuestionsForSubject(subjectId, customSaved).shuffled()
        val easy = pool.filter { it.difficulty == DifficultyTier.EASY }.take(9)
        val medium = pool.filter { it.difficulty == DifficultyTier.MEDIUM }.take(12)
        val hard = pool.filter { it.difficulty == DifficultyTier.HARD }.take(9)
        val combined = (easy + medium + hard).distinctBy { it.id }
        return if (combined.size >= 30) {
            combined.take(30).shuffled()
        } else {
            (combined + (pool - combined.toSet())).take(30).shuffled()
        }
    }

    /**
     * Draws a focused 10-question weakness remedial session targeting the user's weak subtopic(s).
     */
    fun sampleWeaknessRemedial10(
        subjectId: String,
        targetSubtopic: String,
        customSaved: List<SavedCustomQuestion> = emptyList()
    ): List<TkaQuestion> {
        val pool = getAllQuestionsForSubject(subjectId, customSaved)
        val matchingSubtopic = pool.filter {
            it.subtopic.equals(targetSubtopic, ignoreCase = true) ||
                it.subtopic.contains(targetSubtopic, ignoreCase = true) ||
                targetSubtopic.contains(it.subtopic, ignoreCase = true)
        }.shuffled()

        return if (matchingSubtopic.size >= 10) {
            matchingSubtopic.take(10)
        } else {
            val remainder = (pool - matchingSubtopic.toSet()).shuffled()
            (matchingSubtopic + remainder).take(10)
        }
    }

    /**
     * Draws an interactive 10-question Unit Skill Node practice session.
     */
    fun sampleUnitNodeQuiz(
        subjectId: String,
        unitId: String,
        customSaved: List<SavedCustomQuestion> = emptyList()
    ): List<TkaQuestion> {
        val pool = getAllQuestionsForSubject(subjectId, customSaved)
        val unitQuestions = pool.filter { it.unitId == unitId }.shuffled()
        return if (unitQuestions.size >= 10) {
            unitQuestions.take(10)
        } else {
            (unitQuestions + (pool - unitQuestions.toSet()).shuffled()).take(10)
        }
    }
}
