package com.newline.borad.touch

import android.view.GestureDetector
import android.view.View

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/9 9:29
 * @describe
 */
interface FloatBarTouchListener : View.OnTouchListener, GestureDetector.OnGestureListener {


    fun releaseRes()
}
