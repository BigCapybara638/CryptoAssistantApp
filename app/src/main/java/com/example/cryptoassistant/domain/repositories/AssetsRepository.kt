package com.example.cryptoassistant.domain.repositories

import com.example.cryptoassistant.data.local.AssetResult
import com.example.cryptoassistant.data.local.AssetsEntity
import com.example.cryptoassistant.data.local.BalanceResult

interface AssetsRepository {

    suspend fun getAllAssets() : List<AssetResult>

    suspend fun insertAssets(assets: List<AssetsEntity>)

    suspend fun getBalance() : List<BalanceResult>

}