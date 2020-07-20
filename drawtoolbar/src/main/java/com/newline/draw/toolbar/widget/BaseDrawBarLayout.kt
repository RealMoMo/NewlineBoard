package com.newline.draw.toolbar.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.newline.draw.toolbar.listeners.DrawEventListener

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/20 11:15
 * @describe
 */
abstract class BaseDrawBarLayout : LinearLayout {

    protected var eventListener: DrawEventListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        inflateBarLayout()
    }

    abstract fun inflateBarLayout()



    fun setDrawEventListener(drawEventListener: DrawEventListener) {
        eventListener = drawEventListener
    }

    fun release(){
        eventListener =null
    }
}