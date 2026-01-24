package com.example.cryptoassistant.presentation.ui.home

import android.R
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cryptoassistant.databinding.FragmentCryptoBinding

class CryptoFragment : Fragment() {
    companion object {
        private const val ARG_CRYPTO_NAME = "crypto_name"
        private const val ARG_CRYPTO_SYMBOL = "crypto_symbol"
        private const val ARG_CRYPTO_PRICEUSD = "crypto_priceUsd"
        private const val ARG_CRYPTO_PERCENTCHANGE1H = "crypto_percentChange1h"
        private const val ARG_CRYPTO_PERCENTCHANGE24H = "crypto_percentChange24h"
        private const val ARG_CRYPTO_PERCENTCHANGE7D = "crypto_percentChange7d"
        private const val ARG_CRYPTO_MARKETCAP = "crypto_market_cap"
        private const val ARG_CRYPTO_VOLUME24 = "crypto_volume24"
        private const val ARG_CRYPTO_CSUPPLY = "crypto_csupply"
        private const val ARG_CRYPTO_TSUPPLY = "crypto_tsupply"
        private const val ARG_CRYPTO_MSUPPLY = "crypto_msupply"

    }

    private var _binding: FragmentCryptoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCryptoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // получение данных из HomeFragment
        val nameCrypto = arguments?.getString(ARG_CRYPTO_NAME) ?: "Нет данных"
        val symbolCrypto = arguments?.getString(ARG_CRYPTO_SYMBOL) ?: "Нет данных"
        val priceUsdCrypto = arguments?.getString(ARG_CRYPTO_PRICEUSD) ?: "Нет данных"
        val price1h = arguments?.getString(ARG_CRYPTO_PERCENTCHANGE1H) ?: "Нет данных"
        val price24h = arguments?.getString(ARG_CRYPTO_PERCENTCHANGE24H) ?: "Нет данных"
        val price7d = arguments?.getString(ARG_CRYPTO_PERCENTCHANGE7D) ?: "Нет данных"
        val marketCap = arguments?.getString(ARG_CRYPTO_MARKETCAP) ?: "Нет данных"
        val volume24 = arguments?.getString(ARG_CRYPTO_VOLUME24) ?: "Нет данных"
        val cSupply = arguments?.getString(ARG_CRYPTO_CSUPPLY) ?: "Нет данных"
        val tSupply = arguments?.getString(ARG_CRYPTO_TSUPPLY) ?: "Нет данных"
        val mSupply = arguments?.getString(ARG_CRYPTO_MSUPPLY) ?: "Нет данных"

        // установка текста в AppBar
        (activity as AppCompatActivity).supportActionBar?.title = nameCrypto

        // работа с ценой
        val change = price24h.toDoubleOrNull() ?: 0.0
        val changeText = if (change >= 0) {
            "+${String.format("%.2f", change)}%"
        }
        else {
            "${String.format("%.2f", change)}%"
        }

        // работа с графиком
        val priceHour = priceUsdCrypto.toDouble() + priceUsdCrypto.toDouble() * (price1h.toDouble() / 100)
        val priceDay = priceUsdCrypto.toDouble() + priceUsdCrypto.toDouble() * (price24h.toDouble() / 100)
        val priceWeek = priceUsdCrypto.toDouble() + priceUsdCrypto.toDouble() * (price7d.toDouble() / 100)
        val sampleData = listOf(priceWeek, priceDay, priceHour)
        binding.lineChart.setData(sampleData)

        // работа с балансом
        binding.balanceTextView.text = "Ваш баланс в ${symbolCrypto}"

        // основной блок
        binding.cryptoFullName.text = nameCrypto
        binding.cryptoIndex.text = symbolCrypto
        binding.cryptoPrice.text = "$${priceUsdCrypto}"
        binding.cryptoChange.text = changeText

        // блок "обзор"
        binding.mSupply.text = "${mSupply} ${symbolCrypto}"
        binding.cSupply.text = "${cSupply} ${symbolCrypto}"
        binding.tSupply.text = "${tSupply} ${symbolCrypto}"
        binding.marketCapUsd.text = "$${marketCap}"
        binding.volume24.text = "$${volume24}"

        // работа с процентом изменения цены
        if (change >= 0) {
            binding.cryptoChange.setBackgroundColor(
                ContextCompat.getColor(
                requireContext(),
                R.color.holo_green_light))

            binding.cryptoChange.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                R.color.holo_green_dark))

        }
        else {
            binding.cryptoChange.setBackgroundColor(
                ContextCompat.getColor(
                requireContext(),
                R.color.holo_red_light))

            binding.cryptoChange.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                R.color.holo_red_dark))

        }

        // для темной темы
        if (isSystemInDarkTheme(requireContext())) {
            binding.cryptoIndex.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                com.example.cryptoassistant.R.color.purple_500))

            binding.cryptoFullName.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                com.example.cryptoassistant.R.color.boldTextNightTheme))

            binding.marketCapUsd.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                com.example.cryptoassistant.R.color.boldTextNightTheme))

            binding.volume24.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                com.example.cryptoassistant.R.color.boldTextNightTheme))

            binding.tSupply.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                com.example.cryptoassistant.R.color.boldTextNightTheme))

            binding.cSupply.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                com.example.cryptoassistant.R.color.boldTextNightTheme))

            binding.mSupply.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                com.example.cryptoassistant.R.color.boldTextNightTheme))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isSystemInDarkTheme(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}