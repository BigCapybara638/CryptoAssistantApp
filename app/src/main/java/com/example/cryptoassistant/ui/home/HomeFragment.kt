package com.example.cryptoassistant.ui.home


import android.content.Context
import android.content.res.Configuration
import com.example.cryptoassistant.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.cryptoassistant.api.crypronews.CryptoNewsItem
import com.example.cryptoassistant.api.cryptoprice.CryptoItem
import com.example.cryptoassistant.databinding.FragmentDashboardBinding
import com.example.cryptoassistant.databinding.FragmentHomeBinding
import com.example.cryptoassistant.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.launch

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

    fun isSystemInDarkTheme(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    private fun setupRecyclerViews() {
        // Криптовалюты - горизонтально
        binding.cryptoTopRecyclerView.apply {
            adapter = cryptoAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        // Новости - вертикально
        binding.cryptoNewsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // клик на криптовалюту
        cryptoAdapter.onItemClick = { crypto ->
            openCryptoDetail(crypto)
        }

        // клик на новость
        newsAdapter.onItemClick = { newsItem ->
            openNewsDetail(newsItem)
        }
    }

    private fun openCryptoDetail(crypto: CryptoItem) {
        val bundle = Bundle().apply {
            putString("crypto_name", crypto.name)
            putString("crypto_symbol", crypto.symbol)
            putString("crypto_priceUsd", crypto.priceUsd)
            putString("crypto_percentChange24h", crypto.percentChange24h)
        }

        findNavController().navigate(
            R.id.action_first_to_third,
            bundle)

    }

    private fun openNewsDetail(newsItem: CryptoNewsItem) {
        val bundle = Bundle().apply {
            putString("news_title", newsItem.title)
            putString("news_body", newsItem.body)
            putString("news_image", newsItem.imageUrl)
            putString("news_source", newsItem.sourceData?.sourceName)
            putString("news_url", newsItem.url)
        }

        findNavController().navigate(
            R.id.action_first_to_second,
            bundle)

    }

    private fun setupObservers() {
        // Криптовалюты - используем cryptosState
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

        // Новости - используем newsState
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newsState.collect { state ->
                when (state) {
                    is DataState.Success -> {
                        newsAdapter.submitList(state.data)
                        println("🔄 News updated: ${state.data.size} items")
                    }
                    is DataState.Error -> {
                        println("❌ News error: ${state.message}")
                    }
                    is DataState.Loading -> {
                        println("⏳ Loading news...")
                    }
                }
            }
        }

        // Общая загрузка
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // Общие ошибки
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    println("❌ Error: $it")
                }
            }
        }

        // Обновление данных
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isRefreshing.collect { isRefreshing ->
                // Можно добавить индикатор обновления если нужно
                if (isRefreshing) {
                    println("🔄 Refreshing data...")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}