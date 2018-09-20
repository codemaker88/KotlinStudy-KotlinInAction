package study.kotlin.problem3.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by CodeMaker on 2018-09-10.
 */

class DecoratableImageView : ImageView {

    var mDecoratable: Decoratable? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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