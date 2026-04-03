package com.plcoding.emergency.data.platform

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.plcoding.emergency.domain.AudioRecorder
import java.io.File

actual class PlatformAudioRecorder(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null
    private var recording = false

    override suspend fun startRecording() {
        if (recording) return

        val outputFile = File(context.cacheDir, "emergency_audio_${System.currentTimeMillis()}.m4a")

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
        }
        recording = true
    }

    override suspend fun stopRecording() {
        if (!recording) return
        try {
            recorder?.stop()
            recorder?.release()
        } catch (e: Exception) {
            // Ignore
        }
        recorder = null
        recording = false
    }

    override fun isRecording(): Boolean = recording
}
