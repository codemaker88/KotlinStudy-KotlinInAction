package com.rounz.decorator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect

interface Decorator {
    val paint: Paint

    fun decorate(canvas: Canvas) { }

    override fun toString(): String
}

class Decoration {
    companion object {
        fun getDefault(text: String): Decorator = TextDecorator(DefaultDecorator(), text)

        fun getDefault(shape: Shape): Decorator {
            return when (shape) {
                Shape.RECT -> RectDecorator(DefaultDecorator())
                Shape.CIRCLE -> CircleDecorator(DefaultDecorator())
                else -> TriangleDecorator(DefaultDecorator())
            }
        }
    }

    enum class Shape {
        RECT, CIRCLE, TRIANGLE
    }
}

class DefaultDecorator(override val paint: Paint = Paint()) : Decorator {
    override fun toString(): String = javaClass.simpleName
}

class AlphaDecorator(
        private val anotherDecorator: Decorator,
        alpha: Int
) : Decorator by anotherDecorator {
    override val paint: Paint = anotherDecorator.paint

    init {
        paint.alpha = alpha
    }

    override fun decorate(canvas: Canvas) {
        anotherDecorator.decorate(canvas)
    }

    override fun toString(): String = "${javaClass.simpleName} + $anotherDecorator"
}

class ColorDecorator(
        private val anotherDecorator: Decorator,
        color: Int
) : Decorator by anotherDecorator {
    override val paint: Paint = anotherDecorator.paint

    init {
        val oldAlpha = paint.alpha
        paint.color = color
        paint.alpha = oldAlpha
    }

    override fun decorate(canvas: Canvas) {
        anotherDecorator.decorate(canvas)
    }

    override fun toString(): String = "${javaClass.simpleName} + $anotherDecorator"
}

class CircleDecorator(
        private val anotherDecorator: Decorator
) : Decorator by anotherDecorator {
    override val paint: Paint = anotherDecorator.paint

    override fun decorate(canvas: Canvas) {
        anotherDecorator.decorate(canvas)
        canvas.drawCircle(canvas.centerX(), canvas.centerY(), canvas.width / 2f, paint)
    }

    override fun toString(): String = "${javaClass.simpleName} + $anotherDecorator"
}

class TriangleDecorator(
        private val anotherDecorator: Decorator
) : Decorator by anotherDecorator {
    override val paint: Paint = anotherDecorator.paint

    override fun decorate(canvas: Canvas) {
        anotherDecorator.decorate(canvas)
        canvas.drawTriangle(canvas.centerX(), canvas.centerY(), canvas.width, paint)
    }

    override fun toString(): String = "${javaClass.simpleName} + $anotherDecorator"
}

class RectDecorator(
        private val anotherDecorator: Decorator
) : Decorator by anotherDecorator {
    override val paint: Paint = anotherDecorator.paint

    override fun decorate(canvas: Canvas) {
        anotherDecorator.decorate(canvas)
        canvas.drawRect(canvas.clipBounds, paint)
    }

    override fun toString(): String = "${javaClass.simpleName} + $anotherDecorator"
}

class TextDecorator(
        private val anotherDecorator: Decorator,
        private val text: String
) : Decorator by anotherDecorator {
    override val paint: Paint = anotherDecorator.paint

    override fun decorate(canvas: Canvas) {
        anotherDecorator.decorate(canvas)
        paint.textSize = canvas.height * 0.25f
        canvas.drawTextCenter(text, paint)
    }

    override fun toString(): String = "${javaClass.simpleName} + $anotherDecorator"
}
//util extension
fun Canvas.centerX(): Float = width / 2f
fun Canvas.centerY(): Float = height / 2f
fun Canvas.drawTextCenter(text: String, paint: Paint) {
    val r = Rect()
    getClipBounds(r)
    val cHeight = r.height()
    val cWidth = r.width()
    paint.textAlign = Paint.Align.LEFT
    paint.getTextBounds(text, 0, text.length, r)
    val x = cWidth / 2f - r.width() / 2f - r.left
    val y = cHeight / 2f + r.height() / 2f - r.bottom
    drawText(text, x, y, paint)
}
fun Canvas.drawTriangle(x: Float, y: Float, width: Int, paint: Paint) {
    val halfWidth = width / 2
    val path = Path()
    path.moveTo(x, y - halfWidth) // Top
    path.lineTo(x - halfWidth, y + halfWidth) // Bottom left
    path.lineTo(x + halfWidth, y + halfWidth) // Bottom right
    path.lineTo(x, y - halfWidth) // Back to Top
    path.close()

    drawPath(path, paint)
}