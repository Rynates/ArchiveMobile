package com.example.helper.di

import android.content.SharedPreferences
import com.example.helper.data.remote.KtorApi
import com.example.helper.data.repository.MapsRepositoryImpl
import com.example.helper.domen.repository.MapsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {

//    @Binds
//    fun bindMapImpl_to_MappRepository(
//        mapsRepositoryImpl: MapsRepositoryImpl
//    ): MapsRepository

    @Provides
    @Singleton
    fun provideMapRepository(ktorApi: KtorApi,prefs:SharedPreferences):MapsRepository{
        return MapsRepositoryImpl(ktorApi = ktorApi, prefs = prefs)
    }
}
