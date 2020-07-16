package com.newline.draw.toolbar.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.newline.draw.toolbar.R
import com.newline.draw.toolbar.data.DrawOperation
import kotlinx.android.synthetic.main.layout_drawbar.view.*

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/16 9:14
 * @describe
 */

class DrawBarLayout : LinearLayout, View.OnClickListener, View.OnLongClickListener {

    /**
     * DrawBarLayout 事件监听接口
     */
    interface DrawEventListener {

        /**
         * DrawBarLayout ItemView 点击事件
         * @param barLayout
         * @param clickView  : 被点击的控件
         * @param operation  : Draw操作意图类型
         */
        fun onDrawBarItemClick(
            barLayout: DrawBarLayout,
            clickView: View,
            @DrawOperation operation: Int
        )

        /**
         * DrawBarLayout ItemView 长按点击事件
         * @param barLayout
         * @param longClickView  : 被长按点击的控件
         * @param operation  : Draw操作意图类型
         */
        fun onDrawBarItemLongClick(
            barLayout: DrawBarLayout,
            longClickView: View,
            @DrawOperation operation: Int
        )
    }


    private var eventListener: DrawEventListener? = null

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


    fun setDrawEventListener(drawEventListener: DrawEventListener) {
        eventListener = drawEventListener
    }

    fun release(){
        eventListener =null
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
        LayoutInflater.from(context).inflate(R.layout.layout_drawbar, this, true)
        iv_pen.setOnClickListener(this)
        iv_highlightpen.setOnClickListener(this)
        iv_earse.setOnClickListener(this)
        iv_rollback.setOnClickListener(this)
        iv_recover.setOnClickListener(this)

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
    }


}