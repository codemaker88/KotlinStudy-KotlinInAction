package study.kotlin.problem3.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

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
class DefaultDecorate(override val paint: Paint = Paint()) : Decoratable {
    override fun toString(): String {
        return javaClass.simpleName
    }
}

class RectDecorate(
        innerDecorate: Decoratable = DefaultDecorate()
) : Decorate(innerDecorate) {

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0.0f, canvas.height.toFloat(), canvas.width.toFloat(), 0.0f, innerDecorate.paint)
        innerDecorate.onDraw(canvas)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $innerDecorate"
    }
}

class CircleDecorate(
        innerDecorate: Decoratable = DefaultDecorate()
) : Decorate(innerDecorate) {

    override fun onDraw(canvas: Canvas) {
        val halfWidth = canvas.width / 2.0f
        val halfHeight = canvas.height / 2.0f
        canvas.drawCircle(halfWidth, halfHeight, halfHeight, innerDecorate.paint)
        innerDecorate.onDraw(canvas)
    }
}

class TriangleDecorate(
        innerDecorate: Decoratable = DefaultDecorate()
) : Decorate(innerDecorate) {

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        val (topVertexX, topVertexY) = canvas.width / 2.0f to 0.0f
        val (leftVertexX, leftVertexY) = 0.0f to canvas.height.toFloat()
        val (rightVertexX, rightVertexY) = canvas.width.toFloat() to canvas.height.toFloat()
        val path = path.apply {
            reset()
            moveTo(topVertexX, topVertexY)
            lineTo(leftVertexX, leftVertexY)
            lineTo(rightVertexX, rightVertexY)
            lineTo(topVertexX, topVertexY)
            close()
        }
        canvas.drawPath(path, innerDecorate.paint)
        innerDecorate.onDraw(canvas)
    }
}

class TextDecorate(
        innerDecorate: Decoratable = DefaultDecorate(),
        private val text: String
) : Decorate(innerDecorate) {

    override fun onDraw(canvas: Canvas) {
        innerDecorate.paint.textSize = 100.0f
        innerDecorate.onDraw(canvas)
        canvas.drawText(text, canvas.width / 2.0f - 62.5f, canvas.height / 2.0f + 37.5f, paint)
    }
}

class ColorDecorate(
        innerDecorate: Decoratable = DefaultDecorate(),
        private val color: Int
) : Decorate(innerDecorate) {

    override fun onDraw(canvas: Canvas) {
        val presetAlpha = innerDecorate.paint.alpha
        innerDecorate.paint.color = color
        innerDecorate.paint.alpha = presetAlpha
        innerDecorate.onDraw(canvas)
    }
}

class AlphaDecorate(
        innerDecorate: Decoratable = DefaultDecorate(),
        private val alpha: Int
) : Decorate(innerDecorate) {

    override fun onDraw(canvas: Canvas) {
        innerDecorate.paint.alpha = alpha
        innerDecorate.onDraw(canvas)
    }
}

//companion object 연습
abstract class Decorate(
        protected val innerDecorate: Decoratable
) : Decoratable by innerDecorate {

    enum class Shape {
        RECT, CIRCLE, TRIANGLE
    }

    companion object {
        fun createDefault(shape: Shape): Decoratable {
            return when (shape) {
                Shape.RECT -> RectDecorate()
                Shape.CIRCLE -> CircleDecorate()
                Shape.TRIANGLE -> TriangleDecorate()
            }
        }

        fun createDefault(text: String): Decoratable {
            return TextDecorate(text = text)
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $innerDecorate"
    }
}