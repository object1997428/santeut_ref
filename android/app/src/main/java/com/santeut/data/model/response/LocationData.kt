package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class LocationData(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lnt") val lng: Double
)


