package com.example.cryptoassistant.presentation.ui.home

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoassistant.R
import com.example.cryptoassistant.databinding.ItemHomeNewsBinding
import com.example.cryptoassistant.domain.models.CryptoNewsItem

class HomeCryptoNewsAdapter: ListAdapter<CryptoNewsItem, HomeCryptoNewsAdapter.HomeCryptoNewsViewHolder>(DIFF_CALLBACK) {

    // обьявление лямбды
    var onItemClick: ((CryptoNewsItem) -> Unit)? = null
    inner class HomeCryptoNewsViewHolder(private val binding: ItemHomeNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crypto: CryptoNewsItem) {
            // Используем  ID из  layout
            binding.cryptoNewsTitle.text = crypto.title
            binding.resourceName.text = crypto.sourceData?.sourceName
            binding.cryptoNewsTime.text = crypto.relativeTime

            binding.root.setOnClickListener {
                onItemClick?.invoke(crypto)
            }

            if (isSystemInDarkTheme(itemView.context)) {
                binding.resourceName.setTextColor(
                    ContextCompat.getColor(
                    itemView.context,
                    R.color.purple_500))

                binding.cryptoNewsTitle.setTextColor(
                    ContextCompat.getColor(
                    itemView.context,
                    R.color.boldTextNightTheme))
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCryptoNewsViewHolder {
        val binding = ItemHomeNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeCryptoNewsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeCryptoNewsViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CryptoNewsItem>() {
            override fun areItemsTheSame(oldItem: CryptoNewsItem, newItem: CryptoNewsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CryptoNewsItem, newItem: CryptoNewsItem): Boolean {
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