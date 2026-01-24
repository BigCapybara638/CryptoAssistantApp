package com.example.cryptoassistant.domain.repositories

import com.example.cryptoassistant.domain.models.CryptoNewsItem

interface CryptoNewsRepository {

    suspend fun getCryptoNews(limit: Int = 10): List<CryptoNewsItem>

}