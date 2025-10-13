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
import com.example.cryptoassistant.R
import com.example.cryptoassistant.databinding.FragmentCryptoBinding
import com.example.cryptoassistant.databinding.FragmentDashboardBinding
import com.example.cryptoassistant.ui.dashboard.DashboardViewModel

class CryptoFragment : Fragment() {
    private var _binding: FragmentCryptoBinding? = null

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

        val nameCrypto = arguments?.getString("crypto_name") ?: "Нет данных"
        val symbolCrypto = arguments?.getString("crypto_symbol") ?: "Нет данных"
        val priceUsdCrypto = arguments?.getString("crypto_priceUsd") ?: "Нет данных"
        val price24h = arguments?.getString("crypto_percentChange24h") ?: "Нет данных"


        val change = price24h.toDoubleOrNull() ?: 0.0
        val changeText = if (change >= 0) {
            "+${String.format("%.2f", change)}%"
        }
        else {
            "${String.format("%.2f", change)}%"
        }


        binding.cryptoFullName.text = nameCrypto
        binding.cryptoIndex.text = symbolCrypto
        binding.cryptoPrice.text = "$${priceUsdCrypto}"
        binding.cryptoChange.text = changeText

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

        }

        return root
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