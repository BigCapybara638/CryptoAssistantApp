package com.example.cryptoassistant.di

import android.content.Context
import com.example.cryptoassistant.data.local.AssetsDao
import com.example.cryptoassistant.data.local.CryptoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CryptoDatabase {
        return CryptoDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAssetsDao(database: CryptoDatabase): AssetsDao {
        return database.cryptoAssetDao()
    }

}