package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.NotificationListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommonApiService {
    @POST("/api/common/comment/{postId}/{postType}")
    suspend fun createComment(
        @Path("postId") postId: Int,
        @Path("postType") postType: String,
        @Body createCommentRequest: CreateCommentRequest
    ): CustomResponse<Unit>

    @GET("/api/common/alarm")
    suspend fun getNotificationList(): CustomResponse<NotificationListResponse>

    @GET("/api/common/like/{postId}/{postType}")
    suspend fun hitLike(
        @Path("postId") postId: Int,
        @Path("postType") postType: Char
    ): CustomResponse<Unit>

    @DELETE("/api/common/like/{postId}/{postType}")
    suspend fun cancelLike(
        @Path("postId") postId: Int,
        @Path("postType") postType: Char
    ): CustomResponse<Unit>

}