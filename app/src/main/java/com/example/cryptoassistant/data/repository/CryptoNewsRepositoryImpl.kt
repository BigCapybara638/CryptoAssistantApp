package com.example.cryptoassistant.data.repository

import com.example.cryptoassistant.data.remote.RetrofitClient
import com.example.cryptoassistant.data.remote.models.SourceData
import com.example.cryptoassistant.domain.models.CryptoNewsItem
import com.example.cryptoassistant.domain.repositories.CryptoNewsRepository

class CryptoNewsRepositoryImpl : CryptoNewsRepository {

    private val apiService = RetrofitClient.coinDeskApiService

    override suspend fun getCryptoNews(limit: Int): List<CryptoNewsItem> {
        return try {
            val response = apiService.getCryptoNews()
            response.data.take(limit)

        } catch (e: Exception) {
            println("❌ All news sources failed, using mock data")
            getMockNews(limit)
        }
    }

    private fun getMockNews(limit: Int): List<CryptoNewsItem> {
        return listOf(
            CryptoNewsItem(
                id = 1,
                publishedOn = System.currentTimeMillis() / 1000,
                imageUrl = "https://example.com/image1.jpg",
                title = "Bitcoin reaches new all-time high",
                url = "https://example.com/news/1",
                body = "Bitcoin price continues to grow...",
                sourceData = SourceData("Crypto News", "RSS")
            ),
            CryptoNewsItem(
                id = 2,
                publishedOn = System.currentTimeMillis() / 1000 - 3600,
                imageUrl = "https://example.com/image2.jpg",
                title = "Ethereum 2.0 update released",
                url = "https://example.com/news/2",
                body = "Major update for Ethereum network...",
                sourceData = SourceData("Crypto Daily", "RSS")
            ),
            CryptoNewsItem(
                id = 2,
                publishedOn = System.currentTimeMillis() / 1000 - 3600,
                imageUrl = "https://example.com/image2.jpg",
                title = "Ethereum 2.0 update released",
                url = "https://example.com/news/2",
                body = "Major update for Ethereum network...",
                sourceData = SourceData("Crypto Daily", "RSS")
            ),
            CryptoNewsItem(
                id = 2,
                publishedOn = System.currentTimeMillis() / 1000 - 3600,
                imageUrl = "https://example.com/image2.jpg",
                title = "Ethereum 2.0 update released",
                url = "https://example.com/news/2",
                body = "Major update for Ethereum network...",
                sourceData = SourceData("Crypto Daily", "RSS")
            ),
            CryptoNewsItem(
                id = 2,
                publishedOn = System.currentTimeMillis() / 1000 - 3600,
                imageUrl = "https://example.com/image2.jpg",
                title = "Ethereum 2.0 update released",
                url = "https://example.com/news/2",
                body = "Major update for Ethereum network...",
                sourceData = SourceData("Crypto Daily", "RSS")
            ),
            CryptoNewsItem(
                id = 2,
                publishedOn = System.currentTimeMillis() / 1000 - 3600,
                imageUrl = "https://example.com/image2.jpg",
                title = "Ethereum 2.0 update released",
                url = "https://example.com/news/2",
                body = "Major update for Ethereum network...",
                sourceData = SourceData("Crypto Daily", "RSS")
            )
        ).take(limit)
    }
}