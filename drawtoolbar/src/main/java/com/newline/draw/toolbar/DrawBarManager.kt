package com.newline.draw.toolbar

import android.content.Context
import com.newline.draw.toolbar.listeners.DrawEventListener
import com.newline.draw.toolbar.widget.BaseDrawBarLayout
import com.newline.draw.toolbar.widget.HorizontalDrawBarLayout
import com.newline.draw.toolbar.widget.VerticalDrawBarLayout

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
    private var horizontalDrawBarLayout: HorizontalDrawBarLayout
    private var verticalDrawBarLayout: VerticalDrawBarLayout

    private var currentDrawBarLayout : BaseDrawBarLayout?=null

    init {
        mContext = context
        horizontalDrawBarLayout = HorizontalDrawBarLayout(mContext!!)
        verticalDrawBarLayout = VerticalDrawBarLayout(mContext!!)
    }

    /**
     * 设置DrawBar布局排列方向
     * @param isVertical
     * @return BaseDrawBarLayout
     */
    fun setDrawBarOrientation(isVertical : Boolean): BaseDrawBarLayout {
        if (isVertical){
            currentDrawBarLayout = verticalDrawBarLayout
        }else{
            currentDrawBarLayout = horizontalDrawBarLayout
        }
        return currentDrawBarLayout!!
    }

    fun getDrawBarLayout(): BaseDrawBarLayout{
        if(currentDrawBarLayout == null){
            currentDrawBarLayout = horizontalDrawBarLayout
        }
        return currentDrawBarLayout!!
    }

    /**
     * 设置DrawBar 事件监听
     */
    fun setDrawBarEventListener(drawEventListener : DrawEventListener){
        horizontalDrawBarLayout.setDrawEventListener(drawEventListener)
        verticalDrawBarLayout.setDrawEventListener(drawEventListener)
    }


    /**
     * 释放资源
     */
    fun release(){

        mContext = null
        horizontalDrawBarLayout.release()
        verticalDrawBarLayout.release()

    }


}