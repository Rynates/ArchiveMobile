package com.example.helper.di

import android.content.SharedPreferences
import com.example.helper.data.remote.KtorApi
import com.example.helper.data.repository.FamilyRepositoryImpl
import com.example.helper.domen.repository.FamilyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FamilyModule {

    @Singleton
    @Provides
    fun provideFamilyRepo(ktorApi: KtorApi,prefs:SharedPreferences): FamilyRepository {
        return FamilyRepositoryImpl(ktorApi = ktorApi,prefs = prefs)
    }
}