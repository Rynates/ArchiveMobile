package com.example.helper.di

import android.content.SharedPreferences
import com.example.helper.data.remote.KtorApi
import com.example.helper.data.repository.UserRepositoryImpl
import com.example.helper.domen.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        ktorApi: KtorApi,
        sharedPreferences: SharedPreferences
    ): UserRepository{
        return UserRepositoryImpl(ktorApi = ktorApi, prefs = sharedPreferences)
    }
}