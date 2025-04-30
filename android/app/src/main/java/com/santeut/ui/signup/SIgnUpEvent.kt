package com.santeut.ui.signup

sealed class SignUpEvent {
    data class EnteredUserNickName(val value: String) : SignUpEvent()
    data class EnteredUserLoginId(val value: String) : SignUpEvent()
    data class EnteredUserPassword(val value: String) : SignUpEvent()
    data class EnteredUserPassword2(val value: String) : SignUpEvent()
    data class EnteredUserBirth(val value: String) : SignUpEvent()
    data class EnteredUserGender(val value: Boolean) : SignUpEvent()
    object SignUp : SignUpEvent()
}