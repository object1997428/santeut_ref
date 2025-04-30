package com.santeut.data.model.request

data class CreateGuildPostRequest(

    val guildId: Int,
    val categoryId: Int,
    val guildPostTitle: String,
    val guildPostContent: String

)

data class CreateGuildRequest(

    val guildName: String,
    val guildInfo: String,
    val guildIsPrivate: Boolean,
    val regionId: Int,
    val guildGender: Char,
    val guildMinAge: Int,
    val guildMaxAge: Int

)