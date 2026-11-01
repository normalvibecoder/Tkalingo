package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class TkaRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun getOrCreateUserProfile_authenticatedUser_createsAndReadsProfile() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repository = TkaRepository(firestore)

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.getOrCreateUserProfile("Alice TKA")
        }
        assertTrue(result.isSuccess)
        val profile = result.getOrThrow()
        assertEquals(uid, profile.userId)
        assertEquals("Alice TKA", profile.displayName)
    }

    @Test
    fun recordAndObserveExamAttempts_authenticatedOwner_succeeds() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repository = TkaRepository(firestore)

        val createResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.recordExamAttempt(
                subjectId = "mat_wajib",
                subjectName = "Matematika Wajib",
                mode = "SIMULASI_TKA",
                irtScore = 785,
                correctCount = 24,
                totalQuestions = 30,
                durationSeconds = 2850,
                weakSubtopics = listOf("Logaritma"),
                strongSubtopics = listOf("Fungsi Kuadrat"),
                aiAnalysisSummary = "Perkuat sifat-sifat logaritma basis campuran."
            )
        }
        assertTrue(createResult.isSuccess)
        val attemptId = createResult.getOrThrow()

        val attempts = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeExamAttempts(uid).first { list -> list.any { it.id == attemptId } }
        }
        assertTrue(attempts.any { it.id == attemptId && it.irtScore == 785L })
    }

    @Test
    fun getUserProfileById_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val aliceRepo = TkaRepository(firestore)
        withTimeout(DEFAULT_TIMEOUT_MS) { aliceRepo.getOrCreateUserProfile("Alice").getOrThrow() }

        signInTestUser(BOB_EMAIL)
        val bobRepo = TkaRepository(firestore)
        try {
            withTimeout(DEFAULT_TIMEOUT_MS) {
                bobRepo.getUserProfileById(aliceUid).getOrThrow()
            }
            fail("Expected FirebaseFirestoreException PERMISSION_DENIED")
        } catch (e: FirebaseFirestoreException) {
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, e.code)
        }
    }

    @Test
    fun observeExamAttempts_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        auth.signOut()
        val repository = TkaRepository(firestore)

        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repository.observeExamAttempts(aliceUid).first()
            }
            fail("Expected exception for unauthenticated user")
        } catch (e: Exception) {
            val firestoreException = e.unwrapFirestoreException()
            assertNotNull("Expected nested FirebaseFirestoreException, got $e", firestoreException)
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException!!.code)
        }
    }

    private companion object {
        const val ALICE_EMAIL = "alice@tkalingo.test"
        const val BOB_EMAIL = "bob@tkalingo.test"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 4000L
    }
}
