package com.santeut.data.repository

import android.util.Log
import com.santeut.data.apiservice.HikingApiService
import com.santeut.data.model.request.EndHikingRequest
import com.santeut.data.model.request.StartHikingRequest
import com.santeut.data.model.response.StartHikingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HikingRepositoryImpl @Inject constructor(
    private val hikingApiService: HikingApiService
) : HikingRepository {

    override suspend fun startHiking(startHikingRequest: StartHikingRequest): StartHikingResponse {
        return try {
            val response = hikingApiService.startHiking(startHikingRequest)
            if (response.status == "200") {
                response.data
            } else {
                throw Exception("등산 시작 실패: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("HikingRepository", "Network error while fetching start Hiking: ${e.message}", e)
            throw e
        }
    }

    override suspend fun endHiking(endHikingRequest: EndHikingRequest): Flow<Unit> = flow {
        val response = hikingApiService.endHiking(endHikingRequest)
        if (response.status == "200") {
            emit(Unit)
        } else {
            throw Exception("등산 종료 실패: ${response.status} ${response.data}")
        }

    }
}