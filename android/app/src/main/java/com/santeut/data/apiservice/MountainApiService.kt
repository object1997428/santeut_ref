package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.AllCourseResponse
import com.santeut.data.model.response.HikingCourseListResponse
import com.santeut.data.model.response.MountainDetailResponse
import com.santeut.data.model.response.MountainListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MountainApiService {

    @GET("/api/mountain/popular")
    suspend fun popularMountain(): CustomResponse<MountainListResponse>

    @GET("/api/mountain/")
    suspend fun searchMountain(
        @Query("name") name: String,
        @Query("region") region: String?
    ): CustomResponse<MountainListResponse>

    @GET("/api/mountain/v2/{mountainId}")
    suspend fun mountainDetail(
        @Path("mountainId") mountainId: Int
    ): CustomResponse<MountainDetailResponse>

    @GET("/api/mountain/v2/{mountainId}/course")
    suspend fun getHikingCourseList(
        @Path("mountainId") mountainId: Int
    ): CustomResponse<HikingCourseListResponse>

    @GET("/api/mountain/v2/{mountainId}/all-course")
    suspend fun getAllCourses(
        @Path("mountainId") mountainId: Int
    ): CustomResponse<AllCourseResponse>
}