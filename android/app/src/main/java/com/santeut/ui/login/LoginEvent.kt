package com.santeut.ui.login

sealed class LoginEvent {
    data class EnteredUserLoginId(val value: String) : LoginEvent()
    data class EnteredUserPassword(val value: String) : LoginEvent()
    object Login : LoginEvent()
}