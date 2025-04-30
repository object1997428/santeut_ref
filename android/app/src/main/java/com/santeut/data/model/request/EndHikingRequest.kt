package com.santeut.data.model.request

import java.time.LocalDateTime

data class EndHikingRequest(
    val partyId: Int,
    val distance: Int,
    val bestHeight: Int,
    val endTime: LocalDateTime
)
