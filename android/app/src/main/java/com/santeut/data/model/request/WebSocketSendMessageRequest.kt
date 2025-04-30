package com.santeut.data.model.request

data class WebSocketSendMessageRequest(
    val type: String,
    val lat: String,
    val lng: String
)
