package com.example.cryptoassistant.data.local

import android.content.Context
import com.example.cryptoassistant.domain.models.CryptoItem


class DatabaseRepository(context: Context) {

    private val database = CryptoDatabase.getInstance(context)
    private val dao = database.cryptoAssetDao()

    suspend fun updateCurrency(list: List<CryptoItem>) {
        try {
            val entities = list.map { it.toCryptoCurrencyEntity() }
            dao.insertCryptos(entities)

        } catch (e: Exception) {
            println("❌ Error updating currency: ${e.message}")
            e.printStackTrace()
        }
    }

    suspend fun getCurrencyFromDatabase(limit: Int): List<CryptoItem> {
        return try {
            val count = dao.getCryptosCount()
            println("💾 Total records in database: $count")

            val cryptos = dao.getTopCryptos(limit)
            println("💾 Retrieved ${cryptos.size} cryptos from database")

            val cryptoItems = cryptos.map { it.toCryptoItem() }
            println("✅ Data loaded from cache: ${cryptoItems.size} items")
            cryptoItems

        } catch (e: Exception) {
            println("❌ Error loading from cache: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAssetsAll() : List<AssetResult> {
        val result = dao.getAssetsAll()
        return result
    }

    suspend fun insertAssets(assets: List<AssetsEntity>) {
        dao.insertAssets(assets)
    }

    suspend fun getBalance() : List<BalanceResult> {
        return dao.getBalance()
    }
}


// Extension functions для конвертации между Entity и CharacterItem
private fun CryptoItem.toCryptoCurrencyEntity(): CryptoCurrencyEntity {
    return CryptoCurrencyEntity(
        id = this.id,
        symbol = this.symbol,
        name = this.name,
        nameId = this.nameId,
        rank = this.rank,
        priceUsd = this.priceUsd,
        percentChange24h = this.percentChange24h,
        percentChange1h = this.percentChange1h,
        percentChange7d = this.percentChange7d,
        marketCapUsd = this.marketCapUsd,
        volume24 = this.volume24,
        circulatingSupply = this.circulatingSupply,
        tSupply = this.tSupply,
        mSupply = this.mSupply
    )
}

private fun CryptoCurrencyEntity.toCryptoItem(): CryptoItem {
    return CryptoItem(
        id = this.id,
        symbol = this.symbol,
        name = this.name,
        nameId = this.nameId,
        rank = this.rank,
        priceUsd = this.priceUsd,
        percentChange24h = this.percentChange24h,
        percentChange1h = this.percentChange1h,
        percentChange7d = this.percentChange7d,
        marketCapUsd = this.marketCapUsd,
        volume24 = this.volume24,
        circulatingSupply = this.circulatingSupply,
        tSupply = this.tSupply,
        mSupply = this.mSupply
    )
}