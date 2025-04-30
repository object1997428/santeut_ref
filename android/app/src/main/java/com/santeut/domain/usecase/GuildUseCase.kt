package com.santeut.domain.usecase

import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.request.CreateGuildRequest
import com.santeut.data.model.response.GuildApplyResponse
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.model.response.RankingResponse
import com.santeut.data.repository.GuildRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class GuildUseCase @Inject constructor(
    private val guildRepository: GuildRepository
) {

    suspend fun getGuilds(): List<GuildResponse> =
        guildRepository.getGuilds()

    suspend fun searchGuilds(regionName: String, gender: String): List<GuildResponse> =
        guildRepository.searchGuilds(regionName, gender)

    suspend fun searchGuildByName(name: String?): List<GuildResponse> =
        guildRepository.searchGuildByName(name)

    suspend fun createGuild(
        guildProfile: MultipartBody.Part?,
        createGuildRequest: CreateGuildRequest
    ): Flow<Unit> = guildRepository.createGuild(guildProfile, createGuildRequest)

    suspend fun updateGuild(
        guildId: Int,
        guildProfile: MultipartBody.Part?,
        updateGuildRequest: CreateGuildRequest
    ): Flow<Unit> = guildRepository.updateguild(guildId, guildProfile, updateGuildRequest)

    suspend fun myGuilds(): List<GuildResponse> =
        guildRepository.myGuilds()


    suspend fun getGuild(guildId: Int): GuildResponse =
        guildRepository.getGuild(guildId)


    suspend fun applyGuild(guildId: Int): Flow<Unit> =
        guildRepository.applyGuild(guildId)


    suspend fun getGuildPostList(guildId: Int, categoryId: Int): List<GuildPostResponse> =
        guildRepository.getGuildPostList(guildId, categoryId)


    suspend fun createGuildPost(
        images: List<MultipartBody.Part>?,
        createGuildPostRequest: CreateGuildPostRequest
    ): Flow<Unit> = guildRepository.createGuildPost(images, createGuildPostRequest)

    suspend fun getGuildPost(guildPostId: Int): GuildPostDetailResponse =
        guildRepository.getGuildPost(guildPostId)

    suspend fun getGuildMemberList(guildId: Int): List<GuildMemberResponse> =
        guildRepository.getGuildMemberList(guildId)

    suspend fun getGuildApplyList(guildId: Int): List<GuildApplyResponse> =
        guildRepository.getGuildApplyList(guildId)

    suspend fun approveMember(guildId: Int, userId: Int): Flow<Unit> =
        guildRepository.approveMember(guildId, userId)

    suspend fun denyMember(guildId: Int, userId: Int): Flow<Unit> =
        guildRepository.denyMember(guildId, userId)

    suspend fun exileMember(guildId: Int, userId: Int): Flow<Unit> =
        guildRepository.exileMember(guildId, userId)

    suspend fun changeLeader(guildId: Int, newLeaderId: Int): Flow<Unit> =
        guildRepository.changeLeader(guildId, newLeaderId)

    suspend fun quitGuild(guildId: Int): Flow<Unit> = guildRepository.quitGuild(guildId)

    suspend fun getRanking(guildId: Int,type: Char): List<RankingResponse> = guildRepository.getRanking(guildId,type)

}