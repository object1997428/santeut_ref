package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.MountainApiService
import com.santeut.data.model.response.CourseDetailResponse
import com.santeut.data.model.response.HikingCourseResponse
import com.santeut.data.model.response.MountainDetailResponse
import com.santeut.data.model.response.MountainResponse
import javax.inject.Inject

class MountainRepositoryImpl @Inject constructor(
    private val mountainApiService: MountainApiService
) : MountainRepository {

    override suspend fun popularMountain(): List<MountainResponse> {
        return try {
            val response = mountainApiService.popularMountain()
            if (response.status == "200") {
                response.data.mountainList ?: emptyList()
            } else {
                Log.e(
                    "MountainRepository",
                    "인기 있는 산 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MountainRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

    override suspend fun searchMountain(name: String, region: String?): List<MountainResponse> {
        return try {
            val response = mountainApiService.searchMountain(name, region)
            if (response.status == "200") {
                response.data.mountainList ?: emptyList()
            } else {
                Log.e(
                    "MountainRepository",
                    "산 목록 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MountainRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

    override suspend fun mountainDetail(mountainId: Int): MountainDetailResponse {
        return try {
            val response = mountainApiService.mountainDetail(mountainId)
            if (response.status == "200") {
                response.data
            } else {
                throw Exception("산 상세 조회 실패: ${response.status} - ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("MountainRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getHikingCourseList(mountainId: Int): List<HikingCourseResponse> {
        return try {
            val response = mountainApiService.getHikingCourseList(mountainId)
            if (response.status == "200") {
                response.data.hikingCourseList ?: emptyList()
            } else {
                Log.e(
                    "MountainRepository",
                    "코스 목록 조회 실패: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MountainRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAllCourses(mountainId: Int): List<CourseDetailResponse> {
        return try {
            val response = mountainApiService.getAllCourses(mountainId)
            if (response.status == "200") {
                response.data.course?: emptyList()
            } else {
                Log.e(
                    "MountainRepository",
                    "경로 없음: ${response.status} - ${response.data}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MountainRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

}