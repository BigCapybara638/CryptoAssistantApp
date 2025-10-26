package com.example.cryptoassistant.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoassistant.databinding.FragmentDashboardBinding
import com.example.cryptoassistant.ui.home.HomeCryptoTopAdapter
import com.example.cryptoassistant.ui.home.HomeViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        binding.assetsRecycleView.apply {
            adapter = assetsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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

        viewModel.loadAllData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}