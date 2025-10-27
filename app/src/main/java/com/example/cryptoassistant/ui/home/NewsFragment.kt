package com.example.cryptoassistant.ui.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.style.URLSpan
import com.example.cryptoassistant.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoassistant.databinding.FragmentNewsBinding
import com.example.cryptoassistant.ui.dashboard.DashboardViewModel
import com.squareup.picasso.Picasso


class NewsFragment : Fragment() {
    companion object {
        private const val ARG_NEWS_TITLE = "news_title"
        private const val ARG_NEWS_BODY = "news_body"
        private const val ARG_NEWS_SOURCE = "news_source"
    }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // установка текста в AppBar
        (activity as AppCompatActivity).supportActionBar?.title = "Новости"

        setupNewsContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // наполнение фрагмента данными
    private fun setupNewsContent() {
        // получение аргументов из HomeFragment
        val title = arguments?.getString(ARG_NEWS_TITLE) ?: "Нет заголовка"
        val body = arguments?.getString(ARG_NEWS_BODY) ?: "Отсутствует информация"
        val sourceName = arguments?.getString(ARG_NEWS_SOURCE) ?: "Отсутствует"

        binding.newsTitle.text = title
        binding.newsBody.text = body
        binding.newsSource.text = "Источник: ${sourceName}"

        // для темной темы
        if (isSystemInDarkTheme(requireContext())) {
            (activity as AppCompatActivity).supportActionBar?.title = ""

            binding.newsSource.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.purple_500))

            binding.newsTitle.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.boldTextNightTheme))
        }
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