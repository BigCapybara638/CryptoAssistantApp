package com.example.cryptoassistant.data.remote.api

import com.example.cryptoassistant.data.remote.models.CryptoNewsResponse
import retrofit2.http.GET

interface CoinDeskApiService {
    @GET("list?lang=EN&limit=10")
    suspend fun getCryptoNews(): CryptoNewsResponse
}