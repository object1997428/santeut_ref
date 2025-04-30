package com.santeut.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.santeut.data.apiservice.PostApiService
import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.request.CreatePostRequest
import com.santeut.data.model.response.CoursePostDetailResponse
import com.santeut.data.model.response.PostResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postApiService: PostApiService
) : PostRepository {
    override suspend fun getPosts(postType: Char): List<PostResponse> {
        return try {
            val response = postApiService.getPosts(postType)
            if (response.isSuccessful) {
                response.body()?.data?.postList ?: emptyList()
            } else {
                Log.e(
                    "PostRepository",
                    "Error fetching posts: ${response.code()} - ${response.message()}"
                )
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun createPost(
        images: List<MultipartBody.Part>?,
        createPostRequest: CreatePostRequest
    ): Flow<Unit> = flow {
        val response = postApiService.createPost(images, createPostPart(createPostRequest))
        if (response.status == "201") {
            emit(response.data)
        }
    }

    private fun createPostPart(createPostPart: CreatePostRequest): MultipartBody.Part {
        val json = Gson().toJson(createPostPart)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("postCreateRequestDto", null, requestBody)
    }

    override suspend fun readPost(postId: Int, postType: Char): PostResponse {
        try {
            val response = postApiService.readPost(postId, postType.toString())
            if (response.status == "200") {
                response.data.let {
                    return it
                }
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Network error while fetching post: ${e.message}", e)
            throw e
        }
    }

    override suspend fun readCoursePostDetail(
        postId: Int,
        postType: Char,
        partyUserId: Int
    ): CoursePostDetailResponse {
        try {
            val response = postApiService.readCoursePostDetail(postId, postType, partyUserId)
            if (response.status == "200") {
                response.data.let {
                    return it
                }
            } else {
                throw Exception("Failed to load post: ${response.status} ${response.data}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
