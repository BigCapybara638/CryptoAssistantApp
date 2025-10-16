package com.example.cryptoassistant.ui.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoassistant.CryptoChartView
import com.example.cryptoassistant.R
import com.example.cryptoassistant.databinding.FragmentCryptoBinding
import com.example.cryptoassistant.databinding.FragmentDashboardBinding
import com.example.cryptoassistant.ui.dashboard.DashboardViewModel


class CryptoFragment : Fragment() {
    private var _binding: FragmentCryptoBinding? = null
    private lateinit var chartView: CryptoChartView

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        val nameCrypto = arguments?.getString("crypto_name") ?: "Нет данных"
        val symbolCrypto = arguments?.getString("crypto_symbol") ?: "Нет данных"
        val priceUsdCrypto = arguments?.getString("crypto_priceUsd") ?: "Нет данных"
        val price1h = arguments?.getString("crypto_percentChange1h") ?: "Нет данных"
        val price24h = arguments?.getString("crypto_percentChange24h") ?: "Нет данных"
        val price7d = arguments?.getString("crypto_percentChange7d") ?: "Нет данных"

        val marketCap = arguments?.getString("crypto_market_cap") ?: "Нет данных"
        val volume24 = arguments?.getString("crypto_volume24") ?: "Нет данных"
        val cSupply = arguments?.getString("crypto_csupply") ?: "Нет данных"
        val tSupply = arguments?.getString("crypto_tsupply") ?: "Нет данных"
        val mSupply = arguments?.getString("crypto_msupply") ?: "Нет данных"

        val change = price24h.toDoubleOrNull() ?: 0.0
        val changeText = if (change >= 0) {
            "+${String.format("%.2f", change)}%"
        }
        else {
            "${String.format("%.2f", change)}%"
        }

        (activity as AppCompatActivity).supportActionBar?.title = nameCrypto
        binding.balanceTextView.text = "Ваш баланс в ${symbolCrypto}"

        val priceHour = priceUsdCrypto.toDouble() + priceUsdCrypto.toDouble() * (price1h.toDouble() / 100)
        val priceDay = priceUsdCrypto.toDouble() + priceUsdCrypto.toDouble() * (price24h.toDouble() / 100)
        val priceWeek = priceUsdCrypto.toDouble() + priceUsdCrypto.toDouble() * (price7d.toDouble() / 100)

        val sampleData = listOf(priceWeek, priceDay, priceHour)
        binding.lineChart.setData(sampleData)

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

        if (change >= 0) {
            binding.cryptoChange.setBackgroundColor(ContextCompat.getColor(
                requireContext(),
                android.R.color.holo_green_light))

            binding.cryptoChange.setTextColor(ContextCompat.getColor(
                requireContext(),
                android.R.color.holo_green_dark))

        }
        else {
            binding.cryptoChange.setBackgroundColor(ContextCompat.getColor(
                requireContext(),
                android.R.color.holo_red_light))

            binding.cryptoChange.setTextColor(ContextCompat.getColor(
                requireContext(),
                android.R.color.holo_red_dark))

        }


        if (isSystemInDarkTheme(requireContext())) {
            binding.cryptoIndex.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.purple_500))

            binding.cryptoFullName.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.boldTextNightTheme))

            binding.marketCapUsd.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.boldTextNightTheme))

            binding.volume24.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.boldTextNightTheme))

            binding.tSupply.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.boldTextNightTheme))

            binding.cSupply.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.boldTextNightTheme))

            binding.mSupply.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.boldTextNightTheme))

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