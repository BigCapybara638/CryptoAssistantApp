package com.example.cryptoassistant

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CustomProgressBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private var full: Int = 20
    private var fullness: Int = 1
    private val fullnessBar: Float
        get() = if (fullness > full ) {
            1f / full.toFloat()
        } else {
            ( fullness.toFloat() / full.toFloat())
        }

    private val paintForUnder = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GRAY
        textSize = 50f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = 1000
        val desiredHeight = 70

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width = when(widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val height = when(heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paintForUnder.color = Color.GRAY
        canvas.drawRoundRect(0f, 0f,
            width.toFloat(), height.toFloat(), 20f, 20f, paintForUnder)

        paintForUnder.color = Color.RED
        canvas.drawRoundRect(0f, 0f,
            width.toFloat() * fullnessBar, height.toFloat(), 20f, 10f, paintForUnder)

    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putString("full", full.toString())
        bundle.putString("fullness", fullness.toString())
        bundle.putParcelable("instanceState", super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        full = bundle.getString("full")?.toInt() ?: 10
        fullness = bundle.getString("fullness")?.toInt() ?: 1
        super.onRestoreInstanceState(bundle.getParcelable("instanceState"))
    }

    fun updateFullness(flag: Boolean) {
        if(flag) fullness++ else fullness--
        invalidate()
    }
}