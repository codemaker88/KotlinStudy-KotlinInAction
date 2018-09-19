
//인터페이스 연습
interface Decoratable {
    //인터페이스 추상 프로퍼티
    val paint: Paint

    fun onDraw(canvas: Canvas)
    override fun toString(): String
}

interface Propertiable {
    fun addProperty(paint: Paint)
}

//클래스 위임 연습
open class DefaultDecorate(override val paint: Paint = Paint()) : Decoratable {

    protected var decorate: Decoratable? = null

    constructor(decorate: Decoratable) : this() {
        this.decorate = decorate
    }

    fun decorateString(): String {
        return decorate?.let { "+ $it" } ?: "+ DefaultDecorate"
    }

    override fun toString(): String {
        return "${javaClass.simpleName} ${decorateString()}"
    }

    @CallSuper
    override fun onDraw(canvas: Canvas) {
        decorate?.let {
            it.paint.color = paint.color
            it.paint.alpha = paint.alpha
        }
        decorate?.onDraw(canvas)
    }
}

class ColorDecorate(decorate: Decoratable, val color: Int) : DefaultDecorate(decorate), Propertiable {
    override fun addProperty(paint: Paint) {
        val alpha = this.paint.alpha
        paint.color = color
        paint.alpha = alpha
    }
    override fun onDraw(canvas: Canvas) {
        addProperty(paint)
        super.onDraw(canvas)
    }
}

class AlphaDecorate(decorate: Decoratable, val alpha: Int) : DefaultDecorate(decorate), Propertiable {
    override fun addProperty(paint: Paint) {
        paint.alpha = alpha
    }
    override fun onDraw(canvas: Canvas) {
        addProperty(paint)
        super.onDraw(canvas)
    }
}


class RectDecorate : DefaultDecorate {
    constructor() : super()
    constructor(decorate: Decoratable) : super(decorate)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = canvas.width / 10
        val rect = Rect(padding, padding, canvas.width - padding, canvas.height - padding)
        canvas.drawRect(rect, paint)
    }
}

class CircleDecorate : DefaultDecorate {

    constructor() : super()
    constructor(decorate: Decoratable) : super(decorate)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = if (canvas.width > canvas.height) {
            canvas.width / 2
        } else {
            canvas.height / 2
        }.toFloat()
        val (x, y) = (canvas.width.toFloat() / 2 to canvas.height.toFloat() / 2)
        canvas.drawCircle(x, y, radius, paint)
    }
}

class TriangleDecorate : DefaultDecorate {
    constructor() : super()
    constructor(decorate: Decoratable) : super(decorate)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = (canvas.width / 2).toFloat()
        val x = canvas.width.toFloat()
        val y = canvas.height.toFloat()
        val path = Path()
        path.moveTo(centerX, 0f) // Top
        path.lineTo(0f, y) // Bottom left
        path.lineTo(x, y) // Bottom right
        path.lineTo(centerX, 0f) // Back to Top
        path.close()
        canvas.drawPath(path, paint)
    }
}

class TextDecorate(val text: String) : DefaultDecorate() {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val (x, y) = (canvas.width.toFloat() / 2 to canvas.height.toFloat() / 2)
        paint.textSize = 64f
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
