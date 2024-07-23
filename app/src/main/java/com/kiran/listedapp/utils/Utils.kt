package com.kiran.listedapp.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable

private const val GRADIENT_COLOR = "#84B6FF"
class GradientFillDrawable : Drawable() {
    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val paint = Paint().apply {
            shader = LinearGradient(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.left.toFloat(),
                bounds.bottom.toFloat(),
                Color.parseColor(GRADIENT_COLOR),
                Color.TRANSPARENT,
                Shader.TileMode.MIRROR
            )
            style = Paint.Style.FILL
        }
        canvas.drawRect(bounds, paint)
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}