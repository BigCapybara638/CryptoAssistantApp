package com.example.cryptoassistant.ui.home

import android.content.Context
import com.example.cryptoassistant.R
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoassistant.databinding.ItemHomeCryptotopsBinding
import com.example.cryptoassistant.api.cryptoprice.CryptoItem

class HomeCryptoTopAdapter : ListAdapter<CryptoItem, HomeCryptoTopAdapter.CryptoViewHolder>(DIFF_CALLBACK) {

    // обьявление лямбды
    var onItemClick: ((CryptoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ItemHomeCryptotopsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CryptoViewHolder(private val binding: ItemHomeCryptotopsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crypto: CryptoItem) {
            binding.cryptoSymbol.text = crypto.symbol
            binding.cryptoName.text = crypto.name

            binding.root.setOnClickListener {
                onItemClick?.invoke(crypto)
            }

            // форматируем цену
            val price = crypto.priceUsd.toDoubleOrNull() ?: 0.0
            binding.cryptoPrice.text = if (price < 1) {
                "$${String.format("%.4f", price)}"
            } else {
                "$${String.format("%.2f", price)}"
            }

            // изменение цены за 24ч
            val change = crypto.percentChange24h.toDoubleOrNull() ?: 0.0
            val changeText = if (change >= 0) "+${String.format("%.2f", change)}%"
            else "${String.format("%.2f", change)}%"

            binding.cryptoChange.text = changeText

            // цвет в зависимости от изменения цены
            val context = binding.root.context

            // для темной темы
            if (isSystemInDarkTheme(context)) {

                binding.cryptoSymbol.setTextColor(
                    ContextCompat.getColor(context, R.color.boldTextNightTheme)
                )

                binding.cryptoName.setTextColor(
                    ContextCompat.getColor(context, R.color.purple_500)
                )

                binding.cryptoPrice.setTextColor(
                    ContextCompat.getColor(context, R.color.boldTextNightTheme)
                )

                if (change >= 0) {
                    binding.cryptoChange.setTextColor(
                        ContextCompat.getColor(context, android.R.color.holo_green_dark)
                    )

                    binding.cryptoChange.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.greenBlack)
                    )
                } else {
                    binding.cryptoChange.setTextColor(
                        ContextCompat.getColor(context, android.R.color.holo_red_dark)
                    )
                    binding.cryptoChange.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.redBlack)

                    )
                }
            } else {
                if (change >= 0) {
                    binding.cryptoChange.setTextColor(
                        ContextCompat.getColor(context, android.R.color.holo_green_dark)
                    )

                    binding.cryptoChange.setBackgroundColor(
                        ContextCompat.getColor(context, android.R.color.holo_green_light)
                    )
                } else {
                    binding.cryptoChange.setTextColor(
                        ContextCompat.getColor(context, android.R.color.holo_red_dark)
                    )
                    binding.cryptoChange.setBackgroundColor(
                        ContextCompat.getColor(context, android.R.color.holo_red_light)

                    )
                }
            }
        }
    }

    // DiffUtil для обновления списка
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CryptoItem>() {
            override fun areItemsTheSame(oldItem: CryptoItem, newItem: CryptoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CryptoItem, newItem: CryptoItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun isSystemInDarkTheme(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}