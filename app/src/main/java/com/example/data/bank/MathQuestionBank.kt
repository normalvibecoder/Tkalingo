package com.example.data.bank

import com.example.data.model.DifficultyTier
import com.example.data.model.QuestionType
import com.example.data.model.TkaQuestion

object MathQuestionBank {

    /**
     * Generates 108 authentic TKA Matematika Wajib questions across 6 units (18 questions per unit),
     * including Single Choice, MCMA (Multiple Choice Multiple Answers), and True/False Matrix,
     * with comprehensive step-by-step explanations.
     */
    fun getMatWajibQuestions(): List<TkaQuestion> {
        val list = mutableListOf<TkaQuestion>()

        // Unit 1: Eksponen & Logaritma (18 questions: 12 Logaritma, 6 Eksponen)
        val logQuestions = listOf(
            TkaQuestion(
                id = "mw_u1_1", subjectId = "mat_wajib", unitId = "mat_wajib_u1", subtopic = "Logaritma",
                difficulty = DifficultyTier.EASY, type = QuestionType.SINGLE_CHOICE,
                stem = "Jika ²log 3 = a dan ³log 5 = b, maka nilai dari ⁶log 15 dinyatakan dalam a dan b adalah...",
                options = listOf("a(b + 1) / (a + 1)", "(ab + 1) / (a + 1)", "a(b + 1) / (a + b)", "(a + b) / (1 + ab)", "ab / (a + 1)"),
                correctAnswerCode = "0",
                explanation = "Gunakan sifat perubahan basis ke basis 3: ⁶log 15 = (³log 15) / (³log 6) = (³log(3×5)) / (³log(3×2)) = (1 + ³log 5) / (1 + ³log 2) = (1 + b) / (1 + 1/a) = a(b + 1) / (a + 1)."
            ),
            TkaQuestion(
                id = "mw_u1_2", subjectId = "mat_wajib", unitId = "mat_wajib_u1", subtopic = "Logaritma",
                difficulty = DifficultyTier.MEDIUM, type = QuestionType.MCMA,
                stem = "Manakah di antara pernyataan logaritma berikut yang bernilai BENAR? (Pilih semua jawaban yang benar)",
                options = listOf(
                    "²log 8 + ³log 9 = 5",
                    "⁵log 125 - ²log 4 = 2",
                    "⁴log 64 = 3",
                    "⁷log 1 = 0"
                ),
                correctAnswerCode = "0,2,3",
                explanation = "Evaluasi tiap pernyataan:\n• ²log 8 + ³log 9 = 3 + 2 = 5 (BENAR)\n• ⁵log 125 - ²log 4 = 3 - 2 = 1 ≠ 2 (SALAH)\n• ⁴log 64 = ⁴log(4³) = 3 (BENAR)\n• ⁷log 1 = 0 karena 7⁰ = 1 (BENAR)."
            ),
            TkaQuestion(
                id = "mw_u1_3", subjectId = "mat_wajib", unitId = "mat_wajib_u1", subtopic = "Logaritma",
                difficulty = DifficultyTier.HARD, type = QuestionType.TRUE_FALSE_MATRIX,
                stem = "Diketahui persamaan logaritma ²log(x - 1) + ²log(x + 1) = 3 dengan syarat numerus positif. Tentukan Benar (B) atau Salah (S) untuk setiap pernyataan berikut!",
                options = listOf(
                    "Syarat numerus mengharuskan x > 1.",
                    "Persamaan tersebut ekuivalen dengan x² - 1 = 8.",
                    "Himpunan penyelesaiannya adalah x = 3 atau x = -3."
                ),
                correctAnswerCode = "B,B,S",
                explanation = "1) Syarat numerus: x - 1 > 0 dan x + 1 > 0 → irisan x > 1 (Benar).\n2) ²log((x-1)(x+1)) = 3 → x² - 1 = 2³ = 8 (Benar).\n3) x² = 9 → x = ±3, namun karena syarat numerus x > 1 maka x = -3 tidak memenuhi, sehingga hanya x = 3 (Salah)."
            )
        )
        list.addAll(logQuestions)

        // Generate 9 more varied Logaritma questions (so Logaritma alone has 12 rich questions for 10-question weakness drill!)
        val logParams = listOf(
            Triple(2, 8, 3), Triple(3, 27, 3), Triple(2, 16, 4),
            Triple(5, 25, 2), Triple(4, 16, 2), Triple(3, 81, 4),
            Triple(2, 32, 5), Triple(6, 36, 2), Triple(7, 49, 2)
        )
        logParams.forEachIndexed { idx, (base, num, expVal) ->
            val shift = idx + 2
            val rhs = num + shift
            val diff = when (idx % 3) {
                0 -> DifficultyTier.EASY
                1 -> DifficultyTier.MEDIUM
                else -> DifficultyTier.HARD
            }
            val qType = when (idx % 3) {
                0 -> QuestionType.SINGLE_CHOICE
                1 -> QuestionType.MCMA
                else -> QuestionType.TRUE_FALSE_MATRIX
            }
            when (qType) {
                QuestionType.SINGLE_CHOICE -> {
                    list.add(
                        TkaQuestion(
                            id = "mw_u1_log_${idx + 4}",
                            subjectId = "mat_wajib",
                            unitId = "mat_wajib_u1",
                            subtopic = "Logaritma",
                            difficulty = diff,
                            type = QuestionType.SINGLE_CHOICE,
                            stem = "Jika ^$base log(x - $shift) = $expVal, maka nilai dari x adalah...",
                            options = listOf("$rhs", "${rhs - 2}", "${rhs + 4}", "${num - shift}", "${rhs + base}"),
                            correctAnswerCode = "0",
                            explanation = "Berdasarkan definisi logaritma ^a log(f(x)) = c ⇔ f(x) = a^c:\nx - $shift = $base^$expVal = $num\nx = $num + $shift = $rhs. Syarat numerus x - $shift > 0 terpenuhi karena $num > 0."
                        )
                    )
                }
                QuestionType.MCMA -> {
                    list.add(
                        TkaQuestion(
                            id = "mw_u1_log_${idx + 4}",
                            subjectId = "mat_wajib",
                            unitId = "mat_wajib_u1",
                            subtopic = "Logaritma",
                            difficulty = diff,
                            type = QuestionType.MCMA,
                            stem = "Diketahui P = ^$base log($num) dan Q = ^$base log($base). Manakah pernyataan yang tepat mengenai nilai P dan Q? (Pilih semua yang benar)",
                            options = listOf(
                                "Nilai P = $expVal",
                                "Nilai Q = 1",
                                "Nilai P + Q = ${expVal + 1}",
                                "Nilai P × Q = ${expVal + 2}"
                            ),
                            correctAnswerCode = "0,1,2",
                            explanation = "• P = ^$base log($base^$expVal) = $expVal (Benar)\n• Q = ^$base log($base) = 1 (Benar)\n• P + Q = $expVal + 1 = ${expVal + 1} (Benar)\n• P × Q = $expVal × 1 = $expVal ≠ ${expVal + 2} (Salah)."
                        )
                    )
                }
                QuestionType.TRUE_FALSE_MATRIX -> {
                    list.add(
                        TkaQuestion(
                            id = "mw_u1_log_${idx + 4}",
                            subjectId = "mat_wajib",
                            unitId = "mat_wajib_u1",
                            subtopic = "Logaritma",
                            difficulty = diff,
                            type = QuestionType.TRUE_FALSE_MATRIX,
                            stem = "Diberikan fungsi logaritma f(x) = ^$base log(x) untuk x > 0. Tentukan Benar (B) atau Salah (S) untuk tiap pernyataan berikut!",
                            options = listOf(
                                "Nilai f($num) adalah $expVal.",
                                "Grafik f(x) memotong sumbu-X di titik (1, 0).",
                                "Nilai f(1/$base) adalah 1."
                            ),
                            correctAnswerCode = "B,B,S",
                            explanation = "1) f($num) = ^$base log($num) = $expVal (Benar).\n2) Untuk setiap basis > 1, ^$base log(1) = 0 sehingga grafik melalui (1,0) (Benar).\n3) f(1/$base) = ^$base log($base⁻¹) = -1, bukan 1 (Salah)."
                        )
                    )
                }
            }
        }

        // 6 Eksponen questions in Unit 1
        for (i in 1..6) {
            val a = i + 1
            val targetX = i + 2
            list.add(
                TkaQuestion(
                    id = "mw_u1_exp_$i",
                    subjectId = "mat_wajib",
                    unitId = "mat_wajib_u1",
                    subtopic = "Eksponen",
                    difficulty = DifficultyTier.fromLevel((i % 3) + 1),
                    type = if (i % 2 == 1) QuestionType.SINGLE_CHOICE else QuestionType.TRUE_FALSE_MATRIX,
                    stem = if (i % 2 == 1) {
                        "Penyelesaian dari persamaan eksponen 2^(3x - $a) = 2^(2x + ${targetX - a}) adalah x = ..."
                    } else {
                        "Perhatikan sifat eksponen pada ekspresi E = (3^$a × 3^$targetX) / 3^2. Tentukan Benar (B) atau Salah (S)!"
                    },
                    options = if (i % 2 == 1) {
                        listOf("$targetX", "${targetX + 1}", "${targetX - 1}", "${targetX + 2}", "${2 * targetX}")
                    } else {
                        listOf(
                            "Pangkat pada pembilang dapat dijumlahkan menjadi ${a + targetX}.",
                            "Bentuk sederhana E adalah 3^(${a + targetX - 2}).",
                            "Nilai E selalu bernilai negatif."
                        )
                    },
                    correctAnswerCode = if (i % 2 == 1) "0" else "B,B,S",
                    explanation = if (i % 2 == 1) {
                        "Samakan pangkat basis 2: 3x - $a = 2x + ${targetX - a} ⇒ x = $targetX."
                    } else {
                        "1) Sifat perkalian basis sama: 3^a × 3^b = 3^(a+b) = 3^(${a + targetX}) (Benar).\n2) Pembagian dengan 3² mengurangi pangkat sebesar 2 menjadi ${a + targetX - 2} (Benar).\n3) Perpangkatan bilangan positif 3^n selalu bernilai positif (Salah)."
                    }
                )
            )
        }

        // Units 2..6 of Matematika Wajib (18 questions per unit = 90 more questions, total 108!)
        val unitConfigs = listOf(
            Triple("mat_wajib_u2", "Fungsi Kuadrat", "Sistem Persamaan Linear"),
            Triple("mat_wajib_u3", "Barisan & Deret", "Barisan & Deret"),
            Triple("mat_wajib_u4", "Trigonometri Dasar", "Trigonometri Dasar"),
            Triple("mat_wajib_u5", "Statistika", "Peluang & Kombinatorika"),
            Triple("mat_wajib_u6", "Matriks & Transformasi", "Matriks & Transformasi")
        )

        unitConfigs.forEachIndexed { uIdx, (unitId, sub1, sub2) ->
            for (qIdx in 1..18) {
                val sub = if (qIdx <= 10) sub1 else sub2
                val diff = DifficultyTier.fromLevel(((qIdx - 1) % 3) + 1)
                val qType = when (qIdx % 3) {
                    1 -> QuestionType.SINGLE_CHOICE
                    2 -> QuestionType.MCMA
                    else -> QuestionType.TRUE_FALSE_MATRIX
                }
                list.add(buildMatWajibUnitQuestion(uIdx + 2, unitId, sub, qIdx, diff, qType))
            }
        }

        return list
    }

    private fun buildMatWajibUnitQuestion(
        unitNum: Int,
        unitId: String,
        subtopic: String,
        qIdx: Int,
        diff: DifficultyTier,
        qType: QuestionType
    ): TkaQuestion {
        val id = "mw_u${unitNum}_$qIdx"
        val k = qIdx + 1
        return when (unitNum) {
            2 -> { // Fungsi Kuadrat & SPL
                val r1 = qIdx
                val r2 = qIdx + 2
                val sum = r1 + r2
                val prod = r1 * r2
                when (qType) {
                    QuestionType.SINGLE_CHOICE -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Jika x₁ dan x₂ adalah akar-akar persamaan kuadrat x² - ${sum}x + $prod = 0, maka nilai dari x₁² + x₂² adalah...",
                        options = listOf("${sum * sum - 2 * prod}", "${sum * sum - prod}", "${sum + prod}", "${2 * prod}", "${sum * sum}"),
                        correctAnswerCode = "0",
                        explanation = "Dari persamaan x² - ${sum}x + $prod = 0 diperoleh x₁ + x₂ = $sum dan x₁·x₂ = $prod.\nMaka x₁² + x₂² = (x₁ + x₂)² - 2x₁x₂ = $sum² - 2($prod) = ${sum * sum - 2 * prod}."
                    )
                    QuestionType.MCMA -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Diketahui fungsi kuadrat f(x) = x² - ${2 * k}x + ${k * k - 1}. Manakah karakteristik grafik f(x) yang BENAR? (Pilih semua yang benar)",
                        options = listOf(
                            "Parabola terbuka ke atas karena koefisien x² positif (a = 1 > 0)",
                            "Sumbu simetri berada pada garis x = $k",
                            "Nilai minimum fungsi adalah y = -1",
                            "Grafik tidak memotong sumbu-X"
                        ),
                        correctAnswerCode = "0,1,2",
                        explanation = "f(x) = (x - $k)² - 1.\n• a = 1 > 0 sehingga terbuka ke atas (Benar).\n• Sumbu simetri xp = $k (Benar).\n• Nilai puncak minimum yp = -1 (Benar).\n• Karena D > 0, grafik memotong sumbu-X di dua titik (pernyataan 4 Salah)."
                    )
                    QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Diberikan sistem persamaan linear 2x + y = ${3 * k} dan x - y = 0. Tentukan Benar (B) atau Salah (S)!",
                        options = listOf(
                            "Nilai x yang memenuhi adalah $k.",
                            "Nilai x + y sama dengan ${2 * k}.",
                            "Nilai x - 2y adalah positif."
                        ),
                        correctAnswerCode = "B,B,S",
                        explanation = "Dari x - y = 0 ⇒ x = y. Substitusi ke 2x + x = ${3 * k} ⇒ 3x = ${3 * k} ⇒ x = $k dan y = $k.\n1) x = $k (Benar).\n2) x + y = ${2 * k} (Benar).\n3) x - 2y = -$k < 0 (Salah)."
                    )
                }
            }
            3 -> { // Barisan & Deret
                val a = qIdx * 2
                val b = 3
                val u10 = a + 9 * b
                val s10 = 5 * (2 * a + 9 * b)
                when (qType) {
                    QuestionType.SINGLE_CHOICE -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Suatu barisan aritmetika memiliki suku pertama U₁ = $a dan beda b = $b. Jumlah 10 suku pertama (S₁₀) deret tersebut adalah...",
                        options = listOf("$s10", "$u10", "${s10 + 15}", "${s10 - 10}", "${u10 * 5}"),
                        correctAnswerCode = "0",
                        explanation = "Rumus jumlah n suku pertama deret aritmetika: Sₙ = (n/2)(2a + (n-1)b).\nS₁₀ = (10/2)(2($a) + 9($b)) = 5(${2 * a + 27}) = $s10."
                    )
                    QuestionType.MCMA -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Diketahui deret geometri tak hingga dengan suku pertama a = ${k * 4} dan rasio r = 1/2. Manakah pernyataan yang benar?",
                        options = listOf(
                            "Deret bersifat konvergen karena |r| < 1",
                            "Suku kedua deret tersebut adalah ${k * 2}",
                            "Jumlah tak hingga S∞ = ${k * 8}",
                            "Suku ke-4 bernilai lebih besar dari suku ke-2"
                        ),
                        correctAnswerCode = "0,1,2",
                        explanation = "• |r| = 1/2 < 1 ⇒ konvergen (Benar).\n• U₂ = a·r = ${k * 4} × (1/2) = ${k * 2} (Benar).\n• S∞ = a / (1 - r) = ${k * 4} / (1/2) = ${k * 8} (Benar).\n• Karena r = 1/2, suku-sukunya mengecil (pernyataan 4 Salah)."
                    )
                    QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Pada barisan aritmetika dengan rumus suku ke-n: Uₙ = 4n + $k, tentukan Benar (B) atau Salah (S)!",
                        options = listOf(
                            "Suku pertama (U₁) bernilai ${4 + k}.",
                            "Beda antar suku (b) adalah 4.",
                            "Selisih U₁₀ - U₅ adalah 15."
                        ),
                        correctAnswerCode = "B,B,S",
                        explanation = "1) U₁ = 4(1) + $k = ${4 + k} (Benar).\n2) Koefisien n pada Uₙ linear adalah beda b = 4 (Benar).\n3) U₁₀ - U₅ = 5b = 5(4) = 20 ≠ 15 (Salah)."
                    )
                }
            }
            4 -> { // Trigonometri Dasar
                val scale = (qIdx % 5) + 2
                when (qType) {
                    QuestionType.SINGLE_CHOICE -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Pada segitiga siku-siku ABC (siku-siku di B), diketahui panjang sisi AB = ${3 * scale} cm dan BC = ${4 * scale} cm. Nilai dari sin A + cos C adalah...",
                        options = listOf("8/5", "6/5", "7/5", "1", "4/5"),
                        correctAnswerCode = "0",
                        explanation = "Panjang sisi miring AC = √((${3 * scale})² + (${4 * scale})²) = ${5 * scale} cm.\nsin A = BC/AC = 4/5 dan cos C = BC/AC = 4/5.\nJadi sin A + cos C = 4/5 + 4/5 = 8/5."
                    )
                    QuestionType.MCMA -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Manakah di antara identitas dan nilai sudut trigonometri berikut yang BENAR? (Pilih semua yang benar)",
                        options = listOf(
                            "sin²(${qIdx * 5}°) + cos²(${qIdx * 5}°) = 1",
                            "sin 150° = 1/2",
                            "cos 120° = -1/2",
                            "tan 45° = √3"
                        ),
                        correctAnswerCode = "0,1,2",
                        explanation = "• Identitas Pythagoras sin²θ + cos²θ = 1 berlaku untuk setiap sudut θ (Benar).\n• sin 150° = sin(180° - 30°) = sin 30° = 1/2 (Benar).\n• cos 120° = -cos 60° = -1/2 (Benar).\n• tan 45° = 1 ≠ √3 (Salah)."
                    )
                    QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Pada ∆ABC diketahui sisi a = ${2 * scale} cm, sudut A = 30°, dan sudut B = 90°. Tentukan Benar (B) atau Salah (S)!",
                        options = listOf(
                            "Menurut aturan sinus, a / sin 30° = b / sin 90°.",
                            "Panjang sisi b adalah ${4 * scale} cm.",
                            "Sudut C besarnya 45°."
                        ),
                        correctAnswerCode = "B,B,S",
                        explanation = "1) Aturan sinus: a/sin A = b/sin B (Benar).\n2) b = a / (1/2) = 2 × ${2 * scale} = ${4 * scale} cm (Benar).\n3) Jumlah sudut segitiga 180°, sehingga C = 180° - 30° - 90° = 60° ≠ 45° (Salah)."
                    )
                }
            }
            5 -> { // Statistika & Peluang
                val baseMean = 70 + qIdx
                when (qType) {
                    QuestionType.SINGLE_CHOICE -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Rata-rata nilai ujian TKA dari 4 siswa adalah $baseMean. Jika nilai siswa ke-5 sebesar ${baseMean + 10} digabungkan, maka rata-rata gabungan ke-5 siswa tersebut menjadi...",
                        options = listOf("${baseMean + 2}", "${baseMean + 5}", "${baseMean + 1}", "$baseMean", "${baseMean + 4}"),
                        correctAnswerCode = "0",
                        explanation = "Jumlah nilai 4 siswa = 4 × $baseMean.\nSetelah ditambah ${baseMean + 10}, rata-rata baru = (4 × $baseMean + $baseMean + 10) / 5 = (5 × $baseMean + 10) / 5 = $baseMean + 2 = ${baseMean + 2}."
                    )
                    QuestionType.MCMA -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Dari ${qIdx + 4} kandidat pengurus OSIS, akan dipilih beberapa tim. Manakah pernyataan kombinatorika berikut yang BENAR?",
                        options = listOf(
                            "Banyak cara memilih 1 ketua dari ${qIdx + 4} orang adalah ${qIdx + 4} cara",
                            "Banyak permutasi 2 jabatan berbeda (Ketua & Sekretaris) dari 5 orang adalah 20",
                            "Nilai kombinasi C(5, 2) sama dengan C(5, 3) yaitu 10",
                            "Peluang suatu kejadian dapat bernilai lebih dari 1"
                        ),
                        correctAnswerCode = "0,1,2",
                        explanation = "• C(${qIdx + 4}, 1) = ${qIdx + 4} (Benar).\n• P(5,2) = 5 × 4 = 20 (Benar).\n• Sifat simetri kombinasi C(n,r) = C(n, n-r), jadi C(5,2) = C(5,3) = 10 (Benar).\n• Peluang selalu berada pada rentang 0 ≤ P(A) ≤ 1 (Salah)."
                    )
                    QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Sekumpulan data terurut terdiri atas: ${k}, ${k + 2}, ${k + 4}, ${k + 6}, ${k + 8}. Tentukan Benar (B) atau Salah (S)!",
                        options = listOf(
                            "Median data tersebut adalah ${k + 4}.",
                            "Rata-rata (mean) sama dengan median yaitu ${k + 4}.",
                            "Jangkauan (range) data tersebut adalah 12."
                        ),
                        correctAnswerCode = "B,B,S",
                        explanation = "1) Data tengah (ke-3) adalah ${k + 4} (Benar).\n2) Karena data berjarak sama (simetris), mean = median = ${k + 4} (Benar).\n3) Jangkauan = data terbesar - terkecil = (${k + 8}) - $k = 8 ≠ 12 (Salah)."
                    )
                }
            }
            else -> { // Unit 6: Matriks & Transformasi
                val dA = qIdx + 2
                val detVal = dA * 3 - 4
                when (qType) {
                    QuestionType.SINGLE_CHOICE -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Diketahui matriks M = [[$dA, 2], [2, 3]]. Determinan dari matriks M adalah...",
                        options = listOf("$detVal", "${dA * 3 + 4}", "${dA + 3}", "${detVal - 2}", "${dA * 2}"),
                        correctAnswerCode = "0",
                        explanation = "Determinan matriks 2×2 [[a, b], [c, d]] adalah ad - bc.\ndet(M) = ($dA)(3) - (2)(2) = ${dA * 3} - 4 = $detVal."
                    )
                    QuestionType.MCMA -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Titik P($qIdx, ${qIdx + 3}) ditransformasikan pada bidang Kartesius. Manakah pernyataan bayangan yang BENAR?",
                        options = listOf(
                            "Bayangan P oleh translasi T(2, -1) adalah P'(${qIdx + 2}, ${qIdx + 2})",
                            "Bayangan P oleh pencerminan terhadap sumbu-X adalah ($qIdx, -${qIdx + 3})",
                            "Bayangan P oleh dilatasi [O, 2] adalah (${2 * qIdx}, ${2 * (qIdx + 3)})",
                            "Translasi mengubah ukuran luas bangun datar"
                        ),
                        correctAnswerCode = "0,1,2",
                        explanation = "• Translasi menjumlahkan komponen: ($qIdx+2, ${qIdx+3}-1) = (${qIdx+2}, ${qIdx+2}) (Benar).\n• Refleksi sumbu-X mengubah tanda ordinat y menjadi -y (Benar).\n• Dilatasi pusat (0,0) faktor 2 mengalikan koordinat dengan 2 (Benar).\n• Translasi adalah transformasi isometri yang mempertahankan luas (Salah)."
                    )
                    QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                        id = id, subjectId = "mat_wajib", unitId = unitId, subtopic = subtopic,
                        difficulty = diff, type = qType,
                        stem = "Diketahui matriks identitas I = [[1, 0], [0, 1]] dan matriks A berukuran 2×2 dengan det(A) = $k. Tentukan Benar (B) atau Salah (S)!",
                        options = listOf(
                            "Hasil kali A × I sama dengan matriks A.",
                            "Determinan dari matriks transpose Aᵀ adalah $k.",
                            "Matriks A merupakan matriks singular."
                        ),
                        correctAnswerCode = "B,B,S",
                        explanation = "1) Sifat matriks identitas: A·I = I·A = A (Benar).\n2) det(Aᵀ) = det(A) = $k (Benar).\n3) Matriks singular memiliki determinan 0, sedangkan det(A) = $k ≠ 0 (Salah)."
                    )
                }
            }
        }
    }

    /**
     * Generates 108 authentic TKA Matematika Lanjut questions across 6 units (18 questions per unit).
     */
    fun getMatLanjutQuestions(): List<TkaQuestion> {
        val list = mutableListOf<TkaQuestion>()
        val unitConfigs = listOf(
            Triple("mat_lanjut_u1", "Polinomial & Teorema Sisa", "Polinomial & Teorema Sisa"),
            Triple("mat_lanjut_u2", "Limit Fungsi", "Turunan & Optimasi"),
            Triple("mat_lanjut_u3", "Integral Kalkulus", "Integral Kalkulus"),
            Triple("mat_lanjut_u4", "Vektor & Proyeksi", "Irisan Kerucut & Lingkaran"),
            Triple("mat_lanjut_u5", "Trigonometri Analitik", "Trigonometri Analitik"),
            Triple("mat_lanjut_u6", "Distribusi Peluang", "Distribusi Peluang")
        )

        unitConfigs.forEachIndexed { uIdx, (unitId, sub1, sub2) ->
            for (qIdx in 1..18) {
                val sub = if (qIdx <= 9) sub1 else sub2
                val diff = DifficultyTier.fromLevel(((qIdx - 1) % 3) + 1)
                val qType = when (qIdx % 3) {
                    1 -> QuestionType.SINGLE_CHOICE
                    2 -> QuestionType.MCMA
                    else -> QuestionType.TRUE_FALSE_MATRIX
                }
                val id = "ml_u${uIdx + 1}_$qIdx"
                val k = qIdx + 1
                val question = when (uIdx + 1) {
                    1 -> { // Polinomial
                        val rem = 2 + k + 5
                        when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Sisa pembagian suku banyak P(x) = 2x³ + ${k}x² - 3x + 8 oleh (x - 1) adalah...",
                                listOf("$rem", "${rem - 2}", "${rem + 3}", "${2 * k}", "${rem - 5}"),
                                "0",
                                "Menurut Teorema Sisa, sisa pembagian P(x) oleh (x - 1) adalah P(1) = 2(1)³ + $k(1)² - 3(1) + 8 = 2 + $k - 3 + 8 = $rem."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Diketahui polinomial P(x) = (x - 1)(x - 2)(x + $k). Manakah pernyataan yang BENAR?",
                                listOf(
                                    "(x - 1) merupakan faktor dari P(x)",
                                    "Derajat polinomial P(x) adalah 3",
                                    "Nilai P(2) = 0",
                                    "Jumlah akar-akar P(x) adalah ${3 + k}"
                                ),
                                "0,1,2",
                                "• Karena P(1)=0 dan P(2)=0, maka (x-1) dan (x-2) adalah faktor (Benar).\n• Perkalian tiga faktor linear menghasilkan polinomial berderajat 3 (Benar).\n• Akar-akarnya adalah 1, 2, dan -$k sehingga jumlahnya 3 - $k, bukan 3 + $k (Pernyataan 4 Salah)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Suku banyak f(x) dibagi (x - 2) bersisa $k dan dibagi (x + 1) bersisa 4. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Berdasarkan teorema sisa, nilai f(2) = $k.",
                                    "Nilai f(-1) = 4.",
                                    "Sisa pembagian f(x) oleh (x - 2)(x + 1) berderajat paling tinggi 2."
                                ),
                                "B,B,S",
                                "1) f(2) = $k (Benar).\n2) f(-1) = 4 (Benar).\n3) Pembagi berderajat 2 menghasilkan sisa berderajat paling tinggi 1 (linear ax + b), bukan derajat 2 (Salah)."
                            )
                        }
                    }
                    2 -> { // Limit & Turunan
                        when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Nilai dari lim(x→$k) (x² - ${k * k}) / (x - $k) adalah...",
                                listOf("${2 * k}", "$k", "${k * k}", "0", "${2 * k + 2}"),
                                "0",
                                "Faktorkan pembilang: (x - $k)(x + $k) / (x - $k) = x + $k.\nSubstitusi x = $k menghasilkan $k + $k = ${2 * k}."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Diberikan fungsi f(x) = x³ - ${3 * k}x + 5. Manakah pernyataan kalkulus berikut yang BENAR?",
                                listOf(
                                    "Turunan pertama fungsi adalah f'(x) = 3x² - ${3 * k}",
                                    "Turunan kedua fungsi adalah f''(x) = 6x",
                                    "Titik stasioner dicapai saat x² = $k",
                                    "Gradien garis singgung di x = 0 adalah positif"
                                ),
                                "0,1,2",
                                "• f'(x) = 3x² - ${3 * k} (Benar).\n• f''(x) = 6x (Benar).\n• Syarat stasioner f'(x) = 0 ⇔ 3x² = ${3 * k} ⇔ x² = $k (Benar).\n• f'(0) = -${3 * k} < 0 (Salah)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Terkait limit trigonometri lim(x→0) (sin(${k}x) / (2x)), tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Nilai limit tersebut adalah $k/2.",
                                    "Sifat dasar yang digunakan adalah lim(u→0) (sin u / u) = 1.",
                                    "Jika sin(${k}x) diganti cos(${k}x), nilai limitnya tetap $k/2."
                                ),
                                "B,B,S",
                                "1) lim(x→0) sin(${k}x)/(2x) = $k/2 (Benar).\n2) Menggunakan teorema limit trigonometri dasar (Benar).\n3) cos(0) = 1 sehingga 1/0 menuju tak hingga (Salah)."
                            )
                        }
                    }
                    3 -> { // Integral Kalkulus
                        when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Nilai dari integral tentu ∫₀² (3x² + ${2 * k}x) dx adalah...",
                                listOf("${8 + 4 * k}", "${4 + 2 * k}", "${8 + 2 * k}", "${6 + 4 * k}", "${12 + 4 * k}"),
                                "0",
                                "Antiturunan dari 3x² + ${2 * k}x adalah F(x) = x³ + ${k}x².\nEvaluasi batas 0 sampai 2: F(2) - F(0) = (2³ + $k(2²)) - 0 = 8 + ${4 * k} = ${8 + 4 * k}."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Manakah pernyataan mengenai integral tak tentu dan luas daerah berikut yang BENAR?",
                                listOf(
                                    "∫ 2x dx = x² + C",
                                    "∫ cos(x) dx = sin(x) + C",
                                    "Luas daerah di bawah kurva y = x pada [0, $k] adalah ${k * k}/2",
                                    "∫ sin(x) dx = cos(x) + C"
                                ),
                                "0,1,2",
                                "• Turunan x² adalah 2x (Benar).\n• Turunan sin(x) adalah cos(x) (Benar).\n• ∫₀^$k x dx = [x²/2]₀^$k = ${k * k}/2 (Benar).\n• ∫ sin(x) dx = -cos(x) + C (Pernyataan 4 Salah)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Diberikan sifat-sifat integral tentu pada interval [a, b]. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "∫ₐᵃ f(x) dx = 0.",
                                    "∫ₐᵇ $k·f(x) dx = $k · ∫ₐᵇ f(x) dx.",
                                    "∫ₐᵇ f(x) dx = ∫ᵇₐ f(x) dx untuk setiap fungsi tak-nol."
                                ),
                                "B,B,S",
                                "1) Integral dengan batas atas dan bawah sama bernilai 0 (Benar).\n2) Konstanta dapat dikeluarkan dari tanda integral (Benar).\n3) Membalik batas integral mengubah tanda menjadi negatif: ∫ₐᵇ f(x) dx = -∫ᵇₐ f(x) dx (Salah)."
                            )
                        }
                    }
                    4 -> { // Vektor & Irisan Kerucut
                        val dot = 2 * k + 6
                        when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Diketahui vektor u = (2, 3, -1) dan v = ($k, 2, 0). Hasil kali titik (dot product) u · v adalah...",
                                listOf("$dot", "${dot - 4}", "${2 * k}", "${dot + 2}", "${k + 6}"),
                                "0",
                                "u · v = (2)($k) + (3)(2) + (-1)(0) = ${2 * k} + 6 + 0 = $dot."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Diketahui persamaan lingkaran L: (x - $k)² + (y + 2)² = 25. Manakah pernyataan yang BENAR?",
                                listOf(
                                    "Pusat lingkaran berada di titik ($k, -2)",
                                    "Jari-jari lingkaran adalah r = 5",
                                    "Diameter lingkaran adalah 10",
                                    "Titik ($k, -2) terletak tepat pada keliling lingkaran"
                                ),
                                "0,1,2",
                                "Dari bentuk baku (x - a)² + (y - b)² = r²:\n• Pusat (a, b) = ($k, -2) (Benar).\n• r = √25 = 5 dan diameter = 10 (Benar).\n• Titik pusat tidak terletak pada keliling lingkaran (Salah)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Dua vektor tak-nol a dan b membentuk sudut θ. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Jika a ⊥ b (saling tegak lurus), maka a · b = 0.",
                                    "Panjang vektor (3, 4, 0) adalah 5 satuan.",
                                    "Jika θ = 0°, maka a · b = 0."
                                ),
                                "B,B,S",
                                "1) cos 90° = 0 sehingga a · b = 0 (Benar).\n2) √(3² + 4² + 0²) = 5 (Benar).\n3) Saat θ = 0°, cos 0° = 1 sehingga a · b = |a||b| ≠ 0 (Salah)."
                            )
                        }
                    }
                    5 -> { // Trigonometri Analitik
                        when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Jika sin A = 3/5 (A di kuadran I), maka nilai dari sin(2A) adalah...",
                                listOf("24/25", "12/25", "7/25", "6/5", "16/25"),
                                "0",
                                "Karena sin A = 3/5 di kuadran I, maka cos A = 4/5.\nRumus sudut rangkap: sin(2A) = 2 sin A cos A = 2(3/5)(4/5) = 24/25."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Manakah di antara rumus trigonometri analitik berikut yang identitasnya BENAR?",
                                listOf(
                                    "sin(A + B) = sin A cos B + cos A sin B",
                                    "cos(2A) = cos²A - sin²A",
                                    "cos(2A) = 1 - 2sin²A",
                                    "sin(2A) = sin²A + cos²A"
                                ),
                                "0,1,2",
                                "Identitas penjumlahan sudut sin(A+B) dan sudut rangkap cos(2A) pada opsi 1, 2, dan 3 adalah benar, sedangkan sin²A + cos²A = 1, bukan sin(2A)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Terkait fungsi f(x) = $k cos(2x) + 1, tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Nilai maksimum fungsi f(x) adalah ${k + 1}.",
                                    "Nilai minimum fungsi f(x) adalah ${1 - k}.",
                                    "Periode dasar fungsi tersebut adalah 2π."
                                ),
                                "B,B,S",
                                "1) Maksimum saat cos(2x) = 1 yaitu $k(1) + 1 = ${k + 1} (Benar).\n2) Minimum saat cos(2x) = -1 yaitu -$k + 1 = ${1 - k} (Benar).\n3) Periode cos(2x) adalah 2π/2 = π, bukan 2π (Salah)."
                            )
                        }
                    }
                    else -> { // Unit 6: Distribusi Peluang
                        val nTrials = k + 4
                        when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Suatu variabel acak X berdistribusi Binomial B(n = ${nTrials * 2}, p = 0,5). Nilai harapan (rata-rata μ = E[X]) dari variabel acak tersebut adalah...",
                                listOf("$nTrials", "${nTrials / 2}", "${nTrials * 2}", "${nTrials + 2}", "${nTrials - 1}"),
                                "0",
                                "Untuk distribusi Binomial X ~ B(n, p), nilai harapan E[X] = n · p = ${nTrials * 2} × 0,5 = $nTrials."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Manakah karakteristik kurva Distribusi Normal Baku Z ~ N(0, 1) yang BENAR?",
                                listOf(
                                    "Kurva berbentuk lonceng simetris terhadap Z = 0",
                                    "Total luas daerah di bawah seluruh kurva sama dengan 1",
                                    "Nilai mean, median, dan modus berimpit di titik tengah",
                                    "Varians distribusi normal baku bernilai 0"
                                ),
                                "0,1,2",
                                "Distribusi normal baku N(0,1) memiliki mean 0 dan varians 1 (bukan 0), berbentuk simetris lonceng dengan total peluang (luas) = 1."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "mat_lanjut", unitId, sub, diff, qType,
                                "Sebuah koin setimbang dilempar sebanyak 4 kali. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Banyak ruang sampel seluruhnya adalah 2⁴ = 16.",
                                    "Peluang muncul tepat 4 sisi Angka adalah 1/16.",
                                    "Peluang muncul tepat 2 sisi Angka adalah 1/2."
                                ),
                                "B,B,S",
                                "1) n(S) = 2⁴ = 16 (Benar).\n2) P(X=4) = C(4,4)(1/2)⁴ = 1/16 (Benar).\n3) P(X=2) = C(4,2)/16 = 6/16 = 3/8 ≠ 1/2 (Salah)."
                            )
                        }
                    }
                }
                list.add(question)
            }
        }
        return list
    }
}
