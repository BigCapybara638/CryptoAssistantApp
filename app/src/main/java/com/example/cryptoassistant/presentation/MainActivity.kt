package com.example.cryptoassistant.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cryptoassistant.presentation.customviews.CustomToolbarView
import com.example.cryptoassistant.R
import com.example.cryptoassistant.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var mainToolbar: CustomToolbarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainToolbar = binding.customToolbar

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment


        navController = navHostFragment.navController
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard
//            )
//        )

//        setupActionBarWithNavController(navController, appBarConfiguration)

        setupToolbarWithNavigation()
        navView.setupWithNavController(navController)


        // изменение цвета линии над нижним меню
        if (isSystemInDarkTheme(this)) {
            binding.topLine.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.teal_200)
            )
        }
    }

    private fun setupToolbarWithNavigation() {
        // Обновляем Toolbar при смене фрагментов
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    mainToolbar.setTitle("Главная")
                    mainToolbar.setBackVisible(false)
                }

                R.id.navigation_dashboard -> {
                    mainToolbar.setTitle("Активы")
                    mainToolbar.setBackVisible(false)
                }

                else -> {
                    // Для других фрагментов показываем кнопку "Назад"
                    mainToolbar.setBackVisible(true)
                    mainToolbar.setOnBackListener {
                        navController.navigateUp()
                    }
                }
            }
        }
        // Начальная настройка
        mainToolbar.setTitle("Главная")
        mainToolbar.setBackVisible(false)

        // Обработчик кнопки назад в Toolbar
        mainToolbar.setOnBackListener {
            navController.navigateUp()
        }
    }

    // обработка нажатия на кнопку "Назад"
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    // вспомогательная функция для проверки темы
    fun isSystemInDarkTheme(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}