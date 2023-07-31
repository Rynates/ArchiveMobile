package com.example.helper.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.helper.data.remote.KtorApi
import com.example.helper.data.repository.LoginRepositoryImpl
import com.example.helper.domen.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {


    @Singleton
    @Provides
    fun provideSharedPref(applicationContext: Application):SharedPreferences{
        return applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provide_LoginForumRepository(
        ktorApi: KtorApi,
        sharedPreferences: SharedPreferences
    ): LoginRepository {
        return LoginRepositoryImpl(
            ktorApi = ktorApi,
            prefs = sharedPreferences
        )
    }
}