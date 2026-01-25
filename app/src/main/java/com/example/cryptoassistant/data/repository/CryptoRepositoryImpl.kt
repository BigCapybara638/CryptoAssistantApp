package com.example.cryptoassistant.data.repository

import com.example.cryptoassistant.data.local.DatabaseRepository
import com.example.cryptoassistant.data.remote.RetrofitClient
import com.example.cryptoassistant.data.remote.api.CoinLoreApiService
import com.example.cryptoassistant.domain.models.CryptoItem
import com.example.cryptoassistant.domain.repositories.CryptoRepository
import kotlin.math.abs

class CryptoRepositoryImpl(
    private val databaseRepository: DatabaseRepository
) : CryptoRepository {

    private val apiService = RetrofitClient.coinLoreApiService

    // Получить топ криптовалют
    override suspend fun getTopCryptos(limit: Int): List<CryptoItem> {
        return try {
            val response = apiService.getTopCryptos()

            val comparator =
                compareByDescending<CryptoItem> {
                    abs((it.percentChange24h).toDouble())
                }

            databaseRepository.updateCurrency(response.data)
            response.data.sortedWith(comparator).take(limit)

        } catch (e: Exception) {
            println("❌ CoinLore API Error: ${e.message}")
            return databaseRepository.getCurrencyFromDatabase(limit)
        }
    }
}
