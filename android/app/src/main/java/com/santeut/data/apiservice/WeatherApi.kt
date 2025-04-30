package com.santeut.data.apiservice

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
@GET("data/3.0/onecall")
suspend fun getWeather(
    @Query("lat") lat: Double,
    @Query("lon") lon: Double,
    @Query("exclude") exclude: String,
    @Query("units") units: String,
    @Query("appid") appid: String): Response<ResponseBody>
}