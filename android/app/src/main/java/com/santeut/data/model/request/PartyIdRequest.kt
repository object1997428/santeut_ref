package com.santeut.data.model.request

import com.google.gson.annotations.SerializedName

data class PartyIdRequest(
    @SerializedName("partyId") val partyId: Int
)
