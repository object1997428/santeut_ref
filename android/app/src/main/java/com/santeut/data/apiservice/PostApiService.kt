package com.santeut.data.apiservice

import com.santeut.data.model.response.CoursePostDetailResponse
import com.santeut.data.model.CustomResponse
import com.santeut.data.model.response.PostListResponse
import com.santeut.data.model.response.PostResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {
    @GET("/api/community/post")
    suspend fun getPosts(
        @Query("postType") postType: Char
    ): Response<CustomResponse<PostListResponse>>

    @POST("/api/community/post")
    @Multipart
    suspend fun createPost(
        @Part images: List<MultipartBody.Part>?,
        @Part createPostRequest: MultipartBody.Part
    ): CustomResponse<Unit>

    @GET("/api/community/post/{postId}/{postType}")
    suspend fun readPost(
        @Path("postId") postId: Int,
        @Path("postType") postType: String
    ): CustomResponse<PostResponse>

    @GET("/api/community/course/{postId}/{postType}/{partyUserId}")
    suspend fun readCoursePostDetail(
        @Path("postId") postId: Int,
        @Path("PostType") postType: Char,
        @Path("partyUserId") partyUserId: Int
    ): CustomResponse<CoursePostDetailResponse>

}