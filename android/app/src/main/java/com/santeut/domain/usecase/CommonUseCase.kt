package com.santeut.domain.usecase

import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.NotificationResponse
import com.santeut.data.repository.CommonRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path
import javax.inject.Inject

class CommonUseCase @Inject constructor(
    private val commonRepository: CommonRepository
) {
    suspend fun createComment(
        postId: Int,
        postType: Char,
        createCommentRequest: CreateCommentRequest
    ): Flow<Unit> =
        commonRepository.createComment(postId, postType, createCommentRequest)

    suspend fun getNotificationList(): List<NotificationResponse> =
        commonRepository.getNotificationList()

    suspend fun hitLike(
        @Path("postId") postId: Int,
        @Path("postType") postType: Char
    ): Flow<Unit> = commonRepository.hitLike(postId, postType)

    suspend fun cancelLike(
        @Path("postId") postId: Int,
        @Path("postType") postType: Char
    ): Flow<Unit> = commonRepository.cancelLike(postId, postType)
}