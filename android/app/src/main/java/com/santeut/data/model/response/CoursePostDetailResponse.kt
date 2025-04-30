package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CoursePostDetailResponse (
    @SerializedName("postId") val postId: Int,
    @SerializedName("userPartyId") val userPartyId: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("postType") val postType: Char,
    @SerializedName("postTitle") val postTitle: String,
    @SerializedName("postContent") val postContent: String,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("likeCnt") val likeCnt: Int,
    @SerializedName("commentCnt") val commentCnt: Int,
    @SerializedName("hitCnt") val hitCnt: Int,
    @SerializedName("commentList") val commentList: List<CommentResponse>,
    @SerializedName("images") val images: List<String>,
    @SerializedName("writer") val writer: Boolean,
    @SerializedName("like") val like: Boolean,
    @SerializedName("locationDataList") val locationDataList: List<LocationData>,
)
