package com.santeut.data.apiservice

import android.telecom.Call
import com.santeut.data.model.request.PlantIdentificationRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PlantIdApi {
    @Headers("api-key: ZJqq15KzqMMpMnHjGq0JEQy6zCQ2IJ5sKt8nzEoWj6plWhx0tc")
    @POST("v3/identification?details=description")
    suspend fun identifyPlant(@Body request: PlantIdentificationRequest): Response<ResponseBody>
}