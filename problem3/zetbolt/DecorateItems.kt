package com.example.hyeok.myapplication

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect

/**
 * Created by CodeMaker on 2018-09-10.
 */

//인터페이스 연습
interface Decoratable {

    //인터페이스 추상 프로퍼티
    val paint: Paint

    fun onDraw(canvas: Canvas) {
        //default empty
    }

    override fun toString(): String
}

//클래스 위임 연습
open class DefaultDecorate(override val paint: Paint = Paint()) : Decoratable {
    override fun toString(): String {
        return javaClass.simpleName
    }
}

class ColorDecorate(val decorator: Decoratable, val color: Int) :  Decoratable by decorator{
    override fun onDraw(canvas: Canvas) {
        val alpha = paint.alpha
        paint.color = color
        paint.alpha = alpha
        decorator.onDraw(canvas)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decorator"
    }
}

class AlphaDecorate(val decorator: Decoratable, val alpha: Int) : Decoratable by decorator {
    override fun onDraw(canvas: Canvas) {
        paint.alpha = alpha
        decorator.onDraw(canvas)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decorator"
    }
}

class RectDecorate(val decorator: Decoratable = DefaultDecorate()) : Decoratable by decorator {
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(Rect(0, 0, canvas.width, canvas.height), paint)
        decorator.onDraw(canvas)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decorator"
    }
}

class CircleDecorate(val decorator: Decoratable = DefaultDecorate()) : Decoratable by decorator {
    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(canvas.width / 2f, canvas.height / 2f, canvas.width / 2f, paint)
        decorator.onDraw(canvas)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decorator"
    }
}

class TriangleDecorate(val decorator: Decoratable = DefaultDecorate()) : Decoratable by decorator {
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val path = Path()
        path.moveTo(0f, canvas.height.toFloat())
        path.lineTo(canvas.width.toFloat(), canvas.height.toFloat())
        path.lineTo(canvas.width / 2f, 0f)
        path.lineTo(0f, canvas.height.toFloat())
        path.close()
        canvas.drawPath(path, paint)
        decorator.onDraw(canvas)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decorator"
    }
}

class TextDecorate(val decorator: Decoratable = DefaultDecorate(), val text: String) : Decoratable by decorator {
    override fun onDraw(canvas: Canvas) {
        paint.textSize = 100f
        canvas.drawText(text, canvas.width / 2f, canvas.width / 2f, paint)
        decorator.onDraw(canvas)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decorator"
    }
}

//companion object 연습
class Decorate {
    enum class Shape {
        RECT, CIRCLE, TRIANGLE
    }

    companion object {
        fun getDefault(shape: Shape): Decoratable {
            return when (shape) {
                Shape.RECT -> RectDecorate()
                Shape.CIRCLE -> CircleDecorate()
                Shape.TRIANGLE -> TriangleDecorate()
            }
        }
        fun getDefaultText(text: String): Decoratable {
            return TextDecorate(text = text)
        }
    }
}