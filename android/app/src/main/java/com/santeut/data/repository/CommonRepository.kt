package com.santeut.data.repository

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.NotificationResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

interface CommonRepository {
    suspend fun createComment(
        postId: Int,
        postType: Char,
        commentRequest: CreateCommentRequest
    ): Flow<Unit>

    suspend fun getNotificationList(): List<NotificationResponse>

    suspend fun hitLike(
        @Path("postId") postId: Int,
        @Path("postType") postType: Char
    ): Flow<Unit>

    suspend fun cancelLike(
        @Path("postId") postId: Int,
        @Path("postType") postType: Char
    ): Flow<Unit>

}