package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.AudioRecorder

actual class PlatformAudioRecorder : AudioRecorder {
    override suspend fun startRecording() {}
    override suspend fun stopRecording() {}
    override fun isRecording(): Boolean = false
}
