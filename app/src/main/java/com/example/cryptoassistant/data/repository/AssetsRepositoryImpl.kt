package com.example.cryptoassistant.data.repository

import com.example.cryptoassistant.data.local.AssetResult
import com.example.cryptoassistant.data.local.AssetsEntity
import com.example.cryptoassistant.data.local.BalanceResult
import com.example.cryptoassistant.domain.repositories.AssetsRepository

//class AssetsRepositoryImpl : AssetsRepository {
//    override suspend fun getAllAssets(): List<AssetResult> {
//        TODO("Not yet implemented")
//    }

//    override suspend fun insertAssets(assets: List<AssetsEntity>) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getBalance(): List<BalanceResult> {
//        TODO("Not yet implemented")
//    }
//
//    suspend fun insertAssets(assets: List<AssetsEntity>) {
//        DatabaseRepository.insertAssets(assets)
//    }
//
//    suspend fun getAssetAll() : List<AssetResult> {
//        val result = DatabaseRepository.getAssetsAll()
//        return result
//    }
//
//    suspend fun getBalance() : List<BalanceResult> {
//        return DatabaseRepository.getBalance()
//    }
//}