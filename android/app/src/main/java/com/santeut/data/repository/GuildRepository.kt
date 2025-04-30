package com.santeut.data.repository

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.request.CreateGuildRequest
import com.santeut.data.model.response.GuildApplyResponse
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.model.response.RankingResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.Path

interface GuildRepository {

    suspend fun getGuilds(): List<GuildResponse>

    suspend fun searchGuilds(regionName: String, gender: String): List<GuildResponse>

    suspend fun searchGuildByName(name: String?) : List<GuildResponse>

    suspend fun createGuild(
        guildProfile: MultipartBody.Part?,
        createGuildRequest: CreateGuildRequest
    ): Flow<Unit>

    suspend fun updateguild(
        guildId: Int,
        guildProfile: MultipartBody.Part?,
        updateGuildRequest: CreateGuildRequest
    ): Flow<Unit>

    suspend fun myGuilds(): List<GuildResponse>

    suspend fun getGuild(guildId: Int): GuildResponse

    suspend fun applyGuild(guildId: Int): Flow<Unit>

    suspend fun getGuildPostList(guildId: Int, categoryId: Int): List<GuildPostResponse>

    suspend fun createGuildPost(
        images: List<MultipartBody.Part>?,
        createGuildPostRequest: CreateGuildPostRequest
    ): Flow<Unit>

    suspend fun getGuildPost(guildPostId: Int): GuildPostDetailResponse

    suspend fun getGuildMemberList(guildId: Int): List<GuildMemberResponse>

    suspend fun getGuildApplyList(guildId: Int): List<GuildApplyResponse>

    suspend fun approveMember(guildId: Int, userId: Int): Flow<Unit>

    suspend fun denyMember(guildId: Int, userId: Int): Flow<Unit>

    suspend fun exileMember(guildId: Int, userId: Int): Flow<Unit>

    suspend fun changeLeader(guildId: Int, newLeaderId: Int): Flow<Unit>

    suspend fun quitGuild(guildId: Int): Flow<Unit>

    suspend fun getRanking(guildId: Int,type: Char): List<RankingResponse>   // C: 최다등반, H: 최고높이, D: 최장거리

}