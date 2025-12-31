package com.example.cryptoassistant

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DiagramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    // defStyleAttr: Int = 0
) : View(context, attrs) {

    // цвета
    private val colors = listOf(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
    private val sweepAngle = 360f / colors.size

    // Paint - кисточка, которой мы задаем цвет и др. параметры
    private val paintForCircle = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
        strokeWidth = 5f
    }


    // рисование
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircleButton(canvas)
    }

    // отслеживание нажатий
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return super.onTouchEvent(event)
    }

    // функция для создания Arc
    private fun drawCircleButton(canvas: Canvas) {
        val centerX = width/2f
        val centerY = height/2f
        val radius = width.coerceAtMost(height) / 2f

        for(i in colors.indices) {
            paintForCircle.color = colors[i]

            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                sweepAngle * i,
                sweepAngle,
                true,
                paintForCircle
            )
        }
    }
}