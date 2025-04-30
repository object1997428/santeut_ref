package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class GuildListResponse(
    @SerializedName("guildList") val guildList: List<GuildResponse>
)

data class SearchGuildListResponse(
    @SerializedName("searchList") val searchList: List<GuildResponse>
)

data class GuildResponse(
    @SerializedName("guildId") val guildId: Int,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("guildProfile") val guildProfile: String,
    @SerializedName("guildInfo") val guildInfo: String,
    @SerializedName("guildMember") val guildMember: Int,
    @SerializedName("regionId") val regionId: Int,
    @SerializedName("guildGender") val guildGender: Char,
    @SerializedName("guildMinAge") val guildMinAge: Int,
    @SerializedName("guildMaxAge") val guildMaxAge: Int,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("joinStatus") val joinStatus: Char,
    @SerializedName("isPresident") val isPresident: Boolean,
    @SerializedName("guildIsPrivate") val guildIsPrivate: Boolean

)

data class GuildPostListResponse(
    @SerializedName("postList") val postList: List<GuildPostResponse>
)

data class GuildPostResponse(
    @SerializedName("guildPostId") val guildPostId: Int,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("userNickName") val userNickName: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("guildPostTitle") val guildPostTitle: String,
    @SerializedName("guildPostContent") val guildPostContent: String,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("likeCnt") val likeCnt: Int,
    @SerializedName("commentCnt") val commentCnt: Int,
    @SerializedName("hitCnt") val hitCnt: Int
)

data class GuildPostDetailResponse(
    @SerializedName("guildPostId") val guildPostId: Int,
    @SerializedName("guildId") val guildId: Int,
    @SerializedName("postType") val postType: Char,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("guildPostTitle") val guildPostTitle: String,
    @SerializedName("guildPostContent") val guildPostContent: String,
    @SerializedName("userNickName") val userNickName: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("likeCnt") val likeCnt: Int,
    @SerializedName("commentCnt") val commentCnt: Int,
    @SerializedName("hitCnt") val hitCnt: Int,
    @SerializedName("commentList") val commentList: List<CommentResponse>,
    @SerializedName("images") val images: List<String>,
    @SerializedName("writer") val writer: Boolean,
    @SerializedName("like") val like: Boolean
)

data class GuildMemberListResponse(
    @SerializedName("memberList") val memberList: List<GuildMemberResponse>
)

data class GuildMemberResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("userProfile") val userProfile: String,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("joinDate") val joinDate: LocalDateTime
)

data class GuildApplyListResponse(
    @SerializedName("applyGuildList") val guildApplyList: List<GuildApplyResponse>
)

data class GuildApplyResponse(
    @SerializedName("guildRequestId") val guildRequestId: Int,
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("userId") val userId: Int,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("userProfile") val userProfile: String
)

data class RankingListResponse(
    @SerializedName("partyMembers") val partyMembers: List<RankingResponse>
)

data class RankingResponse(
    @SerializedName("order") val order: Int,
    @SerializedName("score") val score: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("userProfile") val userProfile: String
)