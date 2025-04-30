package com.santeut.data.repository

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.request.PartyIdRequest
import com.santeut.data.model.response.ChatMessage
import com.santeut.data.model.response.ChatRoomInfo
import com.santeut.data.model.response.MyCourseResponse
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.data.model.response.PartyCourseResponse
import com.santeut.data.model.response.PartyResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PartyRepository {

    suspend fun getPartyList(
        guildId: Int?,
        name: String?,
        start: String?,
        end: String?
    ): List<PartyResponse>

    suspend fun getParty(
        partyId: Int
    ): PartyResponse

    suspend fun getMyPartyList(
        date: String?,
        includeEnd: Boolean,
        page: Int?,
        size: Int?
    ): List<MyPartyResponse>

    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit>

    suspend fun deleteParty(partyId: Int): Flow<Unit>

    suspend fun joinParty(partyId: Int): Flow<Unit>

    suspend fun quitParty(partyId: Int): Flow<Unit>

    suspend fun getMyRecordList(): List<MyRecordResponse>

    suspend fun getMyScheduleList(year: Int, month: Int): List<String>

    suspend fun getChatList(): List<ChatRoomInfo>

    suspend fun getChatMessageList(partyId: Int): MutableList<ChatMessage>

    suspend fun getSelectedCourseOfParty(partyId: Int) : PartyCourseResponse

    suspend fun getMyCourse(partyUserId: Int) : MyCourseResponse
}