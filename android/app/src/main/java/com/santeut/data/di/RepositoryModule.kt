package com.santeut.data.di

import com.santeut.data.repository.AuthRepository
import com.santeut.data.repository.AuthRepositoryImpl
import com.santeut.data.repository.CommonRepository
import com.santeut.data.repository.CommonRepositoryImpl
import com.santeut.data.repository.GuildRepository
import com.santeut.data.repository.GuildRepositoryImpl
import com.santeut.data.repository.HikingRepository
import com.santeut.data.repository.HikingRepositoryImpl
import com.santeut.data.repository.MountainRepository
import com.santeut.data.repository.MountainRepositoryImpl
import com.santeut.data.repository.PartyRepository
import com.santeut.data.repository.PartyRepositoryImpl
import com.santeut.data.repository.PostRepository
import com.santeut.data.repository.PostRepositoryImpl
import com.santeut.data.repository.UserRepository
import com.santeut.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    abstract fun bindCommonRepository(impl: CommonRepositoryImpl): CommonRepository

    @Binds
    abstract fun bindGuildRepository(impl: GuildRepositoryImpl): GuildRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindPartyRepository(impl: PartyRepositoryImpl): PartyRepository

    @Binds
    abstract fun bindMountainRepository(impl: MountainRepositoryImpl): MountainRepository

    @Binds
    abstract fun bindHikingRepository(impl: HikingRepositoryImpl): HikingRepository

}