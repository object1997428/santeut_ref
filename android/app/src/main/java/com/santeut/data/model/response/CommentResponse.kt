package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CommentResponse(
    @SerializedName("commentId") val commentId: Int,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("commentContent") val commentContent: String,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
)

data class NotificationListResponse(
    @SerializedName("alarmList") val notiList: List<NotificationResponse>
)

data class NotificationResponse(
    @SerializedName("alarmTitle") val alarmTitle: String,
    @SerializedName("alarmContent") val alarmContent: String,
    @SerializedName("referenceType") val referenceType: String,
    @SerializedName("referenceId") val referenceId: Int,
    @SerializedName("createdAt") val createdAt: LocalDateTime
)

