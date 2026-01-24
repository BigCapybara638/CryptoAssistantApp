package com.example.cryptoassistant.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoassistant.api.cryptoprice.CryptoRepositoryImpl
import com.example.cryptoassistant.api.data.AssetResult
import com.example.cryptoassistant.api.data.AssetsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val dashboardCryptoRepositoryImpl = CryptoRepositoryImpl(application.applicationContext)

    // состояния для криптовалют
    private val _cryptosState = MutableStateFlow<DataState<List<AssetResult>>>(DataState.Loading)
    val cryptosState: StateFlow<DataState<List<AssetResult>>> = _cryptosState

    private val _balance = MutableStateFlow<Double?>(null)
    val balance: StateFlow<Double?> = _balance.asStateFlow()

    private val _balanceChange = MutableStateFlow<Double?>(null)
    val balanceChange: StateFlow<Double?> = _balanceChange.asStateFlow()

    // общее состояние загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // общее состояние ошибки
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    init {
        loadAllData()
    }

    // вся загрузка
    fun loadAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                println("🔄 Starting data loading...")
                loadCryptos()
                getBalance()
                println("✅ Data loading completed")

            } catch (e: Exception) {
                println("❌ Error loading data: ${e.message}")
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // загрузка криптовалют
    private suspend fun loadCryptos() {
        try {
            _cryptosState.value = DataState.Loading
            println("📥 Loading cryptos from API...")

            val cryptos = dashboardCryptoRepositoryImpl.getAssetAll()
            println("📊 Received ${cryptos.size} cryptos from API")

//            cryptos.forEach { crypto ->
//                println("   - ${crypto.name}: ${crypto.priceUsd}")
//            }

            _cryptosState.value = DataState.Success(cryptos)

        } catch (e: Exception) {
            println("❌ Crypto loading error: ${e.message}")
            _cryptosState.value = DataState.Error("Ошибка: ${e.message}")
        }
    }

    fun insertAssets(assets: List<AssetsEntity>) {
        viewModelScope.launch {
            dashboardCryptoRepositoryImpl.insertAssets(assets)
        }
    }

    fun getBalance() {
        viewModelScope.launch {
            val listBalance = dashboardCryptoRepositoryImpl.getBalance()
            var oldBalance = 0.0
            var newBalance = 0.0

            for (item in listBalance) {
                oldBalance += item.count * item.price
                newBalance += item.count * item.newCurrent.toDouble()
            }


            _balanceChange.value = newBalance - oldBalance
            _balance.value = newBalance
        }
    }

//    suspend fun getAmountAsset(assetId: String) : AssetsEntity? {
//        viewModelScope.launch {
//            val result = dashboardCryptoRepository.getAssetById(assetId)
//            return result
//        }
//    }
}

// универсальное состояние данных
sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}