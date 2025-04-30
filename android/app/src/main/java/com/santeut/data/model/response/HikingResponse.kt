package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName

data class StartHikingResponse(

    @SerializedName("distance") val distance: Double,
    @SerializedName("courseList") val courseList: List<LocationDataResponse>

)
