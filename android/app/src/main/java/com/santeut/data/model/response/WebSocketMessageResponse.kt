package com.santeut.data.model.response

data class WebSocketMessageResponse(
    val type: String,
    val partyId: Int,
    val userId: Int,
    val userNickname: String,
    val userProfile: String?,
    val lat: String,
    val lng: String
)
