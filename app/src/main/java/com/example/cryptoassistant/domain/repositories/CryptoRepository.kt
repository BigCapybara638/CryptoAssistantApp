package com.example.cryptoassistant.domain.repositories

import com.example.cryptoassistant.domain.models.CryptoItem

interface CryptoRepository {

    suspend fun getTopCryptos(limit: Int = 50) : List<CryptoItem>

}