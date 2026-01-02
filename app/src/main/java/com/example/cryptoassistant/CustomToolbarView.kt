package com.example.cryptoassistant

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.cryptoassistant.databinding.LayoutCustomToolbarBinding
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toDrawable

class CustomToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutCustomToolbarBinding

    var backgroundToolbarColor = ContextCompat.getColor(context, R.color.purple_500)

    init {
        binding = LayoutCustomToolbarBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        binding.customToolbarViewLayout.setBackgroundColor(backgroundToolbarColor)

    }

    val paint = Paint().apply {
        color = Color.BLACK
        //color = backgroundToolbarColor
        style = Paint.Style.FILL
    }


    fun setOnBackListener(listener: () -> Boolean) {
        binding.imageButton.setOnClickListener { listener() }
    }

    fun setTitle(title: String) {
        binding.textView.text = title
    }

    fun setBackVisible(visibility: Boolean) {
        if (visibility) {
            binding.imageButton.visibility = VISIBLE
        } else {
            binding.imageButton.visibility = GONE
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}