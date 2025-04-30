package com.santeut.ui.login

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.MainApplication
import com.santeut.data.model.request.FCMTokenRequest
import com.santeut.data.model.request.LoginRequest
import com.santeut.domain.usecase.FCMTokenUseCase
import com.santeut.domain.usecase.LoginUseCase
import com.santeut.ui.landing.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val fcmTokenUseCase: FCMTokenUseCase,
) : ViewModel() {
    private var _userState = mutableStateOf(UserState())
    val userState = _userState

    private val _userLoginId = mutableStateOf("")
    val userLoginId: State<String> = _userLoginId

    private val _userPassword = mutableStateOf("")
    val userPassword: State<String> = _userPassword

    private val _uiEvent = MutableStateFlow<LoginUiEvent>(LoginUiEvent.Idle)
    val uiEvent: StateFlow<LoginUiEvent> = _uiEvent

    private val _errorMessage = mutableStateOf("")
    val errorMessage = _errorMessage

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnteredUserLoginId -> {
                _userLoginId.value = event.value
            }

            is LoginEvent.EnteredUserPassword -> {
                _userPassword.value = event.value
            }

            is LoginEvent.Login -> {
                viewModelScope.launch {
                    Log.d("Login ViewModel", "이벤트 발생")
                    loginUseCase.excute(
                        LoginRequest(
                            userLoginId = _userLoginId.value,
                            userPassword = _userPassword.value
                        )
                    ).catch { e ->
                        Log.d("Login Error", "${e.message}")
                        _uiEvent.value = LoginUiEvent.Login(false)

                        _userLoginId.value = ""
                        _userPassword.value = ""

                        _errorMessage.value = e.message.toString()
                    }.collectLatest { data ->
                        Log.d("Login Success", "Success ${data.accessToken}")

                        MainApplication.sharedPreferencesUtil.saveToken(data)

                        val fcmToken = MainApplication.sharedPreferencesUtil.getFcmToken()
                        if (fcmToken != null) {
                            Log.d("Fcm Token", fcmToken)
                            fcmTokenUseCase.execute(
                                FCMTokenRequest(fcmToken)
                            ).catch { e ->
                                Log.d("FCM Token 저장 실패", "${e.message}")
                            }
                                .collect { result ->
                                    Log.d("FCM Token 저장 성공", "$result")
                                }
                        }

                        _userState.value.copy(token = data.accessToken, isLoggedIn = true)
                        _uiEvent.value = LoginUiEvent.Login(true)
                    }
                }
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
        _uiEvent.value = LoginUiEvent.Idle
    }

    @Stable
    sealed interface LoginUiEvent {
        @Immutable
        data object Idle : LoginUiEvent

        @Immutable
        data class Login(val success: Boolean) : LoginUiEvent
    }
}
