package com.example.cryptoassistant.data.repository

import android.content.Context
import com.example.cryptoassistant.data.remote.RetrofitClient
import com.example.cryptoassistant.data.local.AssetResult
import com.example.cryptoassistant.data.local.AssetsEntity
import com.example.cryptoassistant.data.local.BalanceResult
import com.example.cryptoassistant.data.local.DatabaseRepository
import com.example.cryptoassistant.domain.models.CryptoItem
import com.example.cryptoassistant.domain.repositories.CryptoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.abs

class CryptoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CryptoRepository {

    private val apiService = RetrofitClient.coinLoreApiService
    private val DatabaseRepository = DatabaseRepository(context)


    // Получить топ криптовалют
    override suspend fun getTopCryptos(limit: Int): List<CryptoItem> {
        return try {
            println("📡 Getting top $limit cryptos from CoinLore...")
            val response = apiService.getTopCryptos()
            println("✅ Success! Received ${response.data.size} cryptos")

            val comparator =
                compareByDescending<CryptoItem> {
                    abs((it.percentChange24h).toDouble())
                }
            DatabaseRepository.updateCurrency(response.data)
            response.data.sortedWith(comparator).take(limit)

        } catch (e: Exception) {
            println("❌ CoinLore API Error: ${e.message}")
            val cache = DatabaseRepository.getCurrencyFromDatabase(limit)
            return cache

        }
    }
}
