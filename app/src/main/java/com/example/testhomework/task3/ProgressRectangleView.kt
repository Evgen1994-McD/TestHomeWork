package com.example.testhomework.task3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class ProgressRectangleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress: Float = 0f // 0.0 - 1.0
    private var fillColor: Int = generateRandomColor()

    private val backgroundPaint = Paint().apply {
        color = 0xFFCCCCCC.toInt() // Серый цвет для фона
        style = Paint.Style.FILL
    }

    private val fillPaint = Paint().apply {
        color = fillColor
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint().apply {
        color = 0xFF000000.toInt() // Черная обводка
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val rect = RectF()

    var onProgressChanged: ((Float) -> Unit)? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = strokePaint.strokeWidth / 2
        rect.set(
            padding,
            padding,
            width - padding,
            height - padding
        )
        canvas.drawRect(rect, backgroundPaint)

        // Рисуем заполненную часть
        if (progress > 0f) {
            val fillWidth = rect.width() * progress
            val fillRect = RectF(
                rect.left,
                rect.top,
                rect.left + fillWidth,
                rect.bottom
            )
            fillPaint.color = fillColor
            canvas.drawRect(fillRect, fillPaint)
        }
        // Рисуем обводку
        canvas.drawRect(rect, strokePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Увеличиваем прогресс на 10%
            progress += 0.1f

            // Если достигли 100% или больше, сбрасываем на 0
            if (progress >= 1.0f) {
                progress = 0f
            }

            // Меняем цвет при каждом нажатии (включая сброс)
            fillColor = generateRandomColor()

            invalidate()
            onProgressChanged?.invoke(progress)
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun generateRandomColor(): Int {
        return android.graphics.Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
    }

    fun getProgress(): Float = progress

    fun reset() {
        progress = 0f
        fillColor = generateRandomColor()
        invalidate()
    }
}
