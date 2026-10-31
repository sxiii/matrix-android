package com.example.matrixwallpaper

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.random.Random

class MatrixRenderer {

    private var columns = 0
    private var rows = 0
    private var cellSize = 0f
    private var drops = IntArray(0)
    private var speeds = IntArray(0)
    private var lengths = IntArray(0)
    private var width = 0
    private var height = 0
    private var theme = MatrixThemes.classicGreen

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val bgPaint = Paint()

    fun setTheme(t: MatrixTheme) {
        theme = t
    }

    fun resize(w: Int, h: Int) {
        if (w <= 0 || h <= 0) return
        width = w
        height = h
        cellSize = w / 22f
        columns = (w / cellSize).toInt() + 1
        rows = (h / cellSize).toInt() + 1
        drops = IntArray(columns) { Random.nextInt(-rows, 0) }
        speeds = IntArray(columns) { Random.nextInt(1, 4) }
        lengths = IntArray(columns) { Random.nextInt(8, rows.coerceAtLeast(12)) }
    }

    fun drawFrame(canvas: Canvas) {
        if (columns == 0) return

        // Semi-transparent background for trail effect
        bgPaint.color = theme.background
        bgPaint.alpha = 45
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        paint.textSize = cellSize * 0.85f

        for (col in 0 until columns) {
            val head = drops[col]
            val len = lengths[col]

            for (row in (head - len)..head) {
                if (row < 0 || row > rows) continue

                val x = col * cellSize + cellSize / 2f
                val y = row * cellSize

                val ch = theme.charSet[Random.nextInt(theme.charSet.length)]

                val distFromHead = head - row
                when {
                    distFromHead == 0 -> {
                        paint.color = theme.headColor
                        paint.alpha = 255
                    }
                    distFromHead < len / 3 -> {
                        paint.color = theme.bodyColor
                        paint.alpha = 240
                    }
                    distFromHead < len * 2 / 3 -> {
                        paint.color = theme.bodyColor
                        paint.alpha = 160
                    }
                    else -> {
                        paint.color = theme.trailColor
                        paint.alpha = (100 - (distFromHead.toFloat() / len * 80)).toInt().coerceIn(20, 100)
                    }
                }

                canvas.drawText(ch.toString(), x, y, paint)
            }

            drops[col] += speeds[col]

            if (drops[col] - lengths[col] > rows) {
                drops[col] = Random.nextInt(-rows / 2, 0)
                speeds[col] = Random.nextInt(1, 4)
                lengths[col] = Random.nextInt(8, rows.coerceAtLeast(12))
            }
        }
    }

    /** Draw a fully opaque background once (call before first drawFrame or on resize). */
    fun drawBackground(canvas: Canvas) {
        bgPaint.color = theme.background
        bgPaint.alpha = 255
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
    }
}
