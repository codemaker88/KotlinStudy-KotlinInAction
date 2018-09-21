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

    //부 생성자 연습
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

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