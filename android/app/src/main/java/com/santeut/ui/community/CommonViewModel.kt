package com.santeut.ui.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.CommentResponse
import com.santeut.data.model.response.NotificationResponse
import com.santeut.domain.usecase.CommonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val commonUseCase: CommonUseCase
) : ViewModel() {

    private val _comments = MutableLiveData<List<CommentResponse>>()
    val comments: LiveData<List<CommentResponse>> = _comments

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _notiList = MutableLiveData<List<NotificationResponse>>()
    val notiList: LiveData<List<NotificationResponse>> = _notiList

    fun createComment(postId: Int, postType: Char, commentContent: String) {
        viewModelScope.launch {
            try {
                val createCommentRequest = CreateCommentRequest(commentContent)
                commonUseCase.createComment(postId, postType, createCommentRequest).collect {
                    Log.d("CommonViewModel", "Create Comment")
                }
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
            }
        }
    }

    fun getNotificationList() {
        viewModelScope.launch {
            try {
                _notiList.postValue(commonUseCase.getNotificationList())
            } catch (e: Exception) {
                _error.postValue("알림 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun hitLike(postId: Int, postType: Char) {
        viewModelScope.launch {
            try {
                commonUseCase.hitLike(postId, postType).collect {
                    Log.d("CommonViewModel", "Hit Like")
                }
            } catch (e: Exception) {
                _error.value = "Failed to hit Like: ${e.message}"
            }
        }
    }

    fun cancelLike(postId: Int, postType: Char) {
        viewModelScope.launch {
            try {
                commonUseCase.cancelLike(postId, postType).collect {
                    Log.d("CommonViewModel", "Cancel Like")
                }
            } catch (e: Exception) {
                _error.value = "Failed to cancel Like: ${e.message}"
            }
        }
    }
}