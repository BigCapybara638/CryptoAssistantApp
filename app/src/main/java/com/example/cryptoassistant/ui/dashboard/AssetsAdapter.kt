package com.example.cryptoassistant.ui.dashboard

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoassistant.R
import com.example.cryptoassistant.api.cryptoprice.CryptoItem
import com.example.cryptoassistant.databinding.ItemAssetsBinding

class AssetsAdapter : ListAdapter<CryptoItem, AssetsAdapter.AssetsViewHolder>(DIFF_CALLBACK) {

    //  обьявление лямбды
    var onItemClick: ((CryptoItem) -> Unit)? = null

    inner class AssetsViewHolder(private val binding: ItemAssetsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crypto: CryptoItem) {
            binding.cryptoName.text = crypto.name
            binding.cryptoPrice.text = "$${crypto.priceUsd}"

            binding.root.setOnClickListener {
                onItemClick?.invoke(crypto)
            }

            // изменение цены за 24ч
            val change = crypto.percentChange24h.toDoubleOrNull() ?: 0.0
            val changeText = if (change >= 0) "+${String.format("%.2f", change)}%"
            else "${String.format("%.2f", change)}%"

            binding.cryptoChange.text = changeText

            // цвет в зависимости от изменения цены
            val context = binding.root.context

            if (isSystemInDarkTheme(context)) {

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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssetsAdapter.AssetsViewHolder {
        val binding = ItemAssetsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AssetsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssetsAdapter.AssetsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


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