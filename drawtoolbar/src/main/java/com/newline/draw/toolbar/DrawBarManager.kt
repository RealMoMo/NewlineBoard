package com.newline.draw.toolbar

import android.content.Context
import android.widget.LinearLayout
import com.newline.draw.toolbar.widget.DrawBarLayout

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/16 11:34
 * @describe
 */
class DrawBarManager(context: Context) {

    private var mContext : Context?=null
    var drawBarLayout: DrawBarLayout



    init {
        mContext = context
        drawBarLayout = DrawBarLayout(mContext!!)
    }

    /**
     * 设置DrawBar布局排列方向
     */
    fun setDrawBarOrientation(isVertical : Boolean){
        if (isVertical){
            drawBarLayout.orientation = LinearLayout.VERTICAL
        }else{
            drawBarLayout.orientation = LinearLayout.HORIZONTAL
        }
    }

    /**
     * 设置DrawBar 事件监听
     */
    fun setDrawBarEventListener(drawEventListener : DrawBarLayout.DrawEventListener){
        drawBarLayout.setDrawEventListener(drawEventListener)
    }


    /**
     * 释放资源
     */
    fun release(){

        mContext = null
        drawBarLayout.release()

    }


}