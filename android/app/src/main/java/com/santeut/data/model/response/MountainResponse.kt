package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName


data class MountainListResponse(
    @SerializedName("mountainList") val mountainList: List<MountainResponse>
)

data class MountainResponse(

    @SerializedName("mountainId") val mountainId: Int,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("regionName") val regionName: String,
    @SerializedName("height") val height: Int,
    @SerializedName("courseCount") val courseCount: Int,
    @SerializedName("isTop100") val isTop100: Boolean,
    @SerializedName("image") val image: String,
    @SerializedName("top100") val top100: Boolean

)

data class MountainDetailResponse(

    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("address") val address: String,
    @SerializedName("description") val description: String,
    @SerializedName("height") val height: Int,
    @SerializedName("courseCount") val courseCount: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("views") val views: Int,
    @SerializedName("image") val image: String

)

data class HikingCourseListResponse(
    @SerializedName("courseList") val hikingCourseList: List<HikingCourseResponse>
)

data class HikingCourseResponse(
    @SerializedName("courseId") val courseId: Int,
    @SerializedName("courseName") val courseName: String?,
    @SerializedName("distance") val distance: Double,
    @SerializedName("level") val level: String,
    @SerializedName("upTime") val upTime: Int,
    @SerializedName("downTime") val downTime: Int
)