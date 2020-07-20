package com.newline.draw.toolbar.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.newline.draw.toolbar.R
import com.newline.draw.toolbar.data.DrawOperation
import kotlinx.android.synthetic.main.layout_horizontal_drawbar.view.*

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/16 9:14
 * @describe
 */

class HorizontalDrawBarLayout : BaseDrawBarLayout, View.OnClickListener, View.OnLongClickListener {




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
        initView()
        initDrawOperationData()
    }

    override fun inflateBarLayout() {
        LayoutInflater.from(context).inflate(R.layout.layout_horizontal_drawbar, this, true)
    }

    override fun onClick(v: View?) {
        v?.let {
            eventListener?.onDrawBarItemClick(this,it,it.tag as Int)
        }

    }

    override fun onLongClick(v: View?): Boolean {
        v?.let {
            eventListener?.onDrawBarItemLongClick(this,it,it.tag as Int)
        }
        return true
    }


    private fun initView() {

        iv_pen.setOnClickListener(this)
        iv_highlightpen.setOnClickListener(this)
        iv_earse.setOnClickListener(this)
        iv_rollback.setOnClickListener(this)
        iv_recover.setOnClickListener(this)
        iv_clear.setOnClickListener(this)
        iv_exit.setOnClickListener(this)

        iv_pen.setOnLongClickListener(this)
        iv_highlightpen.setOnLongClickListener(this)
        iv_earse.setOnLongClickListener(this)
        iv_rollback.setOnLongClickListener(this)
        iv_recover.setOnLongClickListener(this)
    }


    private fun initDrawOperationData() {
        iv_pen.tag = DrawOperation.SOFT_PEN_WITH_STROKE
        iv_highlightpen.tag = DrawOperation.HIGHTLIGHT_PEN
        iv_earse.tag = DrawOperation.EARSE
        iv_rollback.tag = DrawOperation.ROLLBACK
        iv_recover.tag = DrawOperation.RECOVER
        iv_clear.tag = DrawOperation.CLEAR
        iv_exit.tag = DrawOperation.EXIT
    }


}