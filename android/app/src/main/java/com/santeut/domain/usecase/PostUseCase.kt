package com.santeut.domain.usecase

import com.santeut.data.model.request.CreatePostRequest
import com.santeut.data.model.response.CoursePostDetailResponse
import com.santeut.data.model.response.PostListResponse
import com.santeut.data.model.response.PostResponse
import com.santeut.data.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class PostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend fun getPosts(postType: Char): List<PostResponse> =
        postRepository.getPosts(postType)

    suspend fun createPost(
        images: List<MultipartBody.Part>?,
        createPostRequest: CreatePostRequest
    ): Flow<Unit> = postRepository.createPost(images, createPostRequest)

    suspend fun readPost(postId: Int, postType: Char): PostResponse =
        postRepository.readPost(postId, postType)

    suspend fun readCoursePostDetail(postId: Int, postType: Char, partyUserId: Int): CoursePostDetailResponse =
        postRepository.readCoursePostDetail(postId, postType, partyUserId)
}