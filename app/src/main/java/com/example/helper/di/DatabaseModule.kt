package com.example.helper.di

import android.content.Context
import androidx.room.Room
import com.example.helper.data.locale.FundDatabase
import com.example.helper.data.locale.GeofenceDatabase
import com.example.helper.data.locale.InventoryDatabase
import com.example.helper.utils.Constants.DATABASE_NAME
import com.example.helper.utils.Constants.FUND_DATABASE_NAME
import com.example.helper.utils.Constants.INVENTORY_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        GeofenceDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(database: GeofenceDatabase) = database.geofenceDao()

}

@Module
@InstallIn(SingletonComponent::class)
object FundsDatabaseModule{

    @Singleton
    @Provides
    fun provideFunDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        FundDatabase::class.java,
    FUND_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideFundDao(database: FundDatabase) = database.fundsDao()


    @Singleton
    @Provides
    fun provideInventoryDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        InventoryDatabase::class.java,
        INVENTORY_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideInventoryDao(database: InventoryDatabase) = database.inventoryDao()
}