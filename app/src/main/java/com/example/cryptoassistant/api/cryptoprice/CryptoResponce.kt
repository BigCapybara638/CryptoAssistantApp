package com.example.cryptoassistant.api.cryptoprice

import com.example.cryptoassistant.domain.models.CryptoItem
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
