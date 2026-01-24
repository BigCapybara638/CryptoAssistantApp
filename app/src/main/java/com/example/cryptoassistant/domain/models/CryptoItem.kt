package com.example.cryptoassistant.domain.models

import com.google.gson.annotations.SerializedName


data class CryptoItem(
    @SerializedName("id") var id: String,
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
