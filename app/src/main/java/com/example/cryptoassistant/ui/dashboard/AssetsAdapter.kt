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
import com.example.cryptoassistant.api.data.AssetResult
import com.example.cryptoassistant.api.data.AssetsEntity
import com.example.cryptoassistant.databinding.ItemAssetsBinding

class AssetsAdapter : ListAdapter<AssetResult, AssetsAdapter.AssetsViewHolder>(DIFF_CALLBACK) {

    //  обьявление лямбды
    var onItemClick: ((AssetResult) -> Unit)? = null

    inner class AssetsViewHolder(private val binding: ItemAssetsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crypto: AssetResult) {
            val context = binding.root.context

            val amount = crypto.amount
            val price = crypto.price
            val count = amount / price
            val actualPrice = count * crypto.asset.priceUsd.toDouble()


            binding.assetAmount.text = try {
                "$${"%.5f".format(actualPrice)}"
            } catch (e: Exception) {
                "$0.0000"
            }

            binding.assetCount.text = try {
                "${"%.5f".format(count)}"
            } catch (e: Exception) {
                "$0.0000"
            }

            val changeAsset = actualPrice - amount
            if (changeAsset > 0) {
                binding.assetChange.text = "+${"%.2f".format(changeAsset)}"
                binding.assetChange.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_green_dark)
                )

                binding.assetChange.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.greenBlack)
                )
            } else {
                binding.assetChange.text = "${"%.2f".format(changeAsset)}"
                binding.assetChange.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_red_dark)
                )
                binding.assetChange.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.redBlack)

                )
            }

            binding.cryptoName.text = crypto.asset.name
            binding.cryptoPrice.text = "$${crypto.asset.priceUsd}"

            binding.root.setOnClickListener {
                onItemClick?.invoke(crypto)
            }

            // изменение цены за 24ч
            val change = crypto.asset.percentChange24h.toDoubleOrNull() ?: 0.0
            val changeText = if (change >= 0) "+${String.format("%.2f", change)}%"
            else "${String.format("%.2f", change)}%"

            binding.cryptoChange.text = changeText


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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AssetResult>() {
            override fun areItemsTheSame(oldItem: AssetResult, newItem: AssetResult): Boolean {
                return oldItem.asset.id == newItem.asset.id
            }

            override fun areContentsTheSame(oldItem: AssetResult, newItem: AssetResult): Boolean {
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