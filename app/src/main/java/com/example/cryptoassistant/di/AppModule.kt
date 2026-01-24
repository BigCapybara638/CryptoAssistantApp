package com.example.cryptoassistant.di

import android.content.Context
import com.example.cryptoassistant.data.repository.CryptoNewsRepositoryImpl
import com.example.cryptoassistant.data.repository.CryptoRepositoryImpl
import com.example.cryptoassistant.domain.repositories.CryptoNewsRepository
import com.example.cryptoassistant.domain.repositories.CryptoRepository
import com.example.cryptoassistant.domain.usecases.GetCryptoNewsUseCase
import com.example.cryptoassistant.domain.usecases.GetCryptoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn (ViewModelComponent::class)
object AppModule {

    @Provides
    @ViewModelScoped
    fun provideCryptoRepository(
        @ApplicationContext context: Context
    ) : CryptoRepository {
        return CryptoRepositoryImpl(context)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCryptoUseCase(
        repository: CryptoRepository
    ) : GetCryptoUseCase {
        return GetCryptoUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideCryptoNewsRepository() : CryptoNewsRepository {
        return CryptoNewsRepositoryImpl()
    }

    @Provides
    @ViewModelScoped
    fun provideGetCryptoNewsUseCase(
        repository: CryptoNewsRepository
    ) : GetCryptoNewsUseCase {
        return GetCryptoNewsUseCase(repository)
    }

}