package com.santeut.data.model.request

import java.time.LocalDateTime

data class StartHikingRequest(
    val partyId: Int,
    val startTime: LocalDateTime
)
