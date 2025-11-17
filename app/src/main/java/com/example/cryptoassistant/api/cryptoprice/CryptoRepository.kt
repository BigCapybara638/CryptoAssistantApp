package com.example.cryptoassistant.api.cryptoprice

import android.content.Context
import com.example.cryptoassistant.api.RetrofitClient
import com.example.cryptoassistant.api.data.AssetResult
import com.example.cryptoassistant.api.data.AssetsEntity
import com.example.cryptoassistant.api.data.BalanceResult
import com.example.cryptoassistant.api.data.DatabaseRepository
import java.lang.Math.abs
class CryptoRepository(context: Context) {

    private val apiService = RetrofitClient.coinLoreApiService
    private val DatabaseRepository = DatabaseRepository(context)


    // Получить топ криптовалют
    suspend fun getTopCryptos(limit: Int = 50): List<CryptoItem> {
        return try {
            println("📡 Getting top $limit cryptos from CoinLore...")
            val response = apiService.getTopCryptos()
            println("✅ Success! Received ${response.data.size} cryptos")

            val comparator =
                compareByDescending<CryptoItem> { kotlin.math.abs((it.percentChange24h).toDouble())
                }
            DatabaseRepository.updateCurrency(response.data)
            response.data.sortedWith(comparator).take(limit)

        } catch (e: Exception) {
            println("❌ CoinLore API Error: ${e.message}")
            val cache = DatabaseRepository.getCurrencyFromDatabase(limit)
            return cache
        // emptyList()


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

    suspend fun insertAssets(assets: List<AssetsEntity>) {
        DatabaseRepository.insertAssets(assets)
    }

    suspend fun getAssetAll() : List<AssetResult> {
        val result = DatabaseRepository.getAssetsAll()
        return result
    }

    suspend fun getBalance() : List<BalanceResult> {
        return DatabaseRepository.getBalance()
    }
}