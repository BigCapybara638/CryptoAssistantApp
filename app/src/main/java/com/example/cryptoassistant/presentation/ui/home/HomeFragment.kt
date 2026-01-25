package com.example.cryptoassistant.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoassistant.R
import com.example.cryptoassistant.databinding.FragmentHomeBinding
import com.example.cryptoassistant.domain.models.CryptoItem
import com.example.cryptoassistant.domain.models.CryptoNewsItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val cryptoAdapter = HomeCryptoTopAdapter()
    private val newsAdapter = HomeCryptoNewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupObservers()
        viewModel.loadAllData()

    }

    // настройка RecycleView
    private fun setupRecyclerViews() {
        // криптовалюты - горизонтально
        binding.cryptoTopRecyclerView.apply {
            adapter = cryptoAdapter
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    2,
                    GridLayoutManager.VERTICAL,
                    false)
        }

        // новости - вертикально
//        binding.cryptoNewsRecyclerView.apply {
//            adapter = newsAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//        }

        // клик на криптовалюту
        cryptoAdapter.onItemClick = { crypto ->
            openCryptoDetail(crypto)
        }

        // клик на новость
//        newsAdapter.onItemClick = { newsItem ->
//            openNewsDetail(newsItem)
//        }
    }

    // переход в CryptoFragment
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
            R.id.action_first_to_third,
            bundle)

    }

    // переход в NewsFragment
    private fun openNewsDetail(newsItem: CryptoNewsItem) {
        val bundle = Bundle().apply {
            putString("news_title", newsItem.title)
            putString("news_body", newsItem.body)
            putString("news_source", newsItem.sourceData?.sourceName)
        }

        findNavController().navigate(
            R.id.action_first_to_second,
            bundle)

    }

    // загрузка данных и состояние
    private fun setupObservers() {
        // криптовалюты - используем cryptosState
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cryptosState.collect { state ->
                when (state) {
                    is DataState.Success -> {
                        cryptoAdapter.submitList(state.data)
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

        // новости - используем newsState
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.newsState.collect { state ->
//                when (state) {
//                    is DataState.Success -> {
//                        newsAdapter.submitList(state.data)
//                        println("🔄 News updated: ${state.data.size} items")
//                    }
//                    is DataState.Error -> {
//                        println("❌ News error: ${state.message}")
//                    }
//                    is DataState.Loading -> {
//                        println("⏳ Loading news...")
//                    }
//                }
//            }
//        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}