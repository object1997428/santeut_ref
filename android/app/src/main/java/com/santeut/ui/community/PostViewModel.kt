package com.santeut.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreatePostRequest
import com.santeut.data.model.response.PostResponse
import com.santeut.domain.usecase.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _posts = MutableLiveData<List<PostResponse>>()
    val posts: LiveData<List<PostResponse>> = _posts

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _postCreationSuccess = MutableLiveData<Boolean>()
    val postCreationSuccess: LiveData<Boolean> = _postCreationSuccess

    private val _post = MutableLiveData<PostResponse>()
    val post: LiveData<PostResponse> = _post

    init {
        val postType = savedStateHandle.get<String>("postType")?.first() ?: 'T'
        getPosts(postType)
    }

    fun getPosts(postType: Char) {
        viewModelScope.launch {
            try {
                _posts.value = postUseCase.getPosts(postType)
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
            }
        }
    }

    fun createPost(images: List<MultipartBody.Part>?, createPostRequest: CreatePostRequest) {
        viewModelScope.launch {
            try {
                postUseCase.createPost(images, createPostRequest).collect {
                    _postCreationSuccess.value = true
                    Log.d("PostViewModel", "Navigating to postTips Ready")
                }

            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
                _postCreationSuccess.value = false
            }
        }
    }

    fun readPost(postId: Int, postType: Char) {
        viewModelScope.launch {
            try {
                _post.value = postUseCase.readPost(postId, postType)
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
            }
        }
    }
}