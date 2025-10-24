package com.example.cryptoassistant.api.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetsEntity(
    @PrimaryKey val idCrypto: Long,
    val amount: Double,
    val price: Double,
)