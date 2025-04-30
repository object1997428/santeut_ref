package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName

data class PartyCourseResponse(
    @SerializedName("distance") val distance: Double,
    @SerializedName("courseList") val courseList: List<CoordResponse>
)

data class CoordResponse (
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
) {
    companion object {
        fun getLocations(locations: List<CoordResponse>): List<CoordResponse> {
            return locations.filter { !it.lat.isNaN() && !it.lng.isNaN() }
        }
    }
}