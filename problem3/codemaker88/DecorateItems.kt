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

class ColorDecorate(val decoratable: Decoratable, val color: Int) : Decoratable by decoratable {
    init {
        val prevAlpha = paint.alpha
        paint.color = color
        paint.alpha = prevAlpha
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decoratable"
    }
}

class AlphaDecorate(val decoratable: Decoratable, val alpha: Int) : Decoratable by decoratable {
    init {
        paint.alpha = alpha
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decoratable"
    }
}

class RectDecorate(val decoratable: Decoratable) : Decoratable by decoratable {

    init {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        decoratable.onDraw(canvas)
        canvas.drawRect(canvas.clipBounds, paint)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decoratable"
    }
}

class CircleDecorate(val decoratable: Decoratable) : Decoratable by decoratable {

    init {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        decoratable.onDraw(canvas)
        canvas.drawCircle(canvas.width / 2F, canvas.height / 2F, canvas.width / 2F, paint)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decoratable"
    }
}

class TriangleDecorate(val decoratable: Decoratable) : Decoratable by decoratable {

    init {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        decoratable.onDraw(canvas)
        val path = Path()
        val (point1x, point1y) = Pair(0F, canvas.height.toFloat())
        val (point2x, point2y) = Pair(canvas.width.toFloat(), canvas.height.toFloat())
        val (point3x, point3y) = Pair(canvas.width.toFloat() / 2F, 0F)
        path.moveTo(point1x, point1y)
        path.lineTo(point2x, point2y)
        path.lineTo(point3x, point3y)
        path.lineTo(point1x, point1y)
        canvas.drawPath(path, paint)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decoratable"
    }
}

class TextDecorate(val decoratable: Decoratable, val text: String) : Decoratable by decoratable {
    init {
        paint.textSize = 100F
        paint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        decoratable.onDraw(canvas)
        canvas.drawText(text, canvas.width / 2F, canvas.height.toFloat() / 2F + paint.textSize / 2, paint)
    }

    override fun toString(): String {
        return "${javaClass.simpleName} + $decoratable"
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
                Shape.RECT -> getDefaultRect()
                Shape.CIRCLE -> getDefaultCircle()
                Shape.TRIANGLE -> getDefaultTriangle()
            }
        }

        fun getDefaultRect() = RectDecorate(DefaultDecorate())
        fun getDefaultCircle() = CircleDecorate(DefaultDecorate())
        fun getDefaultTriangle() = TriangleDecorate(DefaultDecorate())
        fun getDefaultText(string: String) = TextDecorate(DefaultDecorate(), string)
    }
}