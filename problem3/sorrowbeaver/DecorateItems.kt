package study.kotlin.problem3.view

import android.graphics.*
import study.kotlin.problem3.view.Decorate.Shape.CIRCLE
import study.kotlin.problem3.view.Decorate.Shape.RECT
import study.kotlin.problem3.view.Decorate.Shape.TRIANGLE

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
class DefaultDecorate : Decoratable {
  override val paint: Paint = Paint()
  override fun toString(): String {
    return javaClass.simpleName
  }
}

class ColorDecorate(decoratable: Decoratable, val color: Int) : Decoratable by decoratable {

  init {
    paint.color = color
  }

  override fun toString(): String {
    return ""
  }
}

class AlphaDecorate(
  val alpha: Int,
  decoratable: Decoratable = DefaultDecorate()
) : Decoratable by decoratable {

  init {
    paint.alpha = alpha
  }

  override fun toString(): String {
    return ""
  }

}

class RectDecorate(
  private val decoratable: Decoratable = DefaultDecorate()
) : Decoratable by decoratable {

  init {
    paint.style = Paint.Style.FILL
  }

  override fun toString(): String {
    return ""
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    decoratable.onDraw(canvas)
    canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
  }
}

class CircleDecorate(
  private val decoratable: Decoratable = DefaultDecorate()
) : Decoratable by decoratable {

  init {
    paint.style = Paint.Style.FILL
  }

  override fun toString(): String {
    return ""
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    decoratable.onDraw(canvas)
    val (cx, cy) = canvas.center()
    canvas.drawCircle(cx, cy, cx, paint)
  }
}

class TriangleDecorate(
  private val decoratable: Decoratable = DefaultDecorate()
) : Decoratable by decoratable {

  private val path = Path()

  init {
    paint.style = Paint.Style.FILL
  }

  override fun toString(): String {
    return ""
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    decoratable.onDraw(canvas)
    val (cx, _) = canvas.center()
    with(path) {
      reset()
      moveTo(cx, 0f)
      lineTo(0f, canvas.height.toFloat())
      lineTo(canvas.width.toFloat(), canvas.height.toFloat())
      lineTo(cx, 0f)
      close()
    }
    canvas.drawPath(path, paint)
  }
}

class TextDecorate(
  val text: String,
  decoratable: Decoratable = DefaultDecorate()
) : Decoratable by decoratable {

  private val canvasRect = Rect()

  init {
    paint.textSize = 100f
    paint.textAlign = Paint.Align.LEFT
  }

  override fun toString(): String {
    return ""
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.getClipBounds(canvasRect)
    val cHeight = canvasRect.height()
    val cWidth = canvasRect.width()
    paint.getTextBounds(text, 0, text.length, canvasRect)
    val x = cWidth / 2f - canvasRect.width() / 2f - canvasRect.left
    val y = cHeight / 2f + canvasRect.height() / 2f - canvasRect.bottom
    canvas.drawText(text, x, y, paint)
  }
}

//companion object 연습
class Decorate {
  enum class Shape {
    RECT, CIRCLE, TRIANGLE
  }

  companion object {
    fun getDefault(shape: Shape): Decoratable {
      return when(shape) {
        RECT -> RectDecorate()
        CIRCLE -> CircleDecorate()
        TRIANGLE -> TriangleDecorate()
      }
    }

    fun getDefaultText(text: String): Decoratable {
      return TextDecorate(text)
    }
  }
}

// onDraw 중에 객체 생성 안좋지만.. >:(
fun Canvas.center() = (width / 2).toFloat() to (height / 2).toFloat()