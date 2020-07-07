package com.newline.borad.touch


import android.content.Context
import android.view.View

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/9 9:20
 * @describe
 */
class FloatBarTouchManager private constructor() {


    private var childTouchListenerList : MutableMap<View, FloatBarTouchListener>?=null
    private var layoutTouchListenerList : MutableMap<View, FloatBarTouchListener>?=null
    private var childTouchFlingListenerList : MutableMap<View, FloatBarTouchListener>?=null
    private var layoutTouchFlingListenerList : MutableMap<View, FloatBarTouchListener>?=null

    fun init() {
        childTouchListenerList = LinkedHashMap(10)
        layoutTouchListenerList = LinkedHashMap(4)
        childTouchFlingListenerList = LinkedHashMap(2)
        layoutTouchFlingListenerList = LinkedHashMap(2)
    }


    fun addChildViewTouchListener(context: Context, event: FloatBarTouchEvent, view: View) {
        checkIsInit()
        if(!checkExistKey(childTouchListenerList!!,view)){
            childTouchListenerList!!.put(view, ChildViewTouchListenerImpl(context, event, view))
        }

    }


    fun addLayoutViewTouchListener(context: Context, event: FloatBarTouchEvent, view: View) {
        checkIsInit()
        if(!checkExistKey(layoutTouchListenerList!!,view)){
            layoutTouchListenerList!!.put(view, LayoutViewTouchListenerImpl(context, event, view))
        }

    }




    fun addChildViewTouchWithFlingListener(context: Context, event: FloatBarTouchEvent, view: View) {
        checkIsInit()
        if(!checkExistKey(childTouchFlingListenerList!!,view)){
            childTouchFlingListenerList!!.put(view,
                ChildViewTouchWithFlingListenerImpl(context, event, view)
            )
        }

    }


    fun addLayoutViewTouchWithFlingListener(context: Context, event: FloatBarTouchEvent, view: View) {
        checkIsInit()
        if(!checkExistKey(layoutTouchFlingListenerList!!,view)){
            layoutTouchFlingListenerList!!.put(view,
                LayoutViewTouchWithFlingListenerImpl(context, event, view)
            )
        }

    }


    fun removeChildViewTouchListener(view :View){
        checkIsInit()
        childTouchListenerList!!.get(view)?.releaseRes()
        childTouchListenerList!!.remove(view)

    }

    fun removeChildViewTouchFlingListener(view :View){
        checkIsInit()
        childTouchFlingListenerList!!.get(view)?.releaseRes()
        childTouchFlingListenerList!!.remove(view)

    }

    fun removeLayoutViewTouchListener(view :View){
        checkIsInit()
        layoutTouchListenerList!!.get(view)?.releaseRes()
        layoutTouchListenerList!!.remove(view)
    }

    fun removeLayoutViewTouchFlingListener(view :View){
        checkIsInit()
        layoutTouchFlingListenerList!!.get(view)?.releaseRes()
        layoutTouchFlingListenerList!!.remove(view)
    }

    private fun checkIsInit() {
        checkNotNull(childTouchListenerList) { "Please call the init() method" }
    }

    private fun checkExistKey(map : Map<View, FloatBarTouchListener>, key:View):Boolean{
        return map.containsKey(key)
    }


    fun releaseRes() {
        checkIsInit()
        for (floatBarTouchListener in childTouchListenerList!!) {
            floatBarTouchListener.value.releaseRes()
        }

        for (floatBarTouchListener in layoutTouchListenerList!!) {
            floatBarTouchListener.value.releaseRes()
        }

        for (floatBarTouchListener in childTouchFlingListenerList!!) {
            floatBarTouchListener.value.releaseRes()
        }

        for (floatBarTouchListener in layoutTouchFlingListenerList!!) {
            floatBarTouchListener.value.releaseRes()
        }


        childTouchListenerList!!.clear()
        layoutTouchListenerList!!.clear()
        childTouchFlingListenerList!!.clear()
        layoutTouchFlingListenerList!!.clear()
    }

    companion object {

        private var mInstance: FloatBarTouchManager? = null

        val instance: FloatBarTouchManager
            get() {
                if (mInstance == null) {
                    synchronized(FloatBarTouchManager::class.java) {
                        if (mInstance == null) {
                            mInstance = FloatBarTouchManager()
                        }
                    }
                }
                return mInstance!!
            }
    }
}
