package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.ExamAttempt
import com.example.data.model.SavedCustomQuestion
import com.example.data.model.TkaQuestion
import com.example.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TkaRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth
        get() = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeUserProfile(userId: String): Flow<UserProfile?> = flow {
        val path = "users/$userId"
        emitAll(
            db.collection("users").document(userId)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(
                            UserProfile::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    } else {
                        null
                    }
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.GET, path)
                    throw error
                }
        )
    }

    suspend fun getOrCreateUserProfile(displayName: String = "Pelajar TKA"): Result<UserProfile> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid)
            val snap = try {
                docRef.get().await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.GET, docRef.path)
                throw e
            }

            if (snap.exists()) {
                val existing = snap.toObject(
                    UserProfile::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                ) ?: UserProfile(id = uid, userId = uid, displayName = displayName)
                Result.success(existing)
            } else {
                val cleanName = displayName.trim().ifBlank { "Pelajar TKA" }.take(80)
                val payload = mapOf(
                    "userId" to uid,
                    "displayName" to cleanName,
                    "xp" to 0L,
                    "streakDays" to 1L,
                    "hearts" to 5L,
                    "gems" to 250L,
                    "totalExamsCompleted" to 0L,
                    "totalRemedialsCompleted" to 0L,
                    "weakSubtopics" to emptyList<String>(),
                    "masteredUnits" to emptyList<String>(),
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                try {
                    docRef.set(payload).await()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    handleFirestoreError(e, OperationType.CREATE, docRef.path)
                    throw e
                }
                val created = docRef.get().await().toObject(
                    UserProfile::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                ) ?: UserProfile(id = uid, userId = uid, displayName = cleanName)
                Result.success(created)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfileById(targetUserId: String): Result<UserProfile?> {
        val docRef = db.collection("users").document(targetUserId)
        return try {
            val snap = docRef.get().await()
            Result.success(
                snap.toObject(
                    UserProfile::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            throw e
        }
    }

    suspend fun updateProfileAfterSession(
        currentProfile: UserProfile,
        addedXp: Int,
        addedGems: Int,
        heartsRemaining: Int,
        isExamSimulation: Boolean,
        isRemedial: Boolean,
        newWeakSubtopics: List<String>,
        completedUnitId: String? = null
    ): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid)
            val mergedWeak = if (isRemedial) {
                // If remedial succeeded, remove remediated weak subtopics or update with remaining weak ones
                (currentProfile.weakSubtopics - newWeakSubtopics.toSet() + newWeakSubtopics).distinct().take(25)
            } else {
                (newWeakSubtopics + currentProfile.weakSubtopics).distinct().take(25)
            }
            val mergedUnits = if (completedUnitId != null) {
                (currentProfile.masteredUnits + completedUnitId).distinct().take(90)
            } else {
                currentProfile.masteredUnits.take(90)
            }

            val updates = mapOf(
                "xp" to (currentProfile.xp + addedXp).coerceAtLeast(0L),
                "streakDays" to (currentProfile.streakDays.coerceAtLeast(1L)),
                "hearts" to heartsRemaining.toLong().coerceIn(0L, 5L),
                "gems" to (currentProfile.gems + addedGems).coerceAtLeast(0L),
                "totalExamsCompleted" to (currentProfile.totalExamsCompleted + if (isExamSimulation) 1L else 0L),
                "totalRemedialsCompleted" to (currentProfile.totalRemedialsCompleted + if (isRemedial) 1L else 0L),
                "weakSubtopics" to mergedWeak,
                "masteredUnits" to mergedUnits,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.update(updates).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val uid = auth.currentUser?.uid ?: "unauthenticated"
            handleFirestoreError(e, OperationType.UPDATE, "users/$uid")
            Result.failure(e)
        }
    }

    suspend fun refillHeartsWithGems(currentProfile: UserProfile): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid)
            val cost = 50L
            val newGems = (currentProfile.gems - cost).coerceAtLeast(0L)
            docRef.update(
                mapOf(
                    "hearts" to 5L,
                    "gems" to newGems,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val uid = auth.currentUser?.uid ?: "unauthenticated"
            handleFirestoreError(e, OperationType.UPDATE, "users/$uid")
            Result.failure(e)
        }
    }

    fun observeExamAttempts(userId: String): Flow<List<ExamAttempt>> = flow {
        val path = "users/$userId/attempts"
        emitAll(
            db.collection("users").document(userId).collection("attempts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(50)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        ExamAttempt::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun recordExamAttempt(
        subjectId: String,
        subjectName: String,
        mode: String,
        irtScore: Int,
        correctCount: Int,
        totalQuestions: Int,
        durationSeconds: Int,
        weakSubtopics: List<String>,
        strongSubtopics: List<String>,
        aiAnalysisSummary: String = ""
    ): Result<String> {
        return try {
            val uid = requireUserId()
            val attemptId = "att_${UUID.randomUUID().toString().replace("-", "").take(20)}"
            val docRef = db.collection("users").document(uid).collection("attempts").document(attemptId)
            val payload = mapOf(
                "userId" to uid,
                "subjectId" to subjectId.take(64),
                "subjectName" to subjectName.take(100),
                "mode" to mode,
                "irtScore" to irtScore.toLong().coerceIn(0L, 1000L),
                "correctCount" to correctCount.toLong().coerceIn(0L, 200L),
                "totalQuestions" to totalQuestions.toLong().coerceIn(1L, 200L),
                "durationSeconds" to durationSeconds.toLong().coerceIn(0L, 10000L),
                "weakSubtopics" to weakSubtopics.map { it.take(100) }.take(20),
                "strongSubtopics" to strongSubtopics.map { it.take(100) }.take(20),
                "aiAnalysisSummary" to aiAnalysisSummary.take(4500),
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(attemptId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val uid = auth.currentUser?.uid ?: "unauthenticated"
            handleFirestoreError(e, OperationType.CREATE, "users/$uid/attempts")
            Result.failure(e)
        }
    }

    suspend fun updateAttemptAiSummary(attemptId: String, summary: String): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid).collection("attempts").document(attemptId)
            docRef.update(
                mapOf(
                    "aiAnalysisSummary" to summary.take(4500),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val uid = auth.currentUser?.uid ?: "unauthenticated"
            handleFirestoreError(e, OperationType.UPDATE, "users/$uid/attempts/$attemptId")
            Result.failure(e)
        }
    }

    fun observeSavedCustomQuestions(userId: String): Flow<List<SavedCustomQuestion>> = flow {
        val path = "users/$userId/custom_questions"
        emitAll(
            db.collection("users").document(userId).collection("custom_questions")
                .limit(100)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        SavedCustomQuestion::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun saveCustomQuestions(questions: List<TkaQuestion>): Result<Int> {
        return try {
            val uid = requireUserId()
            var saved = 0
            for (q in questions) {
                val qId = "cq_${UUID.randomUUID().toString().replace("-", "").take(20)}"
                val docRef = db.collection("users").document(uid).collection("custom_questions").document(qId)
                val payload = mapOf(
                    "userId" to uid,
                    "subjectId" to q.subjectId.take(64),
                    "subtopic" to q.subtopic.take(100),
                    "difficulty" to q.difficulty.level.toLong().coerceIn(1L, 3L),
                    "questionType" to q.type.name,
                    "stem" to q.stem.take(2800),
                    "options" to q.options.map { it.take(280) }.take(10),
                    "correctAnswerCode" to q.correctAnswerCode.take(180),
                    "explanation" to q.explanation.take(3800),
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
                saved++
            }
            Result.success(saved)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val uid = auth.currentUser?.uid ?: "unauthenticated"
            handleFirestoreError(e, OperationType.CREATE, "users/$uid/custom_questions")
            Result.failure(e)
        }
    }
}
