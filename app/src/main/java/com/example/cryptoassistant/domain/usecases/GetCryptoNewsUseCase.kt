package com.example.cryptoassistant.domain.usecases

import com.example.cryptoassistant.domain.models.CryptoNewsItem
import com.example.cryptoassistant.domain.repositories.CryptoNewsRepository

class GetCryptoNewsUseCase(
    private val cryptoNewsRepository: CryptoNewsRepository
) {
    suspend operator fun invoke(
        limit: Int = 10
    ) : List<CryptoNewsItem> {
        return cryptoNewsRepository.getCryptoNews(limit)
    }

}