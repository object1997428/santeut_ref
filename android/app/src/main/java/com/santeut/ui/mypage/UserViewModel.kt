package com.santeut.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.response.MyProfileResponse
import com.santeut.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _myProfile = MutableLiveData<MyProfileResponse>()
    val myProfile: LiveData<MyProfileResponse> = _myProfile

    fun getMyProfile() {
        viewModelScope.launch {
            try {
                _myProfile.value = userUseCase.getMyProfile()
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.message}"
            }
        }
    }

}