package frogsm.kotlin.problem3

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

class DecoratableImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    var mDecoratable: Decoratable? = null

    fun setDecoratable(decoratable: Decoratable) {
        mDecoratable = decoratable
    }

    fun getDecoratable() : Decoratable? {
        return mDecoratable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mDecoratable?.onDraw(canvas)
    }

}