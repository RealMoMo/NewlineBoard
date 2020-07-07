package com.newline.borad.touch

import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.newline.borad.touch.BaseTouchListenerImpl
import com.newline.borad.touch.FloatBarTouchEvent

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/9 9:46
 * @describe
 */
open class ChildViewTouchListenerImpl(context: Context, touchEvent: FloatBarTouchEvent, view: View) :
    BaseTouchListenerImpl(context, touchEvent, view) {

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        currentX = (e2.rawX - downX - view!!.left.toFloat()).toInt()
        currentY = (e2.rawY - downY - view!!.top.toFloat()).toInt()

        dx = currentX -dx
        dy = currentY - dy
        //TODO changed calc to dx dy: maybe using e2.x-e1.x  etc...
        touchEvent!!.onTouchMoving(view!!, currentX, currentY,(dx).toInt(),(dy).toInt())
        dx = currentX
        dy = currentY
        return false
    }


}
