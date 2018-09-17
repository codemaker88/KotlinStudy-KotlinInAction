package frogsm.kotlin.problem3

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorInt
import android.support.annotation.IntRange

//인터페이스 연습
interface Decoratable {
    val paint: Paint //인터페이스 추상 프로퍼티
    fun onDraw(canvas: Canvas) = Unit //default empty
    override fun toString(): String
}

//클래스 위임 연습
class DefaultDecorate(override val paint: Paint = Paint()) : Decoratable {
    override fun toString(): String = javaClass.simpleName
}

class ColorDecorate(
        private val decorate: Decoratable,
        @ColorInt private val color: Int
) : Decoratable by decorate {
    override fun toString(): String = "${javaClass.simpleName} + $decorate"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        decorate.onDraw(canvas)
    }

    fun onDrawWithTask(canvas: Canvas, task: () -> Unit) {
        paint.color = color
        task.invoke()
        decorate.onDraw(canvas)
    }
}

class AlphaDecorate(
        private val decorate: Decoratable,
        @IntRange(from = 0, to = 255) private val alpha: Int
) : Decoratable by decorate {
    override fun toString(): String = "${javaClass.simpleName} + $decorate"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (decorate is ColorDecorate) {
            decorate.onDrawWithTask(canvas) { paint.alpha = alpha }
        } else {
            paint.alpha = alpha
            decorate.onDraw(canvas)
        }
    }
}

class RectDecorate(
        private val decorate: Decoratable = DefaultDecorate()
) : Decoratable by decorate {
    override fun toString(): String = "${javaClass.simpleName} + $decorate"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        decorate.onDraw(canvas)
        canvas.drawRect(0.0f, canvas.height.toFloat(), canvas.width.toFloat(), 0.0f, paint)
    }
}

class CircleDecorate(
        private val decorate: Decoratable = DefaultDecorate()
) : Decoratable by decorate {
    override fun toString(): String = "${javaClass.simpleName} + $decorate"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val halfWidth = canvas.width / 2.0f
        val halfHeight = canvas.height / 2.0f
        decorate.onDraw(canvas)
        canvas.drawCircle(halfWidth, halfHeight, halfHeight, paint)
    }
}

class TriangleDecorate(
        private val decorate: Decoratable = DefaultDecorate()
) : Decoratable by decorate {
    private val path: Path = Path()

    override fun toString(): String = "${javaClass.simpleName} + $decorate"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
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
        decorate.onDraw(canvas)
        canvas.drawPath(path, paint)
    }
}

class TextDecorate(
        val text: String,
        private val decorate: Decoratable = DefaultDecorate()
) : Decoratable by decorate {
    override fun toString(): String = "${javaClass.simpleName} + $decorate"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.textSize = 100.0f
        decorate.onDraw(canvas)
        canvas.drawText(text, canvas.width / 2.0f - 62.5f, canvas.height / 2.0f + 37.5f, paint)
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
            return TextDecorate(text)
        }
    }
}