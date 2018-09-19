
class DecoratableImageView : ImageView {

    var mDecoratable: Decoratable? = null

    constructor(context:Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

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
