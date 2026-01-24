package com.example.cryptoassistant.presentation.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoassistant.R
import com.example.cryptoassistant.data.local.AssetResult
import com.example.cryptoassistant.data.local.AssetsEntity
import com.example.cryptoassistant.databinding.DialogBottomSheetBinding
import com.example.cryptoassistant.databinding.DialogBottomSheetDeleteBinding
import com.example.cryptoassistant.databinding.FragmentDashboardBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
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

        // нажатие на кнопку "Добавить"
        binding.addAssets.setOnClickListener {
            showMaterialBottomSheetAdd()
        }

        binding.deleteAssets.setOnClickListener {
            showMaterialBottomSheetDelete()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // общая загрузка и состояние
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.balance.collect { balance ->
                    balance?.let {
                        binding.balance.text = "$${"%.2f".format(it)}"
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.balanceChange.collect { balanceChange ->
                    balanceChange?.let {
                        if (it > 0) {
                            binding.balanceChange.setTextColor(
                                ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
                            )

                            binding.balanceChange.setBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.greenBlack)
                            )}
                        else {
                            binding.balanceChange.setTextColor(
                                ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
                            )
                            binding.balanceChange.setBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.redBlack)

                            )
                        }
                        binding.balanceChange.text = "${"%.2f".format(it)}$"
                    }
                }
            }
        }


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
    private fun openCryptoDetail(crypto: AssetResult) {
        val bundle = Bundle().apply {
            putString("crypto_name", crypto.asset.name)
            putString("crypto_symbol", crypto.asset.symbol)
            putString("crypto_priceUsd", crypto.asset.priceUsd)
            putString("crypto_percentChange1h", crypto.asset.percentChange1h)
            putString("crypto_percentChange24h", crypto.asset.percentChange24h)
            putString("crypto_percentChange7d", crypto.asset.percentChange7d)
            putString("crypto_symbol", crypto.asset.symbol)
            putString("crypto_priceUsd", crypto.asset.priceUsd)
            putString("crypto_market_cap", crypto.asset.marketCapUsd)
            putDouble("crypto_volume24", crypto.asset.volume24)
            putString("crypto_csupply", crypto.asset.circulatingSupply)
            putString("crypto_tsupply", crypto.asset.tSupply)
            putString("crypto_msupply", crypto.asset.mSupply)

        }

        findNavController().navigate(
            R.id.action_four_to_first,
            bundle)

    }

    // диалог для добавления активов
    private fun showMaterialBottomSheetAdd() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogBottomSheetBinding.inflate(
            LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(dialogBinding.root)


        dialogBinding.btnConfirm.setOnClickListener {
            val ticker = dialogBinding.editTicker.text.toString()
            val stringSum = dialogBinding.editSum.text.toString()
            val sum = stringSum.toDouble()
            val stringCurrent = dialogBinding.editCurrent.text.toString()
            val current = stringCurrent.toDouble()

            val listAssets = mutableListOf<AssetsEntity>()
            val asset = AssetsEntity(
                idCrypto = ticker,
                amount = sum/current,
                price = current
            )

            listAssets.add(asset)

            viewModel.insertAssets(listAssets)
            bottomSheetDialog.dismiss()
            viewModel.loadAllData()
        }

        dialogBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun showMaterialBottomSheetDelete() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogBottomSheetDeleteBinding.inflate(
            LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(dialogBinding.root)


        dialogBinding.btnConfirm.setOnClickListener {
            val ticker = dialogBinding.editTicker.text.toString()
            val stringSum = dialogBinding.editSum.text.toString()
            val sum = stringSum.toDouble()
            val stringCurrent = dialogBinding.editCurrent.text.toString()
            val current = stringCurrent.toDouble()


            val listAssets = mutableListOf<AssetsEntity>()
            val asset = AssetsEntity(
                idCrypto = ticker,
                amount = -(sum/current),
                price = current
            )

            listAssets.add(asset)

            viewModel.insertAssets(listAssets)
            bottomSheetDialog.dismiss()
            viewModel.loadAllData()
        }

        dialogBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

}