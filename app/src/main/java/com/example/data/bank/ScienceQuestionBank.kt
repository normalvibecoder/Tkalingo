package com.example.data.bank

import com.example.data.model.DifficultyTier
import com.example.data.model.QuestionType
import com.example.data.model.TkaQuestion

object ScienceQuestionBank {

    /**
     * Generates 108 authentic TKA Fisika questions across 6 units.
     */
    fun getFisikaQuestions(): List<TkaQuestion> {
        val list = mutableListOf<TkaQuestion>()
        val units = listOf(
            Triple("fisika_u1", "Kinematika & Dinamika", "Dinamika Rotasi"),
            Triple("fisika_u2", "Usaha & Energi", "Impuls & Momentum"),
            Triple("fisika_u3", "Fluida Statis & Dinamis", "Termodinamika"),
            Triple("fisika_u4", "Gelombang & Optik", "Gelombang & Optik"),
            Triple("fisika_u5", "Listrik Arus Searah & Bolak-Balik", "Medan Magnet & Induksi"),
            Triple("fisika_u6", "Fisika Modern & Kuantum", "Fisika Modern & Kuantum")
        )

        units.forEachIndexed { uIdx, (unitId, sub1, sub2) ->
            for (qIdx in 1..18) {
                val sub = if (qIdx <= 9) sub1 else sub2
                val diff = DifficultyTier.fromLevel(((qIdx - 1) % 3) + 1)
                val qType = when (qIdx % 3) {
                    1 -> QuestionType.SINGLE_CHOICE
                    2 -> QuestionType.MCMA
                    else -> QuestionType.TRUE_FALSE_MATRIX
                }
                val id = "fis_u${uIdx + 1}_$qIdx"
                val m = (qIdx % 6) + 2
                val v = (qIdx % 5) + 4
                list.add(
                    when (uIdx + 1) {
                        1 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Sebuah benda bermassa $m kg mula-mula diam di atas bidang datar licin, kemudian ditarik gaya mendatar konstan F = ${m * v} N selama 3 sekon. Besar kecepatan benda pada akhir detik ke-3 adalah...",
                                listOf("${3 * v} m/s", "${v} m/s", "${2 * v} m/s", "${m * v} m/s", "${4 * v} m/s"),
                                "0",
                                "Percepatan a = F / m = ${m * v} / $m = $v m/s².\nKecepatan saat t = 3 s adalah v_t = v₀ + a·t = 0 + ($v)(3) = ${3 * v} m/s."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Terkait gerak parabola dan dinamika benda tegar (Soal #$qIdx), manakah pernyataan fisika berikut yang BENAR?",
                                listOf(
                                    "Di titik tertinggi lintasan parabola, komponen kecepatan vertikal v_y = 0",
                                    "Komponen kecepatan horizontal v_x pada gerak parabola (tanpa hambatan udara) bersifat konstan",
                                    "Momen gaya (torsi) sebanding dengan hasil kali gaya dan lengan momen tegak lurus (τ = r · F sin θ)",
                                    "Percepatan gravitasi bernilai nol ketika benda berada di puncak lintasan"
                                ),
                                "0,1,2",
                                "Di puncak lintasan parabola v_y = 0 namun percepatan gravitasi g tetap bekerja ke bawah (~9,8 m/s²). v_x konstan dan τ = r F sin θ."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Sebuah silinder pejal (I = ½MR²) bermassa $m kg menggelinding tanpa slip pada bidang datar. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Silinder memiliki energi kinetik translasi sekaligus energi kinetik rotasi.",
                                    "Hubungan kelajuan linier dan kecepatan sudut tanpa slip adalah v = ω·R.",
                                    "Momen inersia silinder tidak bergantung pada jari-jari R."
                            ),
                                "B,B,S",
                                "1) Menggelinding = translasi + rotasi (Benar).\n2) Syarat tanpa slip v = ωR (Benar).\n3) I = ½MR² bergantung kuadratik pada jari-jari R (Salah)."
                            )
                        }
                        2 -> {
                            val ek = m * v * v / 2
                            when (qType) {
                                QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                    id, "fisika", unitId, sub, diff, qType,
                                    "Sebuah benda bermassa $m kg bergerak dengan kelajuan $v m/s. Besar energi kinetik translasi benda tersebut adalah...",
                                    listOf("$ek Joule", "${ek * 2} Joule", "${m * v} Joule", "${ek + 10} Joule", "${ek / 2} Joule"),
                                    "0",
                                    "Energi kinetik EK = ½ m v² = ½ × $m × ($v)² = $ek Joule."
                                )
                                QuestionType.MCMA -> TkaQuestion(
                                    id, "fisika", unitId, sub, diff, qType,
                                    "Pada peristiwa tumbukan dua bola di bidang datar licin tanpa gaya luar (#$qIdx), manakah hukum fisika yang BERLAKU?",
                                    listOf(
                                        "Hukum kekekalan momentum linier berlaku untuk semua jenis tumbukan (lenting sempurna, sebagian, maupun tidak lenting)",
                                        "Impuls yang bekerja pada benda sama dengan perubahan momentumnya (I = Δp)",
                                        "Pada tumbukan lenting sempurna, koefisien restitusi e = 1 dan energi kinetik total kekal",
                                        "Pada tumbukan tidak lenting sama sekali, koefisien restitusi e = 1"
                                    ),
                                    "0,1,2",
                                    "Momentum kekal pada setiap tumbukan terisolasi, I = Δp, dan e = 1 untuk tumbukan lenting sempurna (sedangkan tidak lenting sama sekali memiliki e = 0)."
                                )
                                QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                    id, "fisika", unitId, sub, diff, qType,
                                    "Sebuah benda bermassa $m kg jatuh bebas dari ketinggian ${v * 5} m (g = 10 m/s²). Tentukan Benar (B) atau Salah (S)!",
                                    listOf(
                                        "Energi mekanik benda selama jatuh bebas (tanpa gesekan udara) bersifat kekal.",
                                        "Saat ketinggian benda berkurang, energi potensial gravitasi berubah menjadi energi kinetik.",
                                        "Usaha oleh gaya gravitasi selama benda turun bernilai negatif."
                                    ),
                                    "B,B,S",
                                    "1) EM = EP + EK konstan (Benar).\n2) Penurunan EP = kenaikan EK (Benar).\n3) Karena arah perpindahan searah gaya gravitasi (ke bawah), usaha gravitasi bernilai positif (Salah)."
                                )
                            }
                        }
                        3 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Suatu mesin kalor Carnot bekerja di antara reservoir suhu tinggi T₁ = 600 K dan reservoir suhu rendah T₂ = ${150 + (qIdx % 4) * 50} K. Efisiensi maksimum mesin Carnot tersebut adalah...",
                                listOf(
                                    "${((600 - (150 + (qIdx % 4) * 50)) * 100) / 600}%",
                                    "90%",
                                    "20%",
                                    "100%",
                                    "15%"
                                ),
                                "0",
                                "Efisiensi mesin Carnot η = (1 - T₂/T₁) × 100% = ((600 - ${150 + (qIdx % 4) * 50}) / 600) × 100% = ${((600 - (150 + (qIdx % 4) * 50)) * 100) / 600}%."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Manakah pernyataan yang BENAR mengenai mekanika fluida dan gas ideal (#$qIdx)?",
                                listOf(
                                    "Menurut asas kontinuitas (A₁v₁ = A₂v₂), kelajuan fluida ideal lebih besar pada penampang pipa yang lebih sempit",
                                    "Menurut asas Bernoulli, daerah dengan kelajuan fluida tinggi memiliki tekanan statis yang lebih rendah",
                                    "Pada proses isotermik gas ideal (suhu tetap), perubahan energi dalam ΔU = 0",
                                    "Gaya apung Archimedes tidak bergantung pada massa jenis fluida"
                            ),
                                "0,1,2",
                                "Kontinuitas A₁v₁ = A₂v₂, asas Bernoulli P + ½ρv² + ρgh = konstan, dan ΔU = 0 saat ΔT = 0 adalah benar. Gaya Archimedes F_A = ρ_f g V_celup bergantung pada massa jenis fluida."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Terkait Hukum I Termodinamika (Q = ΔU + W) pada gas ideal (#$qIdx), tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Pada proses isokhorik (volume tetap), usaha luar W = 0.",
                                    "Pada proses adiabatik, tidak ada kalor yang masuk atau keluar sistem (Q = 0).",
                                    "Mesin kalor nyata dapat memiliki efisiensi 100% tanpa membuang kalor ke suhu rendah."
                                ),
                                "B,B,S",
                                "1) Volume tetap ΔV = 0 ⇒ W = 0 (Benar).\n2) Adiabatik ⇒ Q = 0 (Benar).\n3) Menurut Hukum II Termodinamika (Kelvin-Planck), tidak ada mesin kalor yang berefisiensi 100% (Salah)."
                            )
                        }
                        4 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Sebuah gelombang transversal merambat dengan frekuensi f = ${m * 5} Hz dan panjang gelombang λ = $v m. Cepat rambat gelombang tersebut adalah...",
                                listOf("${m * 5 * v} m/s", "${m * 5 + v} m/s", "${m * v} m/s", "${m * 5 / 2} m/s", "${v * 2} m/s"),
                                "0",
                                "Cepat rambat gelombang v = λ · f = $v m × ${m * 5} Hz = ${m * 5 * v} m/s."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Manakah pernyataan tentang gelombang bunyi dan optika fisis (#$qIdx) berikut yang BENAR?",
                                listOf(
                                    "Ketika sumber bunyi mendekati pendengar yang diam, frekuensi yang terdengar lebih tinggi dari frekuensi asli (Efek Doppler)",
                                    "Interferensi maksimum (terang) pada celah ganda Young terjadi saat selisih lintasan merupakan kelipatan bulat panjang gelombang",
                                    "Gelombang cahaya merupakan gelombang elektromagnetik transversal yang dapat mengalami polarisasi",
                                    "Gelombang bunyi dapat merambat di ruang hampa udara"
                                ),
                                "0,1,2",
                                "Efek Doppler, interferensi konstruktif Young (d sin θ = nλ), dan sifat elektromagnetik cahaya adalah benar. Bunyi adalah gelombang mekanik yang membutuhkan medium."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Sebuah benda diletakkan di depan lensa cembung (konvergen) berfokus f = 10 cm pada jarak s = ${15 + qIdx} cm. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Lensa cembung memiliki jarak fokus bernilai positif (+).",
                                    "Karena benda berada di ruang II atau III (s > f), bayangan yang terbentuk bersifat nyata dan terbalik.",
                                    "Kekuatan lensa tersebut adalah -10 dioptri."
                                ),
                                "B,B,S",
                                "1) Fokus lensa konvergen positif (Benar).\n2) Untuk s > f pada lensa cembung, s' > 0 (nyata, terbalik) (Benar).\n3) P = 100/f(cm) = +10 dioptri, bukan -10 dioptri (Salah)."
                            )
                        }
                        5 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Dua resistor masing-masing R₁ = ${m * 2} Ω dan R₂ = ${m * 2} Ω disusun secara paralel lalu dihubungkan ke sumber tegangan V = ${m * v} Volt. Kuat arus total yang keluar dari sumber tegangan adalah...",
                                listOf("$v Ampere", "${v / 2} Ampere", "${2 * v} Ampere", "${m * v} Ampere", "1 Ampere"),
                                "0",
                                "Hambatan pengganti paralel dua resistor identik R_p = (${m * 2}) / 2 = $m Ω.\nMenurut Hukum Ohm, I_total = V / R_p = ${m * v} / $m = $v Ampere."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Manakah konsep kelistrikan dan kemagnetan (#$qIdx) berikut yang BENAR?",
                                listOf(
                                    "Menurut Hukum I Kirchhoff, jumlah arus listrik yang masuk ke suatu titik cabang sama dengan jumlah arus yang keluar",
                                    "Menurut Hukum Induksi Faraday, GGL induksi sebanding dengan laju perubahan fluks magnetik (ε = -N dΦ/dt)",
                                    "Arah arus induksi menentang perubahan fluks magnetik yangរimbulkan (Hukum Lenz)",
                                    "Muatan listrik yang diam di dalam medan magnet statis selalu mengalami gaya Lorentz maksimum"
                                ),
                                "0,1,2",
                                "Hukum Kirchhoff I, Faraday, dan Lenz benar. Gaya Lorentz F = q v B sin θ bernilai nol jika muatan diam (v = 0)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Kawat lurus panjang dialiri arus listrik searah I = $v A. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Di sekitar kawat berarus timbul medan magnet induksi (B = μ₀I / 2πa).",
                                    "Semakin jauh jarak titik dari kawat (a membesar), kuat medan magnetnya semakin kecil.",
                                    "Arah garis medan magnet sejajar dengan arah kawat lurus."
                                ),
                                "B,B,S",
                                "1) Rumus Biot-Savart kawat lurus panjang B = μ₀I/(2πa) (Benar).\n2) B berbanding terbalik dengan jarak a (Benar).\n3) Garis medan magnet melingkari kawat sesuai kaidah tangan kanan, bukan sejajar kawat (Salah)."
                            )
                        }
                        else -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Logam memiliki fungsi kerja (energi ambang) W₀ = 2,0 eV disinari foton berenergi E = ${m + 2},5 eV. Energi kinetik maksimum fotoelektron yang lepas dari permukaan logam adalah...",
                                listOf("${m},5 eV", "${m + 4},5 eV", "2,0 eV", "${m - 1},0 eV", "0 eV"),
                                "0",
                                "Persamaan efek fotolistrik Einstein: EK_maks = E_foton - W₀ = ${m + 2},5 eV - 2,0 eV = ${m},5 eV."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Manakah pernyataan Fisika Modern & Kuantum (#$qIdx) berikut yang BENAR?",
                                listOf(
                                    "Energi satu foton berbanding lurus dengan frekuensinya (E = h·f = hc/λ)",
                                    "Pada efek fotolistrik, penambahan intensitas cahaya (di atas frekuensi ambang) meningkatkan jumlah elektron yang lepas, bukan energi kinetik maksimumnya",
                                    "Menurut postulat relativitas khusus Einstein, kelajuan cahaya di ruang hampa adalah sama untuk semua kerangka acuan inersial",
                                    "Sinar gamma memiliki panjang gelombang lebih besar daripada gelombang radio"
                                ),
                                "0,1,2",
                                "E = hf, sifat intensitas pada efek fotolistrik, dan postulat relativitas Einstein adalah benar. Sinar gamma memiliki frekuensi tertinggi dan panjang gelombang terkecil."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "fisika", unitId, sub, diff, qType,
                                "Suatu unsur radioaktif memiliki waktu paruh T_½ = 5 hari dan massa awal N₀ = ${m * 8} gram. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Setelah 5 hari (1 waktu paruh), massa unsur yang tersisa adalah ${m * 4} gram.",
                                    "Setelah 10 hari (2 waktu paruh), massa unsur yang tersisa adalah ${m * 2} gram.",
                                    "Setelah 10 hari, unsur radioaktif tersebut habis tepat menjadi 0 gram."
                                ),
                                "B,B,S",
                                "1) t = 5 hari ⇒ N = N₀(1/2)¹ = ${m * 4} g (Benar).\n2) t = 10 hari ⇒ N = N₀(1/2)² = ${m * 2} g (Benar).\n3) Peluruhan radioaktif bersifat eksponensial N = N₀(1/2)^(t/T), tidak langsung habis menjadi 0 pada 2 waktu paruh (Salah)."
                            )
                        }
                    }
                )
            }
        }
        return list
    }

    /**
     * Generates 108 authentic TKA Kimia questions across 6 units.
     */
    fun getKimiaQuestions(): List<TkaQuestion> {
        val list = mutableListOf<TkaQuestion>()
        val units = listOf(
            Triple("kimia_u1", "Stoikiometri", "Stoikiometri"),
            Triple("kimia_u2", "Struktur Atom & Ikatan Kimia", "Struktur Atom & Ikatan Kimia"),
            Triple("kimia_u3", "Termokimia", "Laju Reaksi"),
            Triple("kimia_u4", "Kesetimbangan Kimia", "Asam Basa & Buffer"),
            Triple("kimia_u5", "Redoks & Elektrokimia", "Redoks & Elektrokimia"),
            Triple("kimia_u6", "Kimia Organik", "Kimia Organik")
        )

        units.forEachIndexed { uIdx, (unitId, sub1, sub2) ->
            for (qIdx in 1..18) {
                val sub = if (qIdx <= 9) sub1 else sub2
                val diff = DifficultyTier.fromLevel(((qIdx - 1) % 3) + 1)
                val qType = when (qIdx % 3) {
                    1 -> QuestionType.SINGLE_CHOICE
                    2 -> QuestionType.MCMA
                    else -> QuestionType.TRUE_FALSE_MATRIX
                }
                val id = "kim_u${uIdx + 1}_$qIdx"
                val mol = (qIdx % 5) + 1
                list.add(
                    when (uIdx + 1) {
                        1 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Sebanyak $mol mol gas metana (CH₄) dibakar sempurna menurut reaksi: CH₄(g) + 2O₂(g) → CO₂(g) + 2H₂O(g). Jumlah mol gas O₂ yang dibutuhkan dan mol H₂O yang dihasilkan berturut-turut adalah...",
                                listOf(
                                    "${2 * mol} mol O₂ dan ${2 * mol} mol H₂O",
                                    "$mol mol O₂ dan ${2 * mol} mol H₂O",
                                    "${2 * mol} mol O₂ dan $mol mol H₂O",
                                    "$mol mol O₂ dan $mol mol H₂O",
                                    "${3 * mol} mol O₂ dan ${2 * mol} mol H₂O"
                                ),
                                "0",
                                "Perbandingan koefisien reaksi CH₄ : O₂ : CO₂ : H₂O = 1 : 2 : 1 : 2.\nUntuk $mol mol CH₄, diperlukan 2 × $mol = ${2 * mol} mol O₂ dan dihasilkan 2 × $mol = ${2 * mol} mol H₂O."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Terkait konsep mol dan hukum dasar kimia (Soal #$qIdx), manakah pernyataan yang BENAR? (Ar C = 12, O = 16, H = 1)",
                                listOf(
                                    "Massa molar (Mr) dari CO₂ adalah 44 g/mol",
                                    "Pada suhu dan tekanan standar (STP: 0°C, 1 atm), 1 mol gas ideal menempati volume 22,4 liter",
                                    "Hukum Lavoisier menyatakan bahwa massa zat sebelum dan sesudah reaksi dalam sistem tertutup adalah tetap",
                                    "1 mol H₂O mengandung jumlah molekul yang lebih sedikit dibanding 1 mol CO₂"
                                ),
                                "0,1,2",
                                "Mr CO₂ = 12 + 2(16) = 44, V_STP = 22,4 L/mol, dan hukum kekekalan massa Lavoisier benar. 1 mol zat apa pun mengandung jumlah partikel yang sama (bilangan Avogadro 6,02 × 10²³)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Sebanyak ${mol * 40} gram NaOH (Mr = 40) dilarutkan dalam air hingga volume 1 liter. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Jumlah mol NaOH terlarut adalah $mol mol.",
                                    "Konsentrasi molaritas larutan NaOH tersebut adalah $mol M.",
                                    "NaOH termasuk larutan nonelektrolit."
                                ),
                                "B,B,S",
                                "1) n = massa/Mr = ${mol * 40}/40 = $mol mol (Benar).\n2) M = n/V = $mol/1 L = $mol M (Benar).\n3) NaOH adalah basa kuat sehingga merupakan elektrolit kuat (Salah)."
                            )
                        }
                        2 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Unsur X memiliki nomor atom 17. Konfigurasi elektron kulit dan letak golongan unsur X pada tabel periodik adalah...",
                                listOf(
                                    "2, 8, 7 — Golongan VIIA (Halogen), Periode 3",
                                    "2, 8, 7 — Golongan VIA, Periode 3",
                                    "2, 8, 8 — Golongan VIIIA, Periode 3",
                                    "2, 7, 8 — Golongan VIIA, Periode 2",
                                    "2, 8, 5, 2 — Golongan IIA, Periode 4"
                                ),
                                "0",
                                "Z = 17: 1s² 2s² 2p⁶ 3s² 3p⁵ (kulit: 2, 8, 7). Memiliki 7 elektron valensi pada kulit ke-3 sehingga berada di Golongan VIIA (17) dan Periode 3."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Manakah pernyataan mengenai ikatan kimia dan gaya antarmolekul (#$qIdx) berikut yang BENAR?",
                                listOf(
                                    "Ikatan ion terbentuk akibat serah terima elektron antara atom logam (elektronegativitas rendah) dan nonlogam (elektronegativitas tinggi)",
                                    "Molekul H₂O, NH₃, dan HF memiliki titik didih relatif tinggi karena adanya ikatan hidrogen antarmolekul",
                                    "Molekul CH₄ berbentuk tetrahedral simetris (sp³) sehingga bersifat nonpolar",
                                    "Senyawa kovalen nonpolar dalam bentuk cairan murni merupakan penghantar listrik yang sangat baik"
                                ),
                                "0,1,2",
                                "Ikatan ion, ikatan hidrogen pada H₂O/NH₃/HF, dan kepolaran CH₄ adalah benar. Cairan kovalen nonpolar tidak memiliki ion bebas sehingga tidak menghantarkan listrik."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Terkait kecenderungan sifat periodik unsur dalam satu periode dari kiri ke kanan (hingga golongan VIIA), tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Jari-jari atom cenderung semakin kecil karena muatan inti efektif meningkat.",
                                    "Energi ionisasi pertama cenderung semakin besar.",
                                    "Keelektronegatifan cenderung semakin kecil."
                                ),
                                "B,B,S",
                                "1) Jari-jari atom mengecil dari kiri ke kanan (Benar).\n2) Energi ionisasi cenderung naik (Benar).\n3) Keelektronegatifan dari kiri ke kanan semakin besar hingga golongan VIIA (Salah)."
                            )
                        }
                        3 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Diketahui persamaan termokimia pembentukan standar: N₂(g) + 3H₂(g) → 2NH₃(g) ΔH = -${mol * 92} kJ. Maka nilai entalpi pembentukan standar (ΔHf°) untuk 1 mol gas NH₃ adalah...",
                                listOf("-${mol * 46} kJ/mol", "+${mol * 46} kJ/mol", "-${mol * 92} kJ/mol", "-${mol * 184} kJ/mol", "+${mol * 92} kJ/mol"),
                                "0",
                                "Persamaan tersebut menghasilkan 2 mol NH₃ dengan ΔH = -${mol * 92} kJ. Untuk 1 mol NH₃, ΔHf° = (-${mol * 92}) / 2 = -${mol * 46} kJ/mol (reaksi eksoterm)."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Manakah faktor yang mempercepat LAJU REAKSI kimia (#$qIdx) beserta alasannya menurut teori tumbukan? (Pilih semua yang benar)",
                                listOf(
                                    "Kenaikan suhu meningkatkan energi kinetik rata-rata partikel sehingga frekuensi tumbukan efektif meningkat",
                                    "Penambahan katalis menyediakan jalur alternatif reaksi dengan energi aktivasi (Ea) yang lebih rendah",
                                    "Memperkecil ukuran partikel zat padat memperluas luas permukaan bidang sentuh",
                                    "Mengurangi konsentrasi pereaksi meningkatkan jumlah tumbukan per satuan waktu"
                                ),
                                "0,1,2",
                                "Suhu tinggi, katalis (menurunkan Ea), dan luas permukaan mempercepat laju reaksi, sedangkan mengurangi konsentrasi justru memperlambat laju reaksi."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Suatu reaksi memiliki persamaan laju v = k[A]²[B]. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Orde reaksi terhadap zat A adalah 2 dan terhadap zat B adalah 1.",
                                    "Orde reaksi total adalah 3.",
                                    "Jika konsentrasi A dinaikkan 2 kali lipat pada [B] tetap, laju reaksi menjadi 2 kali semula."
                                ),
                                "B,B,S",
                                "1) Orde A = 2, orde B = 1 (Benar).\n2) Orde total = 2 + 1 = 3 (Benar).\n3) Karena orde A adalah 2, jika [A] dijadikan 2 kali maka v menjadi 2² = 4 kali semula (Salah)."
                            )
                        }
                        4 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Larutan asam lemah HA 0,1 M memiliki tetapan ionisasi asam Ka = 10⁻⁵. Nilai pH larutan asam lemah tersebut adalah...",
                                listOf("3", "1", "5", "6", "11"),
                                "0",
                                "[H⁺] = √(Ka × M_a) = √(10⁻⁵ × 10⁻¹) = √(10⁻⁶) = 10⁻³ M.\nMaka pH = -log[H⁺] = -log(10⁻³) = 3."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Terkait kesetimbangan kimia dan larutan penyangga/buffer (#$qIdx), manakah pernyataan yang BENAR?",
                                listOf(
                                    "Larutan penyangga asam dapat dibuat dari campuran asam lemah (CH₃COOH) dan basa konjugasinya (CH₃COONa)",
                                    "pH larutan penyangga relatif stabil terhadap penambahan sedikit asam kuat, basa kuat, maupun pengenceran",
                                    "Menurut asas Le Chatelier, jika suhu sistem kesetimbangan dinaikkan, kesetimbangan bergeser ke arah reaksi endoterm",
                                    "Penambahan katalis menggeser letak kesetimbangan ke arah produk sehingga nilai Kc membesar"
                                ),
                                "0,1,2",
                                "Sifat buffer dan pergeseran Le Chatelier akibat suhu benar. Katalis hanya mempercepat tercapainya kesetimbangan tanpa menggeser arah kesetimbangan atau mengubah Kc."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Garam CH₃COONa dilarutkan dalam air. Tentukan Benar (B) atau Salah (S) mengenai sifat hidrolisis garam tersebut!",
                                listOf(
                                    "Garam CH₃COONa berasal dari asam lemah CH₃COOH dan basa kuat NaOH.",
                                    "Ion CH₃COO⁻ mengalami hidrolisis parsial menghasilkan ion OH⁻ sehingga larutan bersifat basa (pH > 7).",
                                    "Larutan CH₃COONa akan mengubah kertas lakmus biru menjadi merah."
                                ),
                                "B,B,S",
                                "1) Berasal dari asam lemah + basa kuat (Benar).\n2) Hidrolisis anion menghasilkan OH⁻, pH > 7 (Benar).\n3) Larutan basa membirukan lakmus merah, bukan memerahkan lakmus biru (Salah)."
                            )
                        }
                        5 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Diketahui potensial reduksi standar: Zn²⁺/Zn E° = -0,76 V dan Cu²⁺/Cu E° = +0,34 V. Potensial sel standar (E°sel) dari sel Volta Zn | Zn²⁺ || Cu²⁺ | Cu adalah...",
                                listOf("+1,10 V", "-1,10 V", "+0,42 V", "-0,42 V", "+0,76 V"),
                                "0",
                                "Cu memiliki E° lebih positif sehingga mengalami reduksi di katode, sedangkan Zn mengalami oksidasi di anode.\nE°sel = E°katode - E°anode = (+0,34 V) - (-0,76 V) = +1,10 V."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Manakah pernyataan yang BENAR mengenai reaksi redoks dan sel elektrokimia (#$qIdx)?",
                                listOf(
                                    "Di katode selalu terjadi reaksi reduksi, baik pada sel Volta maupun sel elektrolisis (KARED)",
                                    "Di anode selalu terjadi reaksi oksidasi (ANOKS)",
                                    "Pada senyawa KMnO₄, bilangan oksidasi atom Mn adalah +7",
                                    "Pada sel Volta, aliran elektron bergerak dari katode menuju anode"
                                ),
                                "0,1,2",
                                "KARED dan ANOKS berlaku di semua sel elektrokimia, dan biloks Mn pada KMnO₄ adalah +7 (+1 + Mn + 4(-2) = 0). Elektron selalu mengalir dari anode ke katode."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Terkait Hukum I Faraday pada elektrolisis larutan AgNO₃ (Ar Ag = 108, elektron valensi = 1), tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Massa perak (Ag) yang mengendap di katode berbanding lurus dengan muatan listrik (Q = i · t).",
                                    "Muatan listrik sebesar 1 Faraday (96.500 C) akan mengendapkan tepat 1 mol (108 gram) Ag.",
                                    "Di katode terjadi pelepasan elektron oleh ion Ag⁺ menjadi Ag²⁺."
                                ),
                                "B,B,S",
                                "1) Hukum I Faraday: w ∝ Q (Benar).\n2) w = e · F = (108/1) × 1 = 108 g (Benar).\n3) Di katode ion Ag⁺ menangkap 1 elektron menjadi logam Ag(s) (reduksi) (Salah)."
                            )
                        }
                        else -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Senyawa karbon dengan rumus molekul C₃H₆O dapat bereaksi positif dengan pereaksi Fehling dan Tollens (cermin perak). Nama IUPAC senyawa tersebut adalah...",
                                listOf("Propanal (Aldehida)", "Propanon (Keton)", "Asam propanoat", "1-Propanol", "Metil etil eter"),
                                "0",
                                "Senyawa dengan rumus umum CₙH₂ₙO yang memberikan uji positif terhadap pereaksi Fehling (endapan merah bata) dan Tollens (cermin perak) adalah golongan aldehida (alkanal), yaitu Propanal."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Manakah pasangan gugus fungsi, golongan senyawa organik, dan sifatnya yang BENAR (#$qIdx)?",
                                listOf(
                                    "Alkohol (-OH) dan eter (-O-) dengan jumlah atom C sama merupakan pasangan isomer fungsi (CₙH₂ₙ₊₂O)",
                                    "Reaksi antara asam karboksilat dan alkohol dengan katalis asam menghasilkan senyawa ester (esterifikasi)",
                                    "Protein merupakan polimer alam yang tersusun atas monomer asam amino melalui ikatan peptida",
                                    "Keton (alkanon) mudah dioksidasi oleh pereaksi Fehling menghasilkan asam karboksilat"
                                ),
                                "0,1,2",
                                "Isomer fungsi alkohol-eter, reaksi esterifikasi Fischer, dan ikatan peptida pada protein adalah benar. Keton tidak bereaksi dengan pereaksi Fehling/Tollens."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "kimia", unitId, sub, diff, qType,
                                "Perhatikan reaksi adisi propena (CH₃-CH=CH₂) dengan HBr menurut aturan Markovnikov. Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Reaksi adisi mengubah ikatan rangkap dua (alkena) menjadi ikatan tunggal (haloalkana).",
                                    "Menurut aturan Markovnikov, atom H terikat pada atom C ikatan rangkap yang memiliki lebih banyak atom H.",
                                    "Produk utama reaksi tersebut adalah 1-bromopropana."
                                ),
                                "B,B,S",
                                "1) Adisi memutus ikatan π menjadi ikatan jenuh (Benar).\n2) Aturan Markovnikov: 'yang kaya makin kaya H' (Benar).\n3) Produk utamanya adalah 2-bromopropana, bukan 1-bromopropana (Salah)."
                            )
                        }
                    }
                )
            }
        }
        return list
    }

    /**
     * Generates 108 authentic TKA Biologi & Biosains questions across 6 units.
     */
    fun getBiosainsQuestions(): List<TkaQuestion> {
        val list = mutableListOf<TkaQuestion>()
        val units = listOf(
            Triple("biosains_u1", "Biologi Sel & Metabolisme", "Biologi Sel & Metabolisme"),
            Triple("biosains_u2", "Genetika & Molekuler", "Genetika & Molekuler"),
            Triple("biosains_u3", "Bioteknologi & Rekayasa Genetika", "Bioteknologi & Rekayasa Genetika"),
            Triple("biosains_u4", "Fisiologi & Imunologi", "Fisiologi & Imunologi"),
            Triple("biosains_u5", "Botani & Fotosintesis", "Botani & Fotosintesis"),
            Triple("biosains_u6", "Ekologi & Evolusi", "Ekologi & Evolusi")
        )

        units.forEachIndexed { uIdx, (unitId, sub1, sub2) ->
            for (qIdx in 1..18) {
                val sub = if (qIdx <= 9) sub1 else sub2
                val diff = DifficultyTier.fromLevel(((qIdx - 1) % 3) + 1)
                val qType = when (qIdx % 3) {
                    1 -> QuestionType.SINGLE_CHOICE
                    2 -> QuestionType.MCMA
                    else -> QuestionType.TRUE_FALSE_MATRIX
                }
                val id = "bio_u${uIdx + 1}_$qIdx"
                list.add(
                    when (uIdx + 1) {
                        1 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Pada respirasi aerob sel eukariotik (Kasus Biosains #$qIdx), tahapan yang berlangsung di matriks mitokondria dan menghasilkan NADH, FADH₂, serta ATP melalui oksidasi asetil-KoA adalah...",
                                listOf(
                                    "Siklus Krebs (Siklus Asam Sitrat)",
                                    "Glikolisis di sitosol",
                                    "Reaksi terang di membran tilakoid",
                                    "Fermentasi asam laktat",
                                    "Fiksasi karbon siklus Calvin"
                                ),
                                "0",
                                "Siklus Krebs berlangsung di matriks mitokondria, mengoksidasi asetil-KoA menjadi CO₂ sembari mereduksi NAD⁺ menjadi NADH dan FAD menjadi FADH₂ serta menghasilkan GTP/ATP."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Manakah pernyataan yang BENAR mengenai biologi sel, transpor membran, dan biokatalisator enzim (#$qIdx)?",
                                listOf(
                                    "Enzim menurunkan energi aktivasi reaksi metabolisme tanpa ikut habis bereaksi",
                                    "Inhibitor kompetitif bersaing dengan substrat untuk berikatan pada sisi aktif (active site) enzim",
                                    "Sel tumbuhan yang dimasukkan ke larutan hipertonik akan mengalami plasmolisis (lepasnya membran plasma dari dinding sel)",
                                    "Ribosom hanya ditemukan pada sel eukariotik dan tidak ada pada sel prokariotik"
                                ),
                                "0,1,2",
                                "Mekanisme enzim, inhibitor kompetitif, dan plasmolisis sel tumbuhan benar. Ribosom dimiliki oleh semua sel hidup (baik prokariotik 70S maupun eukariotik 80S)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Perhatikan fungsi organel sel eukariotik berikut (#$qIdx). Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Lisosom mengandung enzim hidrolitik asam untuk pencernaan intraseluler dan autofagi.",
                                    "Retikulum Endoplasma Kasar ditempeli ribosom dan berperan dalam sintesis protein sekretori.",
                                    "Pompa ion Na⁺/K⁺ merupakan transpor pasif yang tidak membutuhkan ATP."
                                ),
                                "B,B,S",
                                "1) Lisosom berperan dalam autofagi/pencernaan intrasel (Benar).\n2) RE Kasar mensintesis protein (Benar).\n3) Pompa Na⁺/K⁺ melawan gradien konsentrasi sehingga merupakan transpor aktif yang wajib menghidrolisis ATP (Salah)."
                            )
                        }
                        2 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Untai cetakan (template/antisense) DNA memiliki urutan basa: 3'-TAC GGC ATG-5'. Urutan kodon mRNA hasil transkripsi dari untai tersebut adalah...",
                                listOf(
                                    "5'-AUG CCG UAC-3'",
                                    "5'-ATG CCG TAC-3'",
                                    "5'-UAC GGC AUG-3'",
                                    "5'-CAU GCC GUA-3'",
                                    "5'-TUG CCG UAC-3'"
                                ),
                                "0",
                                "Pada transkripsi RNA polimerase membaca template 3'→5' dan mensintesis mRNA komplementer 5'→3' (A berpasangan dengan U pada RNA, T dengan A, G dengan C): 3'-TAC GGC ATG-5' → 5'-AUG CCG UAC-3'."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Manakah konsep Genetika Mendel dan Biologi Molekuler (#$qIdx) berikut yang BENAR?",
                                listOf(
                                    "Menurut aturan Chargaff pada DNA untai ganda, persentase Adenin (A) sama dengan Timin (T), dan Guanin (G) sama dengan Sitosin (C)",
                                    "Persilangan dihibrid heterozigot (AaBb × AaBb) dengan dominansi penuh menghasilkan rasio fenotipe F₂ sebesar 9 : 3 : 3 : 1",
                                    "Proses penyambungan ekson dan pemotongan intron (RNA splicing) terjadi pada pemrosesan pra-mRNA eukariotik",
                                    "Translasi mRNA menjadi polipeptida berlangsung di dalam nukleolus"
                                ),
                                "0,1,2",
                                "Aturan Chargaff, rasio Mendel 9:3:3:1, dan splicing ekson-intron benar. Translasi berlangsung di ribosom sitoplasma/RE kasar, bukan di nukleolus."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Terkait pembelahan sel mitosis dan meiosis (#$qIdx), tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Pindah silang (crossing over) kromosom homolog terjadi pada tahap Profase I meiosis dan meningkatkan variasi genetik.",
                                    "Mitosis menghasilkan 2 sel anakan yang identik secara genetik (diploid 2n → 2n).",
                                    "Replikasi DNA terjadi pada tahap Metafase."
                                ),
                                "B,B,S",
                                "1) Crossing over terjadi pada pakiten Profase I meiosis (Benar).\n2) Mitosis mempertahankan ploidi 2n (Benar).\n3) Replikasi DNA terjadi pada fase S (Sintesis) saat Interfase, bukan saat Metafase (Salah)."
                            )
                        }
                        3 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Dalam bioteknologi molekuler modern (#$qIdx), enzim yang berfungsi memotong molekul DNA pada urutan palindromik spesifik dan enzim yang menyambungkan fragmen DNA ke plasmid vektor berturut-turut adalah...",
                                listOf(
                                    "Enzim restriksi endonuklease dan enzim DNA ligase",
                                    "Enzim helikase dan enzim RNA polimerase",
                                    "Enzim Taq polimerase dan enzim amilase",
                                    "Enzim reverse transcriptase dan enzim katalase",
                                    "Enzim primase dan enzim pepsin"
                                ),
                                "0",
                                "Restriksi endonuklease bertindak sebagai 'gunting molekuler' pada situs palindrom, sedangkan DNA ligase bertindak sebagai 'lem molekuler' pembentuk ikatan fosfodiester."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Manakah pernyataan tentang teknik Biosains Modern (PCR, CRISPR-Cas9, dan Hibridoma — Kasus #$qIdx) yang BENAR?",
                                listOf(
                                    "Siklus PCR terdiri atas tiga tahap suhu berulang: Denaturasi (~95°C), Annealing/penempelan primer (~55°C), dan Ekstensi dengan Taq polimerase (~72°C)",
                                    "Sistem CRISPR-Cas9 menggunakan guide RNA (gRNA) untuk mengarahkan endonuklease Cas9 memotong lokus target genom secara presisi",
                                    "Teknik fusi sel hibridoma (limfosit B + sel mieloma) digunakan untuk memproduksi antibodi monoklonal",
                                    "Produksi insulin manusia rekombinan menggunakan plasmid bakteri E. coli termasuk bioteknologi konvensional tanpa rekayasa genetika"
                                ),
                                "0,1,2",
                                "Tahapan PCR, mekanisme CRISPR-Cas9, dan antibodi monoklonal hibridoma adalah benar. Insulin rekombinan merupakan bioteknologi modern berbasis rekayasa genetika."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Evaluasilah aplikasi bioteknologi berikut (#$qIdx). Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Tanaman kapas Bt transgenik menyisipkan gen cry dari Bacillus thuringiensis sehingga tahan terhadap hama larva serangga.",
                                    "Elektroforesis gel agarosa memisahkan fragmen DNA berdasarkan ukuran panjang pasang basa yang bergerak menuju kutub positif (anode).",
                                    "Molekul DNA bermuatan positif sehingga bergerak menuju kutub negatif pada elektroforesis."
                                ),
                                "B,B,S",
                                "1) Kapas Bt mengandung gen endotoksin cry (Benar).\n2) Fragmen lebih pendek bergerak lebih cepat ke kutub positif (Benar).\n3) Gugus fosfat membuat tulang punggung DNA bermuatan negatif, bukan positif (Salah)."
                            )
                        }
                        4 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Pada nefron ginjal manusia (#$qIdx), proses reabsorpsi (penyerapan kembali) glukosa dan asam amino secara keseluruhan (100% pada kondisi normal) berlangsung di bagian...",
                                listOf(
                                    "Tubulus kontortus proksimal (TKP)",
                                    "Glomerulus dan kapsula Bowman",
                                    "Tubulus kontortus distal (TKD)",
                                    "Duktus kolektivus",
                                    "Ureter dan vesika urinaria"
                                ),
                                "0",
                                "Filtrasi terjadi di glomerulus menghasilkan urine primer, kemudian glukosa dan asam amino direabsorpsi secara transpor aktif di Tubulus Kontortus Proksimal (TKP)."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Manakah pernyataan Fisiologi dan Imunologi Manusia (#$qIdx) berikut yang BENAR?",
                                listOf(
                                    "Hormon insulin yang disekresikan sel beta pankreas menurunkan kadar glukosa darah dengan mengubah glukosa menjadi glikogen",
                                    "Limfosit B berdiferensiasi menjadi sel plasma yang menghasilkan antibodi spesifik (imunitas humoral)",
                                    "Pertukaran gas O₂ dan CO₂ secara difusi sederhana berlangsung di alveolus paru-paru karena perbedaan tekanan parsial",
                                    "Vena pulmonalis membawa darah yang miskin oksigen (kaya CO₂) dari jantung ke paru-paru"
                                ),
                                "0,1,2",
                                "Fungsi insulin, imunitas humoral limfosit B, dan difusi alveolus benar. Vena pulmonalis justru membawa darah kaya oksigen dari paru-paru menuju atrium kiri jantung."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Terkait penghantaran impuls saraf dan mekanisme vaksinasi (#$qIdx), tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Selubung mielin mempercepat konduksi impuls saraf melalui mekanisme loncatan (saltatory conduction) pada nodus Ranvier.",
                                    "Vaksinasi memicu pembentukan sel memori B dan T sehingga menghasilkan kekebalan aktif buatan.",
                                    "Sinapsis menghantarkan sinyal antarsaraf tanpa melibatkan senyawa kimia neurotransmiter."
                                ),
                                "B,B,S",
                                "1) Konduksi saltatorik terjadi di nodus Ranvier (Benar).\n2) Vaksinasi adalah kekebalan aktif buatan (Benar).\n3) Sinapsis kimiawi melepaskan neurotransmiter (seperti asetilkolin) melintasi celah sinapsis (Salah)."
                            )
                        }
                        5 -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Pada reaksi terang fotosintesis di membran tilakoid kloroplas (#$qIdx), gas oksigen (O₂) yang dilepaskan ke atmosfer berasal dari proses...",
                                listOf(
                                    "Fotolisis (pemecahan) molekul air (H₂O) pada Fotosistem II",
                                    "Pemecahan molekul CO₂ pada Siklus Calvin di stroma",
                                    "Oksidasi glukosa di mitokondria daun",
                                    "Reduksi NADP⁺ pada Fotosistem I",
                                    "Penguapan transpirasi melalui stomata"
                                ),
                                "0",
                                "Percobaan isotop O-18 (Ruben & Kamen) membuktikan bahwa O₂ hasil fotosintesis berasal dari fotolisis air (2H₂O → 4H⁺ + 4e⁻ + O₂) di Fotosistem II."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Manakah pernyataan mengenai fisiologi tumbuhan dan adaptasi fotosintesis C3, C4, serta CAM (#$qIdx) yang BENAR?",
                                listOf(
                                    "Siklus Calvin (reaksi gelap) berlangsung di stroma kloroplas dengan menggunakan ATP dan NADPH dari reaksi terang",
                                    "Tumbuhan C4 (seperti jagung dan tebu) memisahkan fiksasi CO₂ awal di sel mesofil dan siklus Calvin di sel seludang pembuluh untuk menekan fotorespirasi",
                                    "Hormon auksin berperan dalam pemanjangan sel pucuk dan menyebabkan gerak fototropisme karena terakumulasi di sisi yang teduh",
                                    "Jaringan floem berfungsi mengangkut air dan mineral terlarut dari akar menuju daun"
                                ),
                                "0,1,2",
                                "Siklus Calvin di stroma, adaptasi C4 anatomi Kranz, dan peran auksin benar. Pengangkutan air dari akar dilakukan oleh xilem, sedangkan floem mengangkut hasil fotosintesis."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Terkait jaringan dan hormon tumbuhan (#$qIdx), tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Gas etilen memicu pematangan buah klimakterik dan pengguguran daun (absisi).",
                                    "Asam absisat (ABA) membantu tumbuhan bertahan saat kekeringan dengan memicu penutupan stomata.",
                                    "Tumbuhan CAM (seperti kaktus dan nanas) membuka stomatanya lebar-lebar hanya pada siang hari terik."
                                ),
                                "B,B,S",
                                "1) Etilen memicu pematangan buah (Benar).\n2) ABA memicu penutupan stomata saat cekaman kekeringan (Benar).\n3) Tumbuhan CAM justru membuka stomata pada malam hari untuk menghemat air (Salah)."
                            )
                        }
                        else -> when (qType) {
                            QuestionType.SINGLE_CHOICE -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Dalam suatu populasi yang berada pada kesetimbangan Hardy-Weinberg (#$qIdx), frekuensi individu bergenotipe resesif homozigot (aa) adalah 16% (q² = 0,16). Persentase individu heterozigot (2pq) dalam populasi tersebut adalah...",
                                listOf("48%", "36%", "60%", "24%", "84%"),
                                "0",
                                "q² = 0,16 ⇒ frekuensi alel resesif q = √0,16 = 0,4.\nKarena p + q = 1, maka frekuensi alel dominan p = 1 - 0,4 = 0,6.\nFrekuensi heterozigot 2pq = 2 × 0,6 × 0,4 = 0,48 = 48%."
                            )
                            QuestionType.MCMA -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Manakah pernyataan Ekologi, Daur Biogeokimia, dan Evolusi (#$qIdx) berikut yang BENAR?",
                                listOf(
                                    "Pada daur nitrogen, bakteri Nitrosomonas dan Nitrobacter mengubah amonia menjadi nitrit dan nitrat (nitrifikasi)",
                                    "Perpindahan energi antar tingkat trofik pada piramida ekologi mengikuti aturan ~10% karena sebagian besar energi dilepas sebagai panas respirasi",
                                    "Sayap kelelawar dan sirip paus merupakan organ homolog (struktur dasar sama dari leluhur mamalia yang sama)",
                                    "Eutrofikasi perairan akibat limbah pupuk fosfat/nitrat meningkatkan kadar oksigen terlarut di dasar danau"
                                ),
                                "0,1,2",
                                "Nitrifikasi, efisiensi trofik 10%, dan homologi mamalia benar. Eutrofikasi memicu ledakan alga (algal bloom) yang saat mati dan diuraikan bakteri aerob justru menguras oksigen terlarut (hipoksia)."
                            )
                            QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                                id, "biosains", unitId, sub, diff, qType,
                                "Evaluasilah konsep kesetimbangan Hardy-Weinberg dan suksesi ekologi (#$qIdx). Tentukan Benar (B) atau Salah (S)!",
                                listOf(
                                    "Syarat berlakunya kesetimbangan Hardy-Weinberg antara lain ukuran populasi besar, perkawinan acak, serta tidak ada mutasi, migrasi, dan seleksi alam.",
                                    "Lichen (lumut kerak) berperan sebagai organisme pionir pada suksesi primer di batuan vulkanik baru.",
                                    "Akumulasi polutan pestisida DDT paling rendah ditemukan pada konsumen puncak (biomagnifikasi)."
                                ),
                                "B,B,S",
                                "1) Lima syarat Hardy-Weinberg tepat (Benar).\n2) Lichen adalah organisme pionir suksesi primer (Benar).\n3) Dalam biomagnifikasi, konsentrasi polutan persisten justru paling tinggi pada konsumen puncak (Salah)."
                            )
                        }
                    }
                )
            }
        }
        return list
    }
}
