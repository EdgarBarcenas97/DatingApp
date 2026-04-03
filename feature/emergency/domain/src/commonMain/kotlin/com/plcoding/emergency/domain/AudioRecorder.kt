package com.plcoding.emergency.domain

interface AudioRecorder {
    suspend fun startRecording()
    suspend fun stopRecording()
    fun isRecording(): Boolean
}
