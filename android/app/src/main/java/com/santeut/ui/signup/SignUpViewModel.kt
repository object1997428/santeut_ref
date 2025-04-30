package com.santeut.ui.signup

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.SignUpRequest
import com.santeut.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _userLoginId = mutableStateOf("")
    val userLoginId: State<String> = _userLoginId

    private val _userNickName = mutableStateOf("")
    val userNickName: State<String> = _userNickName

    private val _userPassword = mutableStateOf("")
    val userPassword: State<String> = _userPassword

    private val _userPassword2 = mutableStateOf("")
    val userPassword2: State<String> = _userPassword2

    private val _userBirth = mutableStateOf("")
    val userBirth: State<String> = _userBirth

    private val _userGender = mutableStateOf(true)
    val userGender: State<Boolean> = _userGender

    private val _uiEvent = MutableStateFlow<SignUpUiEvent>(SignUpUiEvent.Idle)
    val uiEvent: StateFlow<SignUpUiEvent> = _uiEvent

    private val _errorMessage = mutableStateOf("")
    val errorMessage = _errorMessage

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EnteredUserLoginId -> {
                _userLoginId.value = event.value
            }

            is SignUpEvent.EnteredUserNickName -> {
                _userNickName.value = event.value
            }

            is SignUpEvent.EnteredUserPassword -> {
                _userPassword.value = event.value
            }

            is SignUpEvent.EnteredUserPassword2 -> {
                _userPassword2.value = event.value
            }

            is SignUpEvent.EnteredUserBirth -> {
                _userBirth.value = event.value
            }

            is SignUpEvent.EnteredUserGender -> {
                _userGender.value = event.value
            }

            is SignUpEvent.SignUp -> {
                viewModelScope.launch {
                    Log.d("SignUp", "userId: ${_userLoginId.value}")
                    signUpUseCase.excute(
                        SignUpRequest(
                            userLoginId = _userLoginId.value,
                            userPassword = _userPassword.value,
                            userNickname = _userNickName.value,
                            userBirth = _userBirth.value,
                            userGender = if (_userGender.value) 'M' else 'F'
                        )
                    ).catch { e ->
                        Log.d("SignUp Error", "${e.message}")
                        _errorMessage.value = e.message.toString()
                        _uiEvent.value = SignUpUiEvent.SignUp(false)
                    }.collectLatest { data ->
                        Log.d("SignUp Success", "Success")
                        _uiEvent.value = SignUpUiEvent.SignUp(true)
                    }
                }
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    @Stable
    sealed interface SignUpUiEvent {
        @Immutable
        data object Idle : SignUpUiEvent

        @Immutable
        data class SignUp(val success: Boolean) : SignUpUiEvent
    }
}