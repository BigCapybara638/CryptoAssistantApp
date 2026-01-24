package com.example.cryptoassistant.presentation.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class CryptoChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var cryptoData: List<Double> = emptyList()
    private val paint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 8f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val pointPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 12f
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.GRAY
        textSize = 42f // Уменьшил размер текста
        textAlign = Paint.Align.CENTER
        //Paint.setTypeface = Typeface.DEFAULT_BOLD
    }

    private val gridPaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    fun setData(data: List<Double>) {
        cryptoData = data
        invalidate() // Перерисовываем view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (cryptoData.isEmpty()) {
            // Показываем сообщение если данных нет
            canvas.drawText(
                "Нет данных для графика",
                width / 2f,
                height / 2f,
                textPaint
            )
            return
        }

        if (cryptoData.size == 1) {
            // Если только одна точка, показываем сообщение
            canvas.drawText(
                "Недостаточно данных",
                width / 2f,
                height / 2f,
                textPaint
            )
            return
        }

        val width = width.toFloat()
        val height = height.toFloat()
        val padding = 80f // Увеличил padding для текста

        val maxPrice = cryptoData.maxOrNull() ?: 0.0
        val minPrice = cryptoData.minOrNull() ?: 0.0
        var priceRange = maxPrice - minPrice

        // Если все цены одинаковые, создаем искусственный диапазон
        if (priceRange == 0.0) {
            priceRange = maxPrice * 0.1 // 10% от цены
            if (priceRange == 0.0) priceRange = 1.0
        }

        val path = Path()
        val availableHeight = height - 2 * padding
        val availableWidth = width - 2 * padding

        // Рисуем сетку
        drawGrid(canvas, width, height, padding)

        // Рисуем линию графика
        cryptoData.forEachIndexed { index, price ->
            val x = padding + (index.toFloat() / (cryptoData.size - 1)) * availableWidth
            val normalizedPrice = if (priceRange > 0) (price - minPrice) / priceRange else 0.5
            val y = padding + availableHeight - (normalizedPrice * availableHeight).toFloat()

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            // Рисуем точки только если данных не слишком много
            if (cryptoData.size <= 20) {
                canvas.drawCircle(x, y, 8f, pointPaint)
            }
        }

        canvas.drawPath(path, paint)

        // Подписи осей
        drawLabels(canvas, width, height, padding, minPrice, maxPrice)
    }

    private fun drawGrid(canvas: Canvas, width: Float, height: Float, padding: Float) {
        // Вертикальные линии
        for (i in 0..4) {
            val x = padding + (width - 2 * padding) * i / 4
            canvas.drawLine(x, padding, x, height - padding, gridPaint)
        }

        // Горизонтальные линии
        for (i in 0..4) {
            val y = padding + (height - 2 * padding) * i / 4
            canvas.drawLine(padding, y, width - padding, y, gridPaint)
        }
    }

    private fun drawLabels(
        canvas: Canvas,
        width: Float,
        height: Float,
        padding: Float,
        minPrice: Double,
        maxPrice: Double
    ) {
        val labelPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 32f
            textAlign = Paint.Align.LEFT
        }

        // Подписи по оси Y (цены)
        for (i in 0..2) {
            val price = minPrice + (maxPrice - minPrice) * (2 - i) / 2
            val y = padding + (height - 2 * padding) * i / 2
            canvas.drawText(
                "$${"%.2f".format(price)}",
                10f,
                y + 10f,
                labelPaint
            )
        }
    }

}