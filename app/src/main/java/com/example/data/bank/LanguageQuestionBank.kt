package com.example.data.bank

import com.example.data.model.DifficultyTier
import com.example.data.model.QuestionType
import com.example.data.model.TkaQuestion

object LanguageQuestionBank {

    private val bindoTopics = listOf(
        "Transisi Energi Terbarukan di Kepulauan Nusantara",
        "Ketahanan Pangan Berbasis Pangan Lokal Sorgum dan Sagu",
        "Literasi Finansial Digital di Kalangan Generasi Muda",
        "Konservasi Ekosistem Mangrove Pesisir sebagai Penyerap Karbon Biru",
        "Etika Kecerdasan Buatan dalam Dunia Pendidikan Tinggi",
        "Revitalisasi Transportasi Publik Perkotaan Rendah Emisi",
        "Penguatan UMKM Kreatif Melalui Ekosistem Niaga Elektronik",
        "Adaptasi Pertanian Cerdas Iklim terhadap Fenomena El Nino",
        "Pelestarian Bahasa Daerah dari Ancaman Kepunahan Leksikal"
    )

    fun getBindoQuestions(): List<TkaQuestion> {
        val list = mutableListOf<TkaQuestion>()
        val units = listOf(
            "bindo_u1" to "Ide Pokok & Simpulan",
            "bindo_u2" to "Kalimat Efektif & EYD",
            "bindo_u3" to "Penalaran Wacana",
            "bindo_u4" to "Makna Kata & Konjungsi",
            "bindo_u5" to "Evaluasi Argumen",
            "bindo_u6" to "Fakta vs Opini"
        )

        units.forEachIndexed { uIdx, (unitId, subtopic) ->
            for (qIdx in 1..18) {
                val topic = bindoTopics[(uIdx * 3 + qIdx) % bindoTopics.size]
                val diff = DifficultyTier.fromLevel(((qIdx - 1) % 3) + 1)
                val qType = when (qIdx % 3) {
                    1 -> QuestionType.SINGLE_CHOICE
                    2 -> QuestionType.MCMA
                    else -> QuestionType.TRUE_FALSE_MATRIX
                }
                val id = "bi_u${uIdx + 1}_$qIdx"
                list.add(buildBindoQuestion(id, unitId, subtopic, topic, uIdx + 1, qIdx, diff, qType))
            }
        }
        return list
    }

    private fun buildBindoQuestion(
        id: String,
        unitId: String,
        subtopic: String,
        topic: String,
        unitNum: Int,
        qIdx: Int,
        diff: DifficultyTier,
        qType: QuestionType
    ): TkaQuestion {
        return when (unitNum) {
            1 -> when (qType) {
                QuestionType.SINGLE_CHOICE -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Bacalah kutipan wacana berikut (Paket #$qIdx):\n\"Percepatan program $topic memerlukan sinergi kebijakan lintas kementerian, kesiapan infrastruktur daerah, serta partisipasi aktif masyarakat lokal agar dampaknya berkelanjutan.\"\nIde pokok paragraf tersebut adalah...",
                    listOf(
                        "Tiga pilar utama pendukung keberhasilan program $topic",
                        "Hambatan anggaran dalam pelaksanaan $topic di daerah terpencil",
                        "Sejarah perkembangan regulasi $topic sejak dekade lalu",
                        "Perbandingan efektivitas $topic dengan negara tetangga",
                        "Penolakan masyarakat terhadap perubahan sistem $topic"
                    ),
                    "0",
                    "Kalimat utama terletak di awal (deduktif) yang menekankan tiga syarat/pilar utama (sinergi kebijakan, infrastruktur, partisipasi masyarakat) bagi keberhasilan $topic."
                )
                QuestionType.MCMA -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Terkait teks bertema \"$topic\" (Wacana #$qIdx), manakah pernyataan yang memenuhi syarat SIMPULAN yang sahih? (Pilih semua yang benar)",
                    listOf(
                        "Simpulan merangkum hubungan logis antargagasan utama dalam teks",
                        "Simpulan tidak menambahkan klaim spekulatif di luar informasi teks",
                        "Keberhasilan $topic bergantung pada kolaborasi kebijakan dan masyarakat",
                        "Simpulan hanya menyalin satu kalimat penjelas secara acak"
                    ),
                    "0,1,2",
                    "Simpulan TKA yang valid harus mencerminkan sintesis gagasan teks secara proporsional tanpa melompat di luar premis atau sekadar menyalin kalimat penjelas."
                )
                QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Evaluasilah struktur gagasan pada teks \"$topic\" (#$qIdx). Tentukan Benar (B) atau Salah (S)!",
                    listOf(
                        "Gagasan utama menjadi payung bagi seluruh kalimat penjelas dalam paragraf.",
                        "Kalimat penjelas yang tidak relevan akan merusak kepaduan (koherensi) paragraf.",
                        "Sebuah paragraf ilmiah yang baik wajib memiliki minimal empat ide pokok berbeda."
                    ),
                    "B,B,S",
                    "1) Gagasan utama menaungi kalimat penjelas (Benar).\n2) Kalimat sumbang merusak koherensi (Benar).\n3) Satu paragraf hanya memiliki satu ide pokok agar fokus (Salah)."
                )
            }
            2 -> when (qType) {
                QuestionType.SINGLE_CHOICE -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Manakah perbaikan kalimat efektif dan baku sesuai EYD V untuk konteks kajian \"$topic\" (#$qIdx) berikut ini?",
                    listOf(
                        "Pemerintah mengoptimalkan analisis risiko guna mempercepat inovasi.",
                        "Para peneliti-peneliti saling bantu-membantu di dalam merumuskan analisa.",
                        "Bagi seluruh peserta rapat diharapkan agar supaya hadir tepat pada waktunya.",
                        "Di wilayah itu merupakan kawasan percontohan daripada proyek nasional.",
                        "Mereka mempraktikan sistim baru yang telah dirubah kemarin."
                    ),
                    "0",
                    "Opsi pertama efektif: memiliki subjek ('Pemerintah') dan predikat ('mengoptimalkan') yang jelas serta menggunakan kata baku ('analisis', 'risiko', 'inovasi'). Opsi lain mengandung pleonasme, kehilangan subjek akibat preposisi di awal, atau kata tidak baku."
                )
                QuestionType.MCMA -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Manakah penulisan kata baku bahasa Indonesia (KBBI & EYD V) yang TEPAT pada laporan \"$topic\" (#$qIdx)? (Pilih semua yang benar)",
                    listOf(
                        "praktik, risiko, dan kualitas",
                        "analisis, sistem, dan hipotesis",
                        "produktivitas, efektivitas, dan kreativitas",
                        "aktifitas, apotik, dan nasehat"
                    ),
                    "0,1,2",
                    "Bentuk baku menurut KBBI adalah praktik, risiko, kualitas, analisis, sistem, hipotesis, produktivitas, efektivitas, kreativitas, aktivitas, apotek, dan nasihat."
                )
                QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Perhatikan kaidah kalimat efektif pada naskah ilmiah \"$topic\" (#$qIdx). Tentukan Benar (B) atau Salah (S)!",
                    listOf(
                        "Kalimat efektif wajib memiliki minimal unsur Subjek (S) dan Predikat (P).",
                        "Penggunaan kata depan 'Bagi' atau 'Untuk' di awal subjek dapat membuat kalimat menjadi nirsubjek.",
                        "Gabungan kata 'agar supaya' dan 'adalah merupakan' dianjurkan untuk mempertegas kalimat ilmiah."
                    ),
                    "B,B,S",
                    "1) S dan P wajib hadir dalam kalimat efektif (Benar).\n2) Preposisi di depan subjek mengaburkan fungsi subjek (Benar).\n3) 'Agar supaya' dan 'adalah merupakan' adalah pleonasme/pemborosan kata (Salah)."
                )
            }
            3 -> when (qType) {
                QuestionType.SINGLE_CHOICE -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Wacana #$qIdx: \"Apabila insentif riset pada bidang $topic ditingkatkan secara konsisten, maka produktivitas paten nasional meningkat hingga 40%.\" Jika produktivitas paten nasional TIDAK meningkat, simpulan logisnya adalah...",
                    listOf(
                        "Insentif riset pada bidang tersebut tidak ditingkatkan secara konsisten",
                        "Peneliti telah menghasilkan terlalu banyak publikasi internasional",
                        "Anggaran pendidikan dasar telah dialihkan seluruhnya",
                        "Masyarakat tidak lagi membutuhkan inovasi teknologi",
                        "Paten nasional otomatis meningkat pada tahun berikutnya"
                    ),
                    "0",
                    "Menggunakan modus tollens (P ⇒ Q; ~Q ∴ ~P): Jika hasil (Q) tidak terjadi, maka syarat cukup (P) tidak terpenuhi."
                )
                QuestionType.MCMA -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Dalam menganalisis penalaran wacana \"$topic\" (#$qIdx), manakah indikator koherensi yang BENAR?",
                    listOf(
                        "Keterkaitan logis sebab-akibat antarpremis dalam teks",
                        "Penggunaan kata rujukan (ini, tersebut, hal itu) yang mengacu secara jelas",
                        "Konsistensi topik dari awal hingga akhir paragraf",
                        "Lompatan argumen tanpa bukti pendukung (non sequitur)"
                    ),
                    "0,1,2",
                    "Koherensi wacana dibangun dari hubungan logis, kejelasan pronomina/rujukan, dan kesatuan topik; bukan dari lompatan argumen."
                )
                QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Terkait hubungan kausalitas pada teks \"$topic\" (#$qIdx), tentukan Benar (B) atau Salah (S)!",
                    listOf(
                        "Korelasi dua fenomena yang terjadi bersamaan belum tentu membuktikan hubungan sebab-akibat langsung.",
                        "Bukti empiris memperkuat validitas simpulan induktif.",
                        "Generalisasi tergesa-gesa dari satu sampel tunggal menghasilkan simpulan yang pasti akurat."
                    ),
                    "B,B,S",
                    "1) Korelasi ≠ kausalitas otomatis (Benar).\n2) Data empiris memperkuat induksi (Benar).\n3) Generalisasi dari satu sampel adalah kekeliruan berpikir (hasty generalization) (Salah)."
                )
            }
            4 -> when (qType) {
                QuestionType.SINGLE_CHOICE -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Kalimat 1: \"Implementasi $topic menghadapi kendala keterbatasan tenaga ahli.\"\nKalimat 2: \"... pemerintah meluncurkan program beasiswa vokasi terpadu.\"\nKonjungsi antarkalimat yang paling tepat untuk mengisi bagian rumpang adalah...",
                    listOf(
                        "Oleh karena itu,",
                        "Sebaliknya,",
                        "Meskipun demikian,",
                        "Bahkan,",
                        "Kecuali itu,"
                    ),
                    "0",
                    "Kalimat kedua menyatakan solusi/akibat logis atas kendala pada kalimat pertama, sehingga konjungsi kausalitas antarkalimat 'Oleh karena itu,' paling tepat."
                )
                QuestionType.MCMA -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Manakah pasangan konjungsi antarkalimat dan fungsinya yang BENAR dalam wacana \"$topic\" (#$qIdx)?",
                    listOf(
                        "'Oleh sebab itu,' menyatakan akibat dari kalimat sebelumnya",
                        "'Namun,' menyatakan pertentangan dengan pernyataan sebelumnya",
                        "'Selain itu,' menyatakan penambahan informasi",
                        "'Sedangkan,' digunakan di awal kalimat baru setelah tanda titik"
                    ),
                    "0,1,2",
                    "'Oleh sebab itu,', 'Namun,', dan 'Selain itu,' adalah konjungsi antarkalimat, sedangkan 'sedangkan' adalah konjungsi intrakalimat dan tidak boleh mengawali kalimat."
                )
                QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Analisis penggunaan makna kata dan konjungsi pada teks \"$topic\" (#$qIdx). Tentukan Benar (B) atau Salah (S)!",
                    listOf(
                        "Makna kontekstual sebuah kata dipengaruhi oleh kalimat yang melingkupinya.",
                        "Konjungsi antarkalimat di awal kalimat wajib diikuti tanda koma (,).",
                        "Kata 'tetapi' dan 'melainkan' baku diletakkan di awal kalimat setelah titik."
                    ),
                    "B,B,S",
                    "1) Makna kontekstual bergantung pada konteks kalimat (Benar).\n2) Konjungsi antarkalimat diikuti tanda koma (Benar).\n3) 'Tetapi' dan 'melainkan' adalah konjungsi intrakalimat; untuk awal kalimat gunakan 'Namun,' atau 'Akan tetapi,' (Salah)."
                )
            }
            5 -> when (qType) {
                QuestionType.SINGLE_CHOICE -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Penulis berargumen bahwa program \"$topic\" (#$qIdx) mampu menurunkan biaya logistik daerah sebesar 25%. Manakah fakta baru yang paling MEMPERKUAT argumen penulis?",
                    listOf(
                        "Studi empiris di lima provinsi menunjukkan efisiensi biaya distribusi mencapai 27% setelah program berjalan",
                        "Sebagian warga belum memahami istilah teknis dalam sosialisasi program",
                        "Biaya logistik di negara Eropa memiliki struktur pajak yang berbeda",
                        "Program serupa pernah ditunda dua dekade silam karena keterbatasan teknologi",
                        "Desain logo program baru saja diperbarui oleh tim komunikasi"
                    ),
                    "0",
                    "Bukti empiris yang menunjukkan penurunan biaya distribusi nyata di lima provinsi secara langsung mengonfirmasi dan memperkuat klaim kausalitas penulis."
                )
                QuestionType.MCMA -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Manakah elemen yang harus ada agar teks argumentasi \"$topic\" (#$qIdx) memiliki daya persuasi ilmiah yang kuat?",
                    listOf(
                        "Pernyataan posisi (tesis) yang jelas dan terukur",
                        "Data statistik atau hasil penelitian yang relevan dan dapat diverifikasi",
                        "Penalaran logis yang menghubungkan bukti dengan klaim",
                        "Serangan pribadi (ad hominem) terhadap pihak yang berbeda pendapat"
                    ),
                    "0,1,2",
                    "Argumen ilmiah dibangun dari tesis, bukti terverifikasi, dan penalaran logis, serta bebas dari sesat pikir ad hominem."
                )
                QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Evaluasilah kekuatan argumen pada wacana \"$topic\" (#$qIdx). Tentukan Benar (B) atau Salah (S)!",
                    listOf(
                        "Bukti tandingan (counter-evidence) yang valid dapat memperlemah klaim mutlak penulis.",
                        "Asumsi dasar argumen adalah jembatan tak tertulis antara premis dan simpulan.",
                        "Argumen semakin kuat apabila hanya mengandalkan opini tanpa data."
                    ),
                    "B,B,S",
                    "1) Bukti tandingan memperlemah klaim mutlak (Benar).\n2) Asumsi menghubungkan premis dengan simpulan (Benar).\n3) Argumen tanpa data bersifat lemah dan subjektif (Salah)."
                )
            }
            else -> when (qType) {
                QuestionType.SINGLE_CHOICE -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Manakah di antara kalimat bertema \"$topic\" (#$qIdx) berikut yang merupakan FAKTA?",
                    listOf(
                        "Kementerian terkait menerbitkan regulasi pelaksanaan program pada tanggal 14 Maret 2025.",
                        "Program ini tampaknya akan menjadi kebijakan paling spektakuler sepanjang abad ini.",
                        "Menurut hemat saya, pelaksanaan program sebaiknya ditunda hingga tahun depan.",
                        "Masyarakat pasti merasa sangat puas apabila seluruh tahapan selesai lebih cepat.",
                        "Kebijakan tersebut dirasa kurang menarik bagi sebagian pengamat."
                    ),
                    "0",
                    "Kalimat pertama memuat peristiwa yang telah terjadi disertai penanda waktu spesifik yang dapat diverifikasi kebenarannya (fakta), sedangkan opsi lain memuat kata penanda opini ('tampaknya', 'sebaiknya', 'pasti', 'dirasa')."
                )
                QuestionType.MCMA -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Manakah ciri kalimat OPINI dalam artikel analisis \"$topic\" (#$qIdx)? (Pilih semua yang benar)",
                    listOf(
                        "Mengandung penilaian subjektif seperti 'sangat indah', 'kurang efektif', atau 'paling ideal'",
                        "Menggunakan modalitas prediksi atau saran seperti 'sebaiknya', 'diperkirakan', atau 'mungkin'",
                        "Kebenarannya masih memerlukan pembuktian lebih lanjut",
                        "Memuat data sensus resmi yang telah terverifikasi lembaga statistik"
                    ),
                    "0,1,2",
                    "Opini ditandai oleh penilaian subjektif, kata saran/prediksi, dan sifatnya yang belum terverifikasi sebagai kejadian faktual."
                )
                QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                    id, "bindo", unitId, subtopic, diff, qType,
                    "Perhatikan perbedaan fakta dan opini pada infografik \"$topic\" (#$qIdx). Tentukan Benar (B) atau Salah (S)!",
                    listOf(
                        "Data angka terukur dari hasil pengukuran resmi termasuk ke dalam fakta.",
                        "Saran kebijakan untuk masa depan termasuk ke dalam opini/rekomendasi.",
                        "Setiap kalimat yang mengutip pendapat seseorang otomatis mengubah isi pendapat tersebut menjadi fakta ilmiah mutlak."
                    ),
                    "B,B,S",
                    "1) Data pengukuran terverifikasi adalah fakta (Benar).\n2) Saran masa depan adalah opini/rekomendasi (Benar).\n3) Isi pendapat tetap berupa opini meskipun dikutip (Salah)."
                )
            }
        }
    }

    fun getBingWajibQuestions(): List<TkaQuestion> {
        val list = mutableListOf<TkaQuestion>()
        val themes = listOf(
            "Urban Vertical Farming and Food Security",
            "Renewable Microgrids in Archipelagic Communities",
            "Neuroplasticity and Adolescent Sleep Cycles",
            "Biodegradable Marine Polymers from Seaweed",
            "Digital Archiving of Endangered Indigenous Scripts",
            "Circular Economy in Electronics Manufacturing"
        )
        val units = listOf(
            "bing_wajib_u1" to "Main Idea & Purpose",
            "bing_wajib_u2" to "Inference & Implied Detail",
            "bing_wajib_u3" to "Vocabulary in Context",
            "bing_wajib_u4" to "Paraphrasing & Reference",
            "bing_wajib_u5" to "Tone & Attitude",
            "bing_wajib_u6" to "Text Coherence"
        )

        units.forEachIndexed { uIdx, (unitId, subtopic) ->
            for (qIdx in 1..18) {
                val theme = themes[(uIdx + qIdx) % themes.size]
                val diff = DifficultyTier.fromLevel(((qIdx - 1) % 3) + 1)
                val qType = when (qIdx % 3) {
                    1 -> QuestionType.SINGLE_CHOICE
                    2 -> QuestionType.MCMA
                    else -> QuestionType.TRUE_FALSE_MATRIX
                }
                val id = "en_w_u${uIdx + 1}_$qIdx"
                list.add(
                    when (qType) {
                        QuestionType.SINGLE_CHOICE -> TkaQuestion(
                            id, "bing_wajib", unitId, subtopic, diff, qType,
                            "Passage Excerpt (#$qIdx — $theme):\n\"Recent longitudinal studies on $theme demonstrate that integrating localized technological frameworks with community-led governance yields a ${(qIdx % 5 + 2) * 10}% improvement in long-term sustainability compared to top-down interventions.\"\nWhat is the primary purpose of this excerpt?",
                            listOf(
                                "To highlight empirical evidence supporting community-integrated approaches in $theme",
                                "To argue that all technological frameworks in $theme should be abandoned immediately",
                                "To narrate a personal biography of an early pioneer in $theme",
                                "To criticize longitudinal research methodologies in general",
                                "To prove that community governance reduces efficiency to zero"
                            ),
                            "0",
                            "The excerpt presents empirical findings ('longitudinal studies... demonstrate') showing superior sustainability when localized technology is paired with community governance."
                        )
                        QuestionType.MCMA -> TkaQuestion(
                            id, "bing_wajib", unitId, subtopic, diff, qType,
                            "Based on standard academic reading principles for passage #$qIdx on \"$theme\" ($subtopic), which of the following statements are CORRECT? (Select all that apply)",
                            listOf(
                                "Combining localized technology with community governance improves sustainability outcomes",
                                "Top-down interventions performed worse than community-integrated models in the cited study",
                                "The word 'yields' in the excerpt is closest in meaning to 'produces' or 'generates'",
                                "The passage claims that technology has no role whatsoever in $theme"
                            ),
                            "0,1,2",
                            "Options 1, 2, and 3 are directly supported by the text and academic lexicon ('yields' = produces), whereas option 4 contradicts the text."
                        )
                        QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                            id, "bing_wajib", unitId, subtopic, diff, qType,
                            "Evaluate the following reading comprehension statements regarding Passage #$qIdx ($theme — $subtopic). Mark Benar (B) or Salah (S)!",
                            listOf(
                                "The author adopts an objective, analytical tone supported by quantitative data.",
                                "It can be inferred that community participation plays a meaningful role in project longevity.",
                                "The passage states that top-down interventions are always 100% superior."
                            ),
                            "B,B,S",
                            "1) The tone is analytical and evidence-based (Benar).\n2) Community-led governance directly correlates with higher sustainability (Benar).\n3) The passage states the opposite—community-integrated models outperform top-down interventions (Salah)."
                        )
                    }
                )
            }
        }
        return list
    }

    fun getBingLanjutQuestions(): List<TkaQuestion> {
        val list = mutableListOf<TkaQuestion>()
        val topics = listOf(
            "Algorithmic Epistemology and Cognitive Offloading",
            "Macroeconomic Trade-offs of Carbon Border Adjustments",
            "CRISPR Epigenome Editing and Bioethical Governance",
            "Post-Keynesian Monetary Transmission in Emerging Markets",
            "Sociolinguistic Code-Switching in Multilingual Digital Spaces",
            "Quantum Cryptography and Post-Silicon Information Security"
        )
        val units = listOf(
            "bing_lanjut_u1" to "Critical Fallacies",
            "bing_lanjut_u2" to "Comparative Synthesis",
            "bing_lanjut_u3" to "Advanced Grammar & Inversion",
            "bing_lanjut_u4" to "Rhetorical Strategy",
            "bing_lanjut_u5" to "Academic Data Synthesis",
            "bing_lanjut_u6" to "Academic Lexicon"
        )

        units.forEachIndexed { uIdx, (unitId, subtopic) ->
            for (qIdx in 1..18) {
                val topic = topics[(uIdx + qIdx) % topics.size]
                val diff = DifficultyTier.fromLevel(((qIdx - 1) % 3) + 1)
                val qType = when (qIdx % 3) {
                    1 -> QuestionType.SINGLE_CHOICE
                    2 -> QuestionType.MCMA
                    else -> QuestionType.TRUE_FALSE_MATRIX
                }
                val id = "en_l_u${uIdx + 1}_$qIdx"
                list.add(
                    when (qType) {
                        QuestionType.SINGLE_CHOICE -> TkaQuestion(
                            id, "bing_lanjut", unitId, subtopic, diff, qType,
                            "Advanced Discourse Item #$qIdx ($topic):\n\"Not only _____ the conventional paradigm of $topic, but the empirical findings also necessitated a fundamental recalibration of regulatory thresholds.\"\nWhich syntactic structure correctly completes the inverted academic sentence?",
                            listOf(
                                "did the symposium challenge",
                                "the symposium challenged",
                                "challenged the symposium",
                                "was the symposium challenge",
                                "has the symposium challenging"
                            ),
                            "0",
                            "When a negative/restrictive adverbial phrase ('Not only') is fronted at the beginning of a clause, subject-auxiliary inversion is mandatory in formal English: 'Not only did the symposium challenge...'."
                        )
                        QuestionType.MCMA -> TkaQuestion(
                            id, "bing_lanjut", unitId, subtopic, diff, qType,
                            "In evaluating critical reasoning and rhetorical strategy in Paper #$qIdx on \"$topic\" ($subtopic), which analytical propositions are VALID? (Select all that apply)",
                            listOf(
                                "A concession acknowledges a valid counterpoint before reinforcing the author's primary thesis",
                                "Confusing correlation with causation is a logical flaw that weakens empirical claims",
                                "Synthesizing paired passages requires identifying both points of convergence and methodological divergence",
                                "An ad hominem fallacy strengthens the statistical validity of a scientific experiment"
                            ),
                            "0,1,2",
                            "Concessions, causal rigor, and comparative synthesis are core principles of C1/C2 academic reading, whereas ad hominem attacks are logical fallacies."
                        )
                        QuestionType.TRUE_FALSE_MATRIX -> TkaQuestion(
                            id, "bing_lanjut", unitId, subtopic, diff, qType,
                            "Assess the linguistic and logical validity of the following statements for Advanced English Item #$qIdx ($topic). Mark Benar (B) or Salah (S)!",
                            listOf(
                                "In the sentence 'Had the protocol been enforced, the anomaly would have been averted', the third conditional inversion expresses an unfulfilled past condition.",
                                "The academic verb 'corroborate' means to confirm or give support to a statement or finding.",
                                "The word 'mitigate' is synonymous with 'aggravate' and 'intensify'."
                            ),
                            "B,B,S",
                            "1) 'Had + S + V3' is standard inverted Type-3 conditional (Benar).\n2) 'Corroborate' means to confirm with evidence (Benar).\n3) 'Mitigate' means to make less severe, which is the antonym of 'aggravate' (Salah)."
                        )
                    }
                )
            }
        }
        return list
    }
}
