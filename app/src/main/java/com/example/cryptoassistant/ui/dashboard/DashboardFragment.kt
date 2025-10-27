package com.example.cryptoassistant.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoassistant.R
import com.example.cryptoassistant.api.cryptoprice.CryptoItem
import com.example.cryptoassistant.databinding.FragmentDashboardBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import kotlin.getValue

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    private val assetsAdapter = AssetsAdapter()
    private val viewModel: DashboardViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupRecycleView()
        viewModel.loadAllData()

        // нажатие на кнопку "Добавить"
        binding.addAssets.setOnClickListener {
            showMaterialBottomSheet()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // общая загрузка и состояние
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cryptosState.collect { state ->
                when (state) {
                    is DataState.Success -> {
                        assetsAdapter.submitList(state.data)
                        println("🔄 Cryptos updated: ${state.data.size} items")
                    }
                    is DataState.Error -> {
                        println("❌ Crypto error: ${state.message}")
                    }
                    is DataState.Loading -> {
                        println("⏳ Loading cryptos...")
                    }
                }
            }
        }

        // общая загрузка
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // общие ошибки
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    println("❌ Error: $it")
                }
            }
        }
    }

    // настройка RecycleView
    private fun setupRecycleView() {

        // вертикально
        binding.assetsRecycleView.apply {
            adapter = assetsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        // клик на криптовалюту
        assetsAdapter.onItemClick = { crypto ->
            openCryptoDetail(crypto)
        }
    }

    // переход в CryptoDetail
    private fun openCryptoDetail(crypto: CryptoItem) {
        val bundle = Bundle().apply {
            putString("crypto_name", crypto.name)
            putString("crypto_symbol", crypto.symbol)
            putString("crypto_priceUsd", crypto.priceUsd)
            putString("crypto_percentChange1h", crypto.percentChange1h)
            putString("crypto_percentChange24h", crypto.percentChange24h)
            putString("crypto_percentChange7d", crypto.percentChange7d)
            putString("crypto_symbol", crypto.symbol)
            putString("crypto_priceUsd", crypto.priceUsd)
            putString("crypto_market_cap", crypto.marketCapUsd)
            putDouble("crypto_volume24", crypto.volume24)
            putString("crypto_csupply", crypto.circulatingSupply)
            putString("crypto_tsupply", crypto.tSupply)
            putString("crypto_msupply", crypto.mSupply)

        }

        findNavController().navigate(
            R.id.action_four_to_first,
            bundle)

    }

    // диалог для добавления активов
    fun showMaterialBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet)

        // Настройка скругленных углов
        bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        bottomSheetDialog.show()
    }
}