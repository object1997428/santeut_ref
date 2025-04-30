package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName

data class ChatResponse (
    @SerializedName("chatRoom") val chatRoomList: List<ChatRoomInfo>
)

data class ChatRoomInfo (
    @SerializedName("partyId") val partyId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("peopleCnt") val peopleCnt: Int,
    @SerializedName("lastMessage") val lastMessage: String?,
    @SerializedName("lastSentDate") val lastSentDate: String?
)

data class ChatRoomResponse(
    @SerializedName("chatMessage") val chatMessageList: List<ChatMessage>
)

data class ChatMessage(
    // @SerializedName("userId") val userId: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("userProfile") var userProfile: String?,
    @SerializedName("content") val content: String
)