package com.newline.borad.touch

import android.view.View
import com.newline.borad.touch.FloatBarTouchEvent

/**
 * @name FloatBar
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2019/11/9 14:54
 * @describe
 */
open class FloatBarTouchEventImpl : FloatBarTouchEvent {
    override fun onFling(view: View, x: Int, y: Int, velocityX: Float, velocityY: Float) {
    }

    override fun onTouchDown(view: View) {
    }

    override fun onTouchUp(view: View) {
    }

    override fun onTouchCancel(view: View) {
    }

    override fun onTouchClick(view: View) {
    }

    override fun onTouchLongClick(view: View) {
    }

    override fun onTouchMoving(view: View, parentX: Int, parentY: Int, dx: Int, dy: Int) {
    }
}