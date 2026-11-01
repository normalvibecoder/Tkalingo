package com.example.data.ai

import android.util.Log
import com.example.data.model.DifficultyTier
import com.example.data.model.QuestionType
import com.example.data.model.SessionResultSummary
import com.example.data.model.TkaQuestion
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.ThinkingLevel
import com.google.firebase.ai.type.Tool
import com.google.firebase.ai.type.generationConfig
import com.google.firebase.ai.type.thinkingConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import java.util.UUID

object TkaAiTutorService {
    private const val TAG = "TkaAiTutorService"

    // 1. Complex reasoning model with ThinkingLevel.HIGH (no maxOutputTokens set)
    private val deepThinkingModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-4-argon",
            generationConfig = generationConfig {
                thinkingConfig = thinkingConfig {
                    thinkingLevel = ThinkingLevel.HIGH
                }
            }
        )
    }

    // 2. Search-grounded model using gemini-3.8-flash with googleSearch tool
    private val searchGroundedModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            tools = listOf(Tool.googleSearch())
        )
    }

    // 3. Fast hint model for instant 1-tap hints during practice
    private val fastHintModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.5-flash-lite"
        )
    }

    // 4. Structured JSON question generator for custom weakness remedial drills
    private val remedialSchema = Schema.obj(
        properties = mapOf(
            "questions" to Schema.array(
                items = Schema.obj(
                    properties = mapOf(
                        "stem" to Schema.string(description = "Pertanyaan soal TKA yang jelas dan kontekstual"),
                        "questionType" to Schema.enumeration(
                            values = listOf("SINGLE_CHOICE", "MCMA", "TRUE_FALSE_MATRIX"),
                            description = "Bentuk soal TKA"
                        ),
                        "difficulty" to Schema.integer(description = "1 untuk Mudah, 2 untuk Sedang, 3 untuk Sulit HOTS"),
                        "options" to Schema.array(
                            items = Schema.string(),
                            description = "Daftar pilihan jawaban (5 opsi untuk SINGLE_CHOICE, 4 pernyataan untuk MCMA, atau 3 pernyataan untuk TRUE_FALSE_MATRIX)"
                        ),
                        "correctAnswerCode" to Schema.string(
                            description = "Kode kunci jawaban: '0'..'4' untuk SINGLE_CHOICE, indeks dipisah koma misal '0,2' untuk MCMA, atau 'B,S,B' untuk TRUE_FALSE_MATRIX"
                        ),
                        "explanation" to Schema.string(description = "Pembahasan langkah demi langkah yang mudah dipahami")
                    )
                ),
                description = "Daftar soal latihan kelemahan TKA"
            )
        )
    )

    private val structuredRemedialModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = remedialSchema
            }
        )
    }

    suspend fun analyzeSessionWeaknessDeep(summary: SessionResultSummary): Result<String> {
        return try {
            val weakStr = summary.weakSubtopics.joinToString(", ").ifBlank { "Belum ada kelemahan signifikan" }
            val strongStr = summary.strongSubtopics.joinToString(", ").ifBlank { "-" }
            val subBreakdown = summary.subtopicPerformances.joinToString("\n") {
                "- ${it.subtopic}: Penguasaan ${it.masteryPercentage}% (${it.totalQuestions} soal)"
            }
            val prompt = """
                Anda adalah Bimo, AI Coach Spesialis TKA (Tes Kemampuan Akademik) dengan analisis mendalam.
                Siswa baru saja menyelesaikan sesi ${summary.mode} mata pelajaran ${summary.subject.name}:
                - Skor IRT TKA: ${summary.irtScore} / 1000
                - Jawaban Benar Penuh: ${summary.rawCorrectCount} dari ${summary.totalQuestions} soal
                - Waktu Pengerjaan: ${summary.durationSeconds / 60} menit ${summary.durationSeconds % 60} detik
                - Subtopik Kelemahan Utama: $weakStr
                - Subtopik Kuat: $strongStr
                - Rincian Per Subtopik:
                $subBreakdown

                Berikan analisis tajam, ramah, dan terstruktur (maksimal 3 paragraf singkat + poin strategi):
                1. Evaluasi arti skor IRT ${summary.irtScore} dan mengapa bobot soal HOTS/MCMA/Benar-Salah memengaruhi skor.
                2. Bedah konsep inti yang sering menjebak pada subtopik kelemahan ($weakStr) beserta rumus/kaidah kuncinya.
                3. Rencana aksi konkret sebelum memulai Sesi Latihan Kelemahan 10 Soal.
            """.trimIndent()

            val response = withTimeout(60_000L) {
                deepThinkingModel.generateContent(prompt)
            }
            val text = response.text?.trim()
            if (!text.isNullOrEmpty()) {
                Result.success(text)
            } else {
                Result.failure(IllegalStateException("AI mengembalikan respons kosong."))
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Deep weakness analysis timed out", e)
            Result.failure(Exception("Analisis mendalam melebihi batas waktu 60 detik. Silakan coba lagi."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Deep weakness analysis failed", e)
            Result.failure(e)
        }
    }

    suspend fun explainQuestionWithHighThinking(
        question: TkaQuestion,
        userAnswer: String,
        customUserQuery: String = ""
    ): Result<String> {
        return try {
            val prompt = """
                Jelaskan soal TKA berikut secara mendalam dengan penalaran langkah demi langkah (High Thinking Mode):
                Subtopik: ${question.subtopic} (${question.difficulty.label} - ${question.type.label})
                Soal: ${question.stem}
                Opsi/Pernyataan:
                ${question.options.mapIndexed { i, opt -> "${i + 1}. $opt" }.joinToString("\n")}
                Kunci Jawaban: ${question.correctAnswerCode}
                Jawaban Siswa: ${userAnswer.ifBlank { "(Belum dijawab)" }}
                Pembahasan Dasar: ${question.explanation}
                ${if (customUserQuery.isNotBlank()) "Pertanyaan Tambahan Siswa: $customUserQuery" else ""}

                Berikan:
                1. Konsep Dasar & Intuisi Cepat
                2. Bedah Langkah demi Langkah (mengapa opsi benar tepat & mengapa pengecoh salah)
                3. Tips Cepat TKA (Trik hemat waktu < 90 detik)
            """.trimIndent()

            val response = withTimeout(60_000L) {
                deepThinkingModel.generateContent(prompt)
            }
            val text = response.text?.trim()
            if (!text.isNullOrEmpty()) {
                Result.success(text)
            } else {
                Result.failure(IllegalStateException("Respons penjelasan kosong."))
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "High thinking explanation timed out", e)
            Result.failure(Exception("Penalaran AI melebihi batas waktu 60 detik."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "High thinking explanation failed", e)
            Result.failure(e)
        }
    }

    suspend fun generateQuickHint(question: TkaQuestion): Result<String> {
        return try {
            val prompt = """
                Berikan 1 petunjuk singkat (maksimal 2 kalimat) untuk membantu siswa menjawab soal TKA ini TANPA membocorkan jawaban akhirnya secara langsung:
                Subtopik: ${question.subtopic}
                Soal: ${question.stem}
            """.trimIndent()

            val response = withTimeout(35_000L) {
                fastHintModel.generateContent(prompt)
            }
            val text = response.text?.trim()
            if (!text.isNullOrEmpty()) {
                Result.success(text)
            } else {
                Result.failure(IllegalStateException("Petunjuk kosong."))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Quick hint failed", e)
            Result.failure(e)
        }
    }

    suspend fun searchGroundedTkaQuery(query: String, subjectName: String): Result<String> {
        return try {
            val prompt = """
                Gunakan Google Search untuk memberikan informasi akurat, terkini, dan terverifikasi terkait konteks TKA / Sains / Akademik ($subjectName):
                Pertanyaan Siswa: $query
                Jawab dalam bahasa Indonesia yang jelas, terstruktur, dan sertakan fakta ilmiah atau referensi kurikulum terbaru.
            """.trimIndent()

            val response = withTimeout(60_000L) {
                searchGroundedModel.generateContent(prompt)
            }
            val text = response.text?.trim()
            if (!text.isNullOrEmpty()) {
                Result.success(text)
            } else {
                Result.failure(IllegalStateException("Hasil pencarian kosong."))
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Search grounding timed out", e)
            Result.failure(Exception("Pencarian Google Search melebihi waktu 60 detik."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Search grounding failed", e)
            Result.failure(e)
        }
    }

    suspend fun generateCustomWeaknessQuestions(
        subjectId: String,
        subjectName: String,
        weakSubtopic: String,
        count: Int = 5
    ): Result<List<TkaQuestion>> {
        return try {
            val prompt = """
                Buatkan $count soal latihan TKA (Tes Kemampuan Akademik) berkualitas tinggi untuk mata pelajaran $subjectName yang berfokus penuh pada subtopik kelemahan siswa: "$weakSubtopic".
                Aturan wajib:
                - Variasikan questionType antara SINGLE_CHOICE (5 opsi, correctAnswerCode berupa indeks '0' sampai '4'), MCMA (4 opsi, correctAnswerCode berupa indeks benar dipisah koma seperti '0,1,2'), dan TRUE_FALSE_MATRIX (3 pernyataan, correctAnswerCode berupa 'B,B,S' atau 'B,S,B').
                - Variasikan difficulty antara 1 (Mudah), 2 (Sedang), dan 3 (Sulit HOTS).
                - Sertakan explanation (pembahasan lengkap langkah demi langkah).
            """.trimIndent()

            val response = withTimeout(60_000L) {
                structuredRemedialModel.generateContent(prompt)
            }
            val jsonStr = response.text ?: throw IllegalStateException("Respons JSON kosong.")
            val root = JSONObject(jsonStr)
            val arr = root.getJSONArray("questions")
            val resultList = mutableListOf<TkaQuestion>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val stem = obj.getString("stem")
                val qTypeStr = obj.optString("questionType", "SINGLE_CHOICE")
                val qType = try {
                    QuestionType.valueOf(qTypeStr)
                } catch (_: Exception) {
                    QuestionType.SINGLE_CHOICE
                }
                val diffInt = obj.optInt("difficulty", 2).coerceIn(1, 3)
                val optsArr = obj.getJSONArray("options")
                val opts = (0 until optsArr.length()).map { optsArr.getString(it) }
                val code = obj.getString("correctAnswerCode").trim()
                val exp = obj.getString("explanation").trim()
                if (opts.size >= 2 && stem.isNotBlank() && code.isNotBlank()) {
                    resultList.add(
                        TkaQuestion(
                            id = "ai_${UUID.randomUUID().toString().take(8)}_$i",
                            subjectId = subjectId,
                            unitId = "${subjectId}_u1",
                            subtopic = weakSubtopic,
                            difficulty = DifficultyTier.fromLevel(diffInt),
                            type = qType,
                            stem = stem,
                            options = opts,
                            correctAnswerCode = code,
                            explanation = exp
                        )
                    )
                }
            }
            if (resultList.isNotEmpty()) {
                Result.success(resultList)
            } else {
                Result.failure(IllegalStateException("Tidak ada soal valid yang dihasilkan."))
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Custom question generation timed out", e)
            Result.failure(Exception("Pembuatan soal AI melebihi batas waktu 60 detik."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Custom question generation failed", e)
            Result.failure(e)
        }
    }
}
