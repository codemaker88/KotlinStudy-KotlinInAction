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

  constructor(context: Context) : super(context)
  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
  constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int)
      : super(context, attributeSet, defStyleAttr)

  fun setDecoratable(decoratable: Decoratable) {
    mDecoratable = decoratable
  }

  fun getDecoratable(): Decoratable? {
    return mDecoratable
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    mDecoratable?.onDraw(canvas)
  }
}