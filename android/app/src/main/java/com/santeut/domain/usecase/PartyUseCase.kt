package com.santeut.domain.usecase

import com.santeut.data.model.request.CreatePartyRequest
import com.santeut.data.model.request.PartyIdRequest
import com.santeut.data.model.response.ChatRoomInfo
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.data.model.response.PartyResponse
import com.santeut.data.repository.PartyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PartyUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {

    suspend fun getPartyList(
        guildId: Int?,
        name: String?,
        start: String?,
        end: String?
    ): List<PartyResponse> = partyRepository.getPartyList(guildId, name, start, end)

    suspend fun getParty(
        partyId: Int
    ): PartyResponse = partyRepository.getParty(partyId)

    suspend fun getMyPartyList(
        date: String?,
        includeEnd: Boolean,
        page: Int?,
        size: Int?
    ): List<MyPartyResponse> = partyRepository.getMyPartyList(date, includeEnd, page, size)

    suspend fun createParty(createPartyRequest: CreatePartyRequest): Flow<Unit> =
        partyRepository.createParty(createPartyRequest)

    suspend fun deleteParty(partyId: Int): Flow<Unit> = partyRepository.deleteParty(partyId)

    suspend fun joinParty(partyId: Int): Flow<Unit> = partyRepository.joinParty(partyId)

    suspend fun quitParty(partyId: Int): Flow<Unit> = partyRepository.quitParty(partyId)

    suspend fun getMyRecordList(): List<MyRecordResponse> = partyRepository.getMyRecordList()

    suspend fun getMyScheduleList(year: Int, month: Int): List<String> =
        partyRepository.getMyScheduleList(year, month)

    suspend fun getChatRoomList(): List<ChatRoomInfo> = partyRepository.getChatList()

    suspend fun getChatMessageList(
        partyId: Int
    ) = partyRepository.getChatMessageList(partyId)

    suspend fun getSelectedCourseOfParty(
        partyId: Int
    ) = partyRepository.getSelectedCourseOfParty(partyId)

    suspend fun getMyCourse(
        partyUserId: Int
    ) = partyRepository.getMyCourse(partyUserId)
}