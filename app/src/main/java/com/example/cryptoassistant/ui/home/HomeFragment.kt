package com.example.cryptoassistant.ui.home

import com.example.cryptoassistant.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cryptoassistant.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

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

        setupNestedNavigation()
    }

    private fun setupNestedNavigation() {
        // Создаем вложенный NavHostFragment
        val navHostFragment = childFragmentManager.findFragmentById(
            R.id.nav_host_fragment_home
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Настраиваем BottomNavigationView
        binding.bottomNavHome.setupWithNavController(navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}