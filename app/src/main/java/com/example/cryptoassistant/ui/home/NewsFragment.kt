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
    private var _binding: FragmentNewsBinding? = null

// This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as AppCompatActivity).supportActionBar?.title = "Новости"

        val title = arguments?.getString("news_title") ?: "Нет заголовка"
        val body = arguments?.getString("news_body") ?: "Отсутствует информация"
        val newsUrl = arguments?.getString("news_image") ?: "Отсутствует изображение"
        val sourceName = arguments?.getString("news_source") ?: "Отсутствует"
        val url = arguments?.getString("news_url") ?: "Отсутствует ссылка"


        binding.newsTitle.text = title
        binding.newsBody.text = body
        binding.newsSource.text = "Источник: ${sourceName}"

        if (isSystemInDarkTheme(requireContext())) {
            (activity as AppCompatActivity).supportActionBar?.title = ""


            binding.newsSource.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.purple_500))

            binding.newsTitle.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.boldTextNightTheme))
        }


//        Picasso.get()
//            .load(newsUrl)
//            .placeholder(R.drawable.ic_crypto) // заглушка на время загрузки
//            .into(binding.newsImageView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Вспомогательная функция для проверки темы
    fun isSystemInDarkTheme(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}