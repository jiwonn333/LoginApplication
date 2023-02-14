package com.example.loginapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.BoringLayout.Metrics
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class KakaoLoginButton(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#FECB00")
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE

//        textPaint.textSize = textSize

//        textSize =
//        textSize = sp(14).toFloat()
//        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, context.resources.displayMetrics)

        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 8f, 8f, paint)
        canvas?.drawText("Kakao Login", width / 2f, height / 2f + textPaint.textSize / 2, textPaint)
    }
}
