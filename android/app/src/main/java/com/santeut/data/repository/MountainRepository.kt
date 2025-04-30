package com.santeut.data.repository

import com.santeut.data.model.response.CourseDetailResponse
import com.santeut.data.model.response.HikingCourseResponse
import com.santeut.data.model.response.MountainDetailResponse
import com.santeut.data.model.response.MountainResponse

interface MountainRepository {
    suspend fun popularMountain(): List<MountainResponse>
    suspend fun searchMountain(name: String, region: String?): List<MountainResponse>
    suspend fun mountainDetail(mountainId: Int): MountainDetailResponse
    suspend fun getHikingCourseList(mountainId: Int): List<HikingCourseResponse>
    suspend fun getAllCourses(mountainId: Int): List<CourseDetailResponse>

}