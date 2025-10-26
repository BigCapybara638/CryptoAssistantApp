package com.example.cryptoassistant.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoassistant.api.crypronews.CryptoNewsItem
import com.example.cryptoassistant.api.crypronews.CryptoNewsRepository
import com.example.cryptoassistant.api.cryptoprice.CryptoItem
import com.example.cryptoassistant.api.cryptoprice.CryptoRepository
import com.example.cryptoassistant.api.cryptoprice.GlobalStats
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val cryptoRepository = CryptoRepository(application.applicationContext)
    private val newsRepository = CryptoNewsRepository()

    // Состояния для криптовалют
    private val _cryptosState = MutableStateFlow<DataState<List<CryptoItem>>>(DataState.Loading)
    val cryptosState: StateFlow<DataState<List<CryptoItem>>> = _cryptosState

    // Состояния для глобальной статистики
    private val _globalStatsState = MutableStateFlow<DataState<GlobalStats>>(DataState.Loading)
    val globalStatsState: StateFlow<DataState<GlobalStats>> = _globalStatsState

    // Состояния для новостей
    private val _newsState = MutableStateFlow<DataState<List<CryptoNewsItem>>>(DataState.Loading)
    val newsState: StateFlow<DataState<List<CryptoNewsItem>>> = _newsState

    // Общее состояние загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _news = MutableStateFlow<List<CryptoNewsItem>>(emptyList())
    val news: StateFlow<List<CryptoNewsItem>> = _news

    // Общее состояние ошибки
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Флаг для обновления данных
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadAllData()
    }

    fun loadAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Параллельная загрузка всех данных
                val cryptosDeferred = async {
                    loadCryptos()
                }
                val statsDeferred = async {
                    loadGlobalStats()
                }
                val newsDeferred = async {
                    loadNews()
                }

                // Ожидаем завершения всех загрузок
                cryptosDeferred.await()
                statsDeferred.await()
                newsDeferred.await()

                println("✅ Все данные успешно загружены")

            } catch (e: Exception) {
                _error.value = "Ошибка загрузки данных: ${e.message}"
                println("❌ Ошибка загрузки: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null

            try {
                // Параллельное обновление всех данных
                val cryptosDeferred = async { loadCryptos() }
                val statsDeferred = async { loadGlobalStats() }
                val newsDeferred = async { loadNews() }

                cryptosDeferred.await()
                statsDeferred.await()
                newsDeferred.await()

                println("🔄 Данные успешно обновлены")

            } catch (e: Exception) {
                _error.value = "Ошибка обновления: ${e.message}"
                println("❌ Ошибка обновления: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private suspend fun loadCryptos() {
        try {
            _cryptosState.value = DataState.Loading
            val cryptos = cryptoRepository.getTopCryptos(20)
            _cryptosState.value = DataState.Success(cryptos)
            println("✅ Загружено ${cryptos.size} криптовалют")
        } catch (e: Exception) {
            _cryptosState.value = DataState.Error("Ошибка загрузки криптовалют: ${e.message}")
            println("❌ Ошибка загрузки криптовалют: ${e.message}")
        }
    }

    private suspend fun loadGlobalStats() {
        try {
            _globalStatsState.value = DataState.Loading
        } catch (e: Exception) {
            _globalStatsState.value = DataState.Error("Ошибка загрузки статистики: ${e.message}")
            println("❌ Ошибка загрузки статистики: ${e.message}")
        }
    }

    private suspend fun loadNews() {
        try {
            _newsState.value = DataState.Loading
            val news = newsRepository.getCryptoNews(10)
            _newsState.value = DataState.Success(news)
            println("✅ Загружено ${news.size} новостей")
        } catch (e: Exception) {
            _newsState.value = DataState.Error("Ошибка загрузки новостей: ${e.message}")
            println("❌ Ошибка загрузки новостей: ${e.message}")
        }
    }

}

// Универсальное состояние данных
sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}