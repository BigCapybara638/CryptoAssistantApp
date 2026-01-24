package com.example.cryptoassistant.domain.models

import com.example.cryptoassistant.api.crypronews.CategoryData
import com.example.cryptoassistant.api.crypronews.SourceData
import com.example.cryptoassistant.api.crypronews.toRelativeTime
import com.google.gson.annotations.SerializedName

data class CryptoNewsItem(
    @SerializedName("ID") val id: Long, // в логах видно что ID длинные числа
    @SerializedName("PUBLISHED_ON") val publishedOn: Long,
    @SerializedName("IMAGE_URL") val imageUrl: String?,
    @SerializedName("TITLE") val title: String,
    @SerializedName("URL") val url: String,
    @SerializedName("BODY") val body: String,
    @SerializedName("SOURCE_DATA") val sourceData: SourceData?,
    @SerializedName("CATEGORY_DATA") val categoryData: List<CategoryData>? = null,
    @SerializedName("SENTIMENT") val sentiment: String? = null
) {
    // Computed property для относительного времени
    val relativeTime: String
        get() = publishedOn.toRelativeTime()

}