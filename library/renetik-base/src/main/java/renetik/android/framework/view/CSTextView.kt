package renetik.android.framework.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.makeMeasureSpec
import renetik.android.R


class CSTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    //    private val minWidth: Int
//    private val maxWidth: Int
    private val dispatchState: Boolean

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CSLayout, 0, 0)
        try {
//            minWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_minWidth, -1)
//            maxWidth = attributes.getDimensionPixelSize(R.styleable.CSLayout_maxWidth, -1)
            dispatchState = attributes.getBoolean(R.styleable.CSLayout_dispatchState, true)
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (minWidth != -1 && measuredWidth < minWidth) {
            super.onMeasure(makeMeasureSpec(minWidth, EXACTLY), heightMeasureSpec)
        } else if (maxWidth != -1 && measuredWidth > maxWidth) {
            super.onMeasure(makeMeasureSpec(maxWidth, EXACTLY), heightMeasureSpec)
        }
    }

    override fun dispatchSetActivated(activated: Boolean) {
        if (dispatchState) super.dispatchSetActivated(activated)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        if (dispatchState) super.dispatchSetSelected(selected)
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        if (dispatchState) super.dispatchSetPressed(pressed)
    }
}