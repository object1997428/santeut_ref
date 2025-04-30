package com.santeut.data.model.request
data class CreatePostRequest(
    val postTitle: String,
    val postContent: String,
    val postType: Char,
    val userPartyId: Int,
)
