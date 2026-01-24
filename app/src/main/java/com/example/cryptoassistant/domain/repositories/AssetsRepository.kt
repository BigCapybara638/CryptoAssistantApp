package com.example.cryptoassistant.domain.repositories

import com.example.cryptoassistant.api.data.AssetResult
import com.example.cryptoassistant.api.data.AssetsEntity
import com.example.cryptoassistant.api.data.BalanceResult

interface AssetsRepository {

    suspend fun getAllAssets() : List<AssetResult>

    suspend fun insertAssets(assets: List<AssetsEntity>)

    suspend fun getBalance() : List<BalanceResult>

}