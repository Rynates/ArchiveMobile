package com.example.helper.di

import android.content.SharedPreferences
import com.example.helper.data.remote.KtorApi
import com.example.helper.data.repository.ArchivesRepositoryImpl
import com.example.helper.domen.repository.ArchivesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArchiveModule {

   @Provides
   @Singleton
   fun provideArchiveRepository(
       ktorApi: KtorApi,
       prefs:SharedPreferences,
       @IoDispatcher
       dispatcher: CoroutineDispatcher
   ):ArchivesRepository{
       return ArchivesRepositoryImpl(ktorApi = ktorApi,prefs = prefs, coroutineDispatcher = dispatcher)
   }
}