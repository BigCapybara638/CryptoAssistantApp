package com.example.cryptoassistant.api.cryptoprice

import com.example.cryptoassistant.api.RetrofitClient
import java.lang.Math.abs
class CryptoRepository {

    private val apiService = RetrofitClient.coinLoreApiService

    // Получить топ криптовалют
    suspend fun getTopCryptos(limit: Int = 50): List<CryptoItem> {
        return try {
            println("📡 Getting top $limit cryptos from CoinLore...")
            val response = apiService.getTopCryptos()
            println("✅ Success! Received ${response.data.size} cryptos")

            val comparator = compareByDescending<CryptoItem> { kotlin.math.abs((it.percentChange24h).toDouble()) }
            response.data.sortedWith(comparator).take(limit)

        } catch (e: Exception) {
            println("❌ CoinLore API Error: ${e.message}")
            emptyList()
        }
    }

    // Получить глобальную статистику
    suspend fun getGlobalStats(): GlobalStats? {
        return try {
            println("📡 Getting global stats from CoinLore...")
            val response = apiService.getGlobalStats()
            response.firstOrNull()
        } catch (e: Exception) {
            println("❌ Global stats error: ${e.message}")
            null
        }
    }
}