package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateGuildRequest
import com.santeut.data.model.response.GuildApplyListResponse
import com.santeut.data.model.response.GuildListResponse
import com.santeut.data.model.response.GuildMemberListResponse
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.data.model.response.GuildPostListResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.model.response.RankingListResponse
import com.santeut.data.model.response.SearchGuildListResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GuildApiService {

    @GET("/api/guild/list")
    suspend fun getGuilds(): CustomResponse<GuildListResponse>

    @GET("/api/guild/search")
    suspend fun searchGuilds(
        @Query("regionName") regionName: String,
        @Query("gender") gender: String
    ): CustomResponse<SearchGuildListResponse>

    @POST("/api/guild/create")
    @Multipart
    suspend fun createGuild(
        @Part guildProfile: MultipartBody.Part?,
        @Part request: MultipartBody.Part
    ): CustomResponse<Unit>

    @PATCH("/api/guild/{guildId}")
    @Multipart
    suspend fun updateGuild(
        @Path("guildId") guildId: Int,
        @Part guildProfile: MultipartBody.Part?,
        @Part request: MultipartBody.Part
    ): CustomResponse<Unit>

    @GET("/api/guild/myguild")
    suspend fun myGuilds(): CustomResponse<GuildListResponse>

    @GET("/api/guild/{guildId}")
    suspend fun getGuild(
        @Path("guildId") guildId: Int
    ): CustomResponse<GuildResponse>

    @POST("/api/guild/user/apply/{guildId}")
    suspend fun applyGuild(
        @Path("guildId") guildId: Int
    ): CustomResponse<Unit>

    @GET("/api/guild/post/{guildId}/{categoryId}")
    suspend fun getGuildPostList(
        @Path("guildId") guildId: Int,
        @Path("categoryId") categoryId: Int
    ): CustomResponse<GuildPostListResponse>

    @POST("/api/guild/post")
    @Multipart
    suspend fun createGuildPost(
        @Part images: List<MultipartBody.Part>?,
        @Part createGuildPostRequest: MultipartBody.Part
    ): CustomResponse<Unit>

    @GET("/api/guild/post/{guildPostId}")
    suspend fun getGuildPost(
        @Path("guildPostId") guildPostId: Int
    ): CustomResponse<GuildPostDetailResponse>

    @GET("/api/guild/user/{guildId}/member-list")
    suspend fun getGuildMemberList(
        @Path("guildId") guildId: Int
    ): CustomResponse<GuildMemberListResponse>

    @GET("/api/guild/user/apply-list/{guildId}")
    suspend fun getGuildApplyList(
        @Path("guildId") guildId: Int,
    ): CustomResponse<GuildApplyListResponse>

    @PATCH("/api/guild/user/{guildId}/{userId}/approve")
    suspend fun approveMember(
        @Path("guildId") guildId: Int,
        @Path("userId") userId: Int
    ): CustomResponse<Unit>

    @PATCH("/api/guild/user/{guildId}/{userId}/deny")
    suspend fun denyMember(
        @Path("guildId") guildId: Int,
        @Path("userId") userId: Int
    ): CustomResponse<Unit>

    @DELETE("/api/guild/user/{guildId}/member-list/{userId}")
    suspend fun exileMember(
        @Path("guildId") guildId: Int,
        @Path("userId") userId: Int
    ): CustomResponse<Unit>

    @PATCH("/api/guild/user/{guildId}/delegate/{newLeaderId}")
    suspend fun changeLeader(
        @Path("guildId") guildId: Int,
        @Path("newLeaderId") newLeaderId: Int
    ): CustomResponse<Unit>

    @DELETE("/api/guild/user/{guildId}/quit")
    suspend fun quitGuild(
        @Path("guildId") guildId: Int
    ): CustomResponse<Unit>

    @GET("/api/guild/{guildId}/rank/{type}")
    suspend fun getRanking(
        @Path("guildId") guildId: Int,
        @Path("type") type: Char
    ): CustomResponse<RankingListResponse>

    @GET("/api/guild/search/guild")
    suspend fun searchGuildByName(
        @Query("name") name: String?
    ): CustomResponse<SearchGuildListResponse>

}