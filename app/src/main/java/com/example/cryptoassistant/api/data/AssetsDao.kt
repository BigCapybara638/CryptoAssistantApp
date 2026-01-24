package com.example.cryptoassistant.api.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cryptoassistant.api.data.AssetsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetsDao {

    // Методы для Assets
    @Query("SELECT * FROM assets")
    fun getAllAssets(): Flow<List<AssetsEntity>>

    @Query("SELECT * FROM assets LIMIT :limit")
    suspend fun getAssets(limit: Int): List<AssetsEntity>

    @Query("SELECT * FROM assets WHERE idCrypto = :assetId")
    suspend fun getAssetById(assetId: String): AssetsEntity?

    @Query("""SELECT cryptoCurrency.*, SUM(assets.amount * assets.price)/SUM(assets.amount) as price, SUM(assets.amount) as amount FROM cryptoCurrency JOIN assets ON cryptoCurrency.symbol = assets.idCrypto GROUP BY name""")
    suspend fun getAssetsAll() : List<AssetResult>

    @Query("SELECT assets.amount as count, assets.price as price, cryptocurrency.priceUsd as newCurrent FROM assets JOIN cryptoCurrency ON assets.idCrypto = cryptoCurrency.symbol")
    suspend fun getBalance() : List<BalanceResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(assets: List<AssetsEntity>)

    @Query("DELETE FROM assets")
    suspend fun deleteAllAssets()

    @Query("SELECT COUNT(*) FROM assets")
    suspend fun getAssetsCount(): Int

    // Методы для CryptoCurrency
    @Query("SELECT * FROM cryptoCurrency LIMIT :limit")
    suspend fun getTopCryptos(limit: Int): List<CryptoCurrencyEntity>

    @Query("SELECT * FROM cryptoCurrency WHERE id = :id")
    suspend fun getCryptoById(id: String): CryptoCurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptos(cryptos: List<CryptoCurrencyEntity>)

    @Query("DELETE FROM cryptoCurrency")
    suspend fun deleteAllCryptos()

    @Query("SELECT COUNT(*) FROM cryptoCurrency")
    suspend fun getCryptosCount(): Int
}