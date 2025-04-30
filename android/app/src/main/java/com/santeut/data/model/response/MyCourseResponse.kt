package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName

data class MyCourseResponse (
    @SerializedName("courseList") val courseList: List<CoordResponse>
)