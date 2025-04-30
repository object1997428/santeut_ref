package com.santeut.domain.usecase

import com.santeut.data.model.response.CourseDetailResponse
import com.santeut.data.model.response.HikingCourseResponse
import com.santeut.data.model.response.MountainDetailResponse
import com.santeut.data.model.response.MountainResponse
import com.santeut.data.repository.MountainRepository
import javax.inject.Inject

class MountainUseCase @Inject constructor(
    private val mountainRepository: MountainRepository
) {
    suspend fun getAllCourses(mountainId: Int): List<CourseDetailResponse> =
        mountainRepository.getAllCourses(mountainId)

    suspend fun popularMountain(): List<MountainResponse> = mountainRepository.popularMountain()

    suspend fun searchMountain(name: String, region: String?): List<MountainResponse> =
        mountainRepository.searchMountain(name, region)

    suspend fun mountainDetail(mountainId: Int): MountainDetailResponse =
        mountainRepository.mountainDetail(mountainId)

    suspend fun getHikingCourseList(mountainId: Int): List<HikingCourseResponse> =
        mountainRepository.getHikingCourseList(mountainId)
}