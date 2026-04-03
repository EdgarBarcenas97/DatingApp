package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.AudioRecorder

actual class PlatformAudioRecorder : AudioRecorder {
    private var recording = false

    override suspend fun startRecording() {
        // AVAudioRecorder implementation would go here
        recording = true
    }

    override suspend fun stopRecording() {
        recording = false
    }

    override fun isRecording(): Boolean = recording
}
