package com.santeut.ui.chat

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("content") var content: String,
    @SerializedName("type") var type: String
)