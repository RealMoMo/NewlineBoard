package com.newline.draw.toolbar.listeners

import android.view.View
import com.newline.draw.toolbar.data.DrawOperation
import com.newline.draw.toolbar.widget.BaseDrawBarLayout
import com.newline.draw.toolbar.widget.VerticalDrawBarLayout

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/20 11:13
 * @describe  DrawBarLayout 事件监听接口
 */
interface DrawEventListener {


    /**
     * DrawBarLayout ItemView 点击事件
     * @param barLayout
     * @param clickView  : 被点击的控件
     * @param operation  : Draw操作意图类型
     */
    fun onDrawBarItemClick(
        barLayout: BaseDrawBarLayout,
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
        barLayout: BaseDrawBarLayout,
        longClickView: View,
        @DrawOperation operation: Int
    )
}