package com.santeut.data.model.response

import com.google.gson.annotations.SerializedName


data class PartyListResponse(

    @SerializedName("partyList") val partyList: List<PartyResponse>,

)

data class PartyResponse(

    @SerializedName("partyId") val partyId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("place") val place: String,
    @SerializedName("maxPeople") val maxPeople: Int,
    @SerializedName("curPeople") val curPeople: Int,
    @SerializedName("isOwner") val isOwner: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("isMember") val isMember: Boolean

)

data class MyPartyListResponse(

    @SerializedName("partyList") val partyList: List<MyPartyResponse>,

)

data class MyPartyResponse(
    @SerializedName("partyId") val partyId: Int,
    @SerializedName("partyUserId") val partyUserId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("place") val place: String,
    @SerializedName("maxPeople") val maxPeople: Int,
    @SerializedName("curPeople") val curPeople: Int,
    @SerializedName("isOwner") val isOwner: Boolean,
    @SerializedName("status") val status: String,
)

data class MyRecordListResponse(
    @SerializedName("recordList") val recordList: List<MyRecordResponse>
)

data class MyRecordResponse(
    @SerializedName("partyUserId") val partyUserId: Int,
    @SerializedName("partyName") val partyName: String,
    @SerializedName("guildName") val guildName: String,
    @SerializedName("mountainName") val mountainName: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("distance") val distance: Double?,
    @SerializedName("height") val height: Int?,
    @SerializedName("duration") val duration: Int?
)

data class MyScheduleList(
    @SerializedName("date") val date: List<String>
)