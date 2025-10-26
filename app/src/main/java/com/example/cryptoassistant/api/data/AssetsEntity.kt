package com.example.cryptoassistant.api.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetsEntity(
    @PrimaryKey val idCrypto: String,
    val amount: Double,
    val price: Double,
)

@Entity(tableName = "cryptoCurrency")
data class CryptoCurrencyEntity(
    @PrimaryKey
    val id: String = "",
    val symbol: String = "",
    val name: String = "",
    val nameId: String = "",
    val rank: Int = 0,
    val priceUsd: String = "",
    val percentChange24h: String = "",
    val percentChange1h: String = "",
    val percentChange7d: String = "",
    val marketCapUsd: String = "",
    val volume24: Double = 0.0,
    val circulatingSupply: String? = null,
    val tSupply: String? = null,
    val mSupply: String? = null
) {
    constructor() : this("", "", "", "", 0, "", "", "", "", "", 0.0, "", "", "")
}

