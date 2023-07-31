package com.example.helper.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.helper.data.remote.KtorApi
import com.example.helper.data.repository.SignUpRepositoryImpl
import com.example.helper.domen.repository.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignUpModule {


    @Singleton
    @Provides
    fun provide_SingUpRepository(
        ktorApi: KtorApi
    ):SignUpRepository{
        return SignUpRepositoryImpl(ktorApi)
    }

}