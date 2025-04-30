package com.santeut.data.util

sealed class RecordingState {
    object Idle : RecordingState()
    object OnRecord : RecordingState()
    object Paused : RecordingState()
}