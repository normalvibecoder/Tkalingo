package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Synthesizes crisp, cheerful Duolingo-inspired sound effects procedurally using AudioTrack
 * so the app has zero external audio file dependencies and instant feedback.
 */
object SoundEffectsManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    var isSoundEnabled: Boolean = true

    fun playTap() {
        if (!isSoundEnabled) return
        scope.launch {
            playToneSequence(
                notes = listOf(Note(680.0, 35, 0.25))
            )
        }
    }

    fun playCorrectChime() {
        if (!isSoundEnabled) return
        scope.launch {
            // Bright Duolingo-style ascending major chime (G5 -> B5 -> D6 -> G6)
            playToneSequence(
                notes = listOf(
                    Note(783.99, 75, 0.45),
                    Note(987.77, 75, 0.50),
                    Note(1174.66, 90, 0.55),
                    Note(1567.98, 190, 0.60)
                )
            )
        }
    }

    fun playWrongSound() {
        if (!isSoundEnabled) return
        scope.launch {
            // Soft descending two-tone thud (F4 -> B3)
            playToneSequence(
                notes = listOf(
                    Note(349.23, 120, 0.45),
                    Note(246.94, 210, 0.50)
                )
            )
        }
    }

    fun playCelebrationFanfare() {
        if (!isSoundEnabled) return
        scope.launch {
            // Victory fanfare arpeggio (C5 -> E5 -> G5 -> C6 -> E6 -> G6)
            playToneSequence(
                notes = listOf(
                    Note(523.25, 80, 0.45),
                    Note(659.25, 80, 0.45),
                    Note(783.99, 80, 0.50),
                    Note(1046.50, 110, 0.55),
                    Note(1318.51, 110, 0.55),
                    Note(1567.98, 280, 0.65)
                )
            )
        }
    }

    private data class Note(
        val freqHz: Double,
        val durationMs: Int,
        val amplitude: Double
    )

    private fun playToneSequence(notes: List<Note>) {
        try {
            val sampleRate = 24000
            val totalSamples = notes.sumOf { (sampleRate * it.durationMs) / 1000 }
            if (totalSamples <= 0) return

            val pcm = ShortArray(totalSamples)
            var offset = 0

            for (note in notes) {
                val count = (sampleRate * note.durationMs) / 1000
                for (i in 0 until count) {
                    val t = i.toDouble() / sampleRate.toDouble()
                    // Attack + exponential decay envelope for warm marimba/bell timbre
                    val attack = (i.toDouble() / (sampleRate * 0.006)).coerceAtMost(1.0)
                    val decay = exp(-4.5 * (i.toDouble() / count.toDouble()))
                    val env = attack * decay
                    val fundamental = sin(2.0 * PI * note.freqHz * t)
                    val overtone = 0.28 * sin(2.0 * PI * (note.freqHz * 2.0) * t)
                    val sampleVal = (fundamental + overtone) * env * note.amplitude * Short.MAX_VALUE
                    pcm[offset + i] = sampleVal.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
                offset += count
            }

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(pcm.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(pcm, 0, pcm.size)
            track.play()
            val totalDurationMs = notes.sumOf { it.durationMs }.toLong()
            Thread.sleep(totalDurationMs + 80L)
            track.stop()
            track.release()
        } catch (_: Exception) {
            // Ignore audio hardware unavailability in headless/test environments
        }
    }
}
