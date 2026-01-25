package com.example.cryptoassistant.presentation.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoassistant.data.repository.CryptoNewsRepositoryImpl
import com.example.cryptoassistant.data.repository.CryptoRepositoryImpl
import com.example.cryptoassistant.domain.models.CryptoItem
import com.example.cryptoassistant.domain.models.CryptoNewsItem
import com.example.cryptoassistant.domain.usecases.GetCryptoNewsUseCase
import com.example.cryptoassistant.domain.usecases.GetCryptoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCryptoUseCase: GetCryptoUseCase
) : ViewModel() {

    private val newsRepository = CryptoNewsRepositoryImpl()

    private val getCryptoNewsUseCase =
        GetCryptoNewsUseCase(newsRepository)


    // состояния для криптовалют
    private val _cryptosState = MutableStateFlow<DataState<List<CryptoItem>>>(DataState.Loading)
    val cryptosState: StateFlow<DataState<List<CryptoItem>>> = _cryptosState

    // состояния для новостей
    private val _newsState = MutableStateFlow<DataState<List<CryptoNewsItem>>>(DataState.Loading)
    val newsState: StateFlow<DataState<List<CryptoNewsItem>>> = _newsState

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
                // параллельная загрузка всех данных
                val cryptosDeferred = async {
                    loadCryptos()
                }
                val newsDeferred = async {
                    loadNews()
                }

                // ожидаем завершения всех загрузок
                cryptosDeferred.await()
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

    // загрузка криптовалют
    private suspend fun loadCryptos() {
        try {
            _cryptosState.value = DataState.Loading
            val cryptos = getCryptoUseCase(20)
            _cryptosState.value = DataState.Success(cryptos)
            println("✅ Загружено ${cryptos.size} криптовалют")
        } catch (e: Exception) {
            _cryptosState.value = DataState.Error("Ошибка загрузки криптовалют: ${e.message}")
            println("❌ Ошибка загрузки криптовалют: ${e.message}")
        }
    }

    // загрузка новостей
    private suspend fun loadNews() {
        try {
            _newsState.value = DataState.Loading
            val news = getCryptoNewsUseCase(10)
            _newsState.value = DataState.Success(news)
            println("✅ Загружено ${news.size} новостей")
        } catch (e: Exception) {
            _newsState.value = DataState.Error("Ошибка загрузки новостей: ${e.message}")
            println("❌ Ошибка загрузки новостей: ${e.message}")
        }
    }
}

// универсальное состояние данных
sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}