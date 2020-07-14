package com.newline.borad

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.*
import com.newline.borad.touch.FloatBarTouchEvent
import com.newline.borad.widget.note.NoteWidget


/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/2 16:14
 * @describe
 */
class IdeaService : Service(),NoteWidget.NoteGestureListener, NoteWidget.NoteAnimationListener {
    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams
    private lateinit var noteView : NoteWidget


    private val minWidth :Int by lazy {
        (resources.displayMetrics.widthPixels /2.5f).toInt()
    }

    private val minHeight :Int by lazy {
        (resources.displayMetrics.heightPixels /1.5f).toInt()
    }

    private var width : Int = 0
    private var height : Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        initView()
        initWindowManager()

       noteView.startShowNoteAnimation()

    }

    private fun initWindowManager() {
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }

        mLayoutParams.format = PixelFormat.TRANSLUCENT
        mLayoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mLayoutParams.gravity = Gravity.CENTER

        width = minWidth
        height = minHeight
        mLayoutParams.width = width
        mLayoutParams.height = height


        mWindowManager.addView(noteView,mLayoutParams)

        mLayoutParams.gravity = Gravity.START or Gravity.TOP


        noteView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    noteView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    //init postion coordinate
                    val location = IntArray(2)
                    noteView.getLocationOnScreen(location)
                    mLayoutParams.x = location[0]
                    mLayoutParams.y = location[1]


                }

            }
        )
    }

    private fun initView() {
        noteView = NoteWidget(this)
        noteView.setNoteGestureListener(this)
        noteView.setNoteAnimationListener(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        mWindowManager.removeView(noteView)
    }

    override fun windowMove(x: Int, y: Int) {

        mLayoutParams.x = x
        mLayoutParams.y = y

        mWindowManager.updateViewLayout(noteView,mLayoutParams)
    }

    override fun fling(velocityX: Float, velocityY: Float) {
        if(velocityY > 0){
            noteView.startHideVortexAnimation()
        }

    }

    override fun showAnimationStart() {
    }

    override fun showAnimationEnd() {
    }

    override fun hideAnimationStart() {
    }

    override fun hideAnimationEnd() {
        //TODO save idea content in workthread then to stopself service
        stopSelf()
    }

    override fun hideVortexAnimationStart() {
    }

    override fun hideVortexAnimationEnd() {
        //TODO save idea content in workthread then to stopself service
        stopSelf()
    }


}