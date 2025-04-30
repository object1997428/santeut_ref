package com.santeut.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.santeut.data.apiservice.AuthApiService
import com.santeut.data.apiservice.CommonApiService
import com.santeut.data.apiservice.GuildApiService
import com.santeut.data.apiservice.HikingApiService
import com.santeut.data.apiservice.MountainApiService
import com.santeut.data.apiservice.PartyApiService
import com.santeut.data.apiservice.PostApiService
import com.santeut.data.apiservice.UserApiService
import com.santeut.data.util.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideAuthApiService(@Named("retrofit") retrofit: Retrofit) =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun providePostApiService(@Named("retrofit") retrofit: Retrofit) =
        retrofit.create(PostApiService::class.java)

    @Provides
    @Singleton
    fun provideCommonApiService(@Named("retrofit") retrofit: Retrofit) =
        retrofit.create(CommonApiService::class.java)

    @Provides
    @Singleton
    fun provideGuildApiService(@Named("retrofit") retrofit: Retrofit): GuildApiService =
        retrofit.create(GuildApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(@Named("retrofit") retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun providePartyApiService(@Named("retrofit") retrofit: Retrofit): PartyApiService =
        retrofit.create(PartyApiService::class.java)

    @Provides
    @Singleton
    fun providerMountainApiService(@Named("retrofit") retrofit: Retrofit): MountainApiService =
        retrofit.create(MountainApiService::class.java)

    @Provides
    @Singleton
    fun providerHikingApiService(@Named("retrofit") retrofit: Retrofit): HikingApiService =
        retrofit.create(HikingApiService::class.java)

    @Provides
    @Singleton
    @Named("retrofit")
    fun provideRetrofitInstance(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://k10e201.p.ssafy.io")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson)).client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                LocalDateTime.parse(
                    json.asJsonPrimitive.asString,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                )
            })
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonSerializer<LocalDateTime> { src, _, _ ->
                    JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                })
            .setLenient().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    @WebSocketClient
    fun provideWebSocketClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .build()
}