package com.example.cryptoassistant.api.cryptoprice

import com.example.cryptoassistant.domain.models.CryptoItem
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinLoreApiService {

    // Получить топ криптовалют
    @GET("tickers/")
    suspend fun getTopCryptos(): CryptoResponse

    // Получить конкретную криптовалюту по ID
    @GET("ticker/?id={id}")
    suspend fun getCryptoById(@Path("id") id: String): List<CryptoItem>

}