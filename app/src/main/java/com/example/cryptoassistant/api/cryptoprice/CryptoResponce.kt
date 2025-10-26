package com.example.cryptoassistant.api.cryptoprice

import com.google.gson.annotations.SerializedName


// Ответ для списка криптовалют
data class CryptoResponse(
    @SerializedName("data") val data: List<CryptoItem>,
    @SerializedName("info") val info: Info
)

data class Info(
    @SerializedName("coins_num") val coinsCount: Int,
    @SerializedName("time") val time: Long
)

// Модель криптовалюты - исправленные поля
data class CryptoItem(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("nameid") val nameId: String,
    @SerializedName("rank") val rank: Int,
    @SerializedName("price_usd") val priceUsd: String,
    @SerializedName("percent_change_24h") val percentChange24h: String,
    @SerializedName("percent_change_1h") val percentChange1h: String,
    @SerializedName("percent_change_7d") val percentChange7d: String,
    @SerializedName("market_cap_usd") val marketCapUsd: String, // рыночая капитализация
    @SerializedName("volume24") val volume24: Double,   // объем торгов
    @SerializedName("csupply") val circulatingSupply: String? = null, // текущее количество монет в обращении
    @SerializedName("tsupply") val tSupply: String? = null, // общее текущее предложение
    @SerializedName("msupply") val mSupply: String? = null, // максимальное  предложение

)

// Глобальная статистика
data class GlobalStats(
    @SerializedName("coins_count") val coinsCount: Int,
    @SerializedName("active_markets") val activeMarkets: Int,
    @SerializedName("total_mcap") val totalMarketCap: Double,
    @SerializedName("total_volume") val totalVolume: Double,
    @SerializedName("btc_d") val btcDominance: String,
    @SerializedName("eth_d") val ethDominance: String
)