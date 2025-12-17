package com.example.testhomework.task3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
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
        color = 0xFFCCCCCC.toInt()
        style = Paint.Style.FILL
    }

    private val fillPaint = Paint().apply {
        color = fillColor
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint().apply {
        color = 0xFF000000.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val rect = RectF()
    private val fillRect = RectF()
    private val padding = strokePaint.strokeWidth / 2
    private var fillWidth =0f
    var onProgressChanged: ((Float) -> Unit)? = null

    init {
        setOnClickListener {
            progress += 0.1f
            if (progress >= 1.0f) {
                progress = 0f
            }
            fillColor = generateRandomColor()
            invalidate()
            onProgressChanged?.invoke(progress)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rect.set(
            padding,
            padding,
            width - padding,
            height - padding
        )
        canvas.drawRect(rect, backgroundPaint)

        if (progress > 0f) {
             fillWidth = rect.width() * progress
            fillRect.set(
                rect.left,
                rect.top,
                rect.left + fillWidth,
                rect.bottom
            )
            fillPaint.color = fillColor
            canvas.drawRect(fillRect, fillPaint)
        }

        canvas.drawRect(rect, strokePaint)
    }

    private fun generateRandomColor(): Int {
        return android.graphics.Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
    }
}
