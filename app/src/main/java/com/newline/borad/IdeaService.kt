package com.newline.borad

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import com.jacky.commondraw.model.stroke.InsertableObjectStroke
import com.jacky.commondraw.views.doodleview.DoodleEnum
import com.newline.borad.utils.BitmapUtils
import com.newline.borad.utils.ScreenAdapterUtils
import com.newline.borad.widget.note.FakeNoteWidget
import com.newline.borad.widget.note.NoteWidget
import com.newline.draw.toolbar.DrawBarManager
import com.newline.draw.toolbar.data.DrawOperation
import com.newline.draw.toolbar.listeners.DrawEventListener
import com.newline.draw.toolbar.widget.BaseDrawBarLayout
import kotlinx.android.synthetic.main.layout_note.view.*


/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/2 16:14
 * @describe
 */
class IdeaService : Service(), FakeNoteWidget.NoteGestureListener,
    FakeNoteWidget.NoteEventListener {

    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams
    private lateinit var noteView : FakeNoteWidget
    //private lateinit var drawBarManager: DrawBarManager

    /**
     * NoteView 最小宽度
     */
    private val ideaMinWidth :Int by lazy {
        (resources.displayMetrics.widthPixels *0.566f).toInt()*2
    }
    /**
     * NoteView 最小高度
     */
    private val ideaMinHeight :Int by lazy {
        (resources.displayMetrics.heightPixels *0.566f).toInt()*2
    }

    /**
     * NoteView 宽度
     */
    private var ideaWidth : Int = 0

    /**
     * NoteView 高度
     */
    private var ideaHeight : Int = 0

    private var maxX : Int = 0
    private var maxY : Int = 0

    /**
     * WindowManager是否添加了View标记
     */
    private var isAddView = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        ScreenAdapterUtils.setOtherScreenAdapter(this)
        initView()
        initWindowManager()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(!isAddView){
            isAddView = true

            mLayoutParams.x = 0
            mLayoutParams.y = 0
            mLayoutParams.gravity = Gravity.CENTER
            mWindowManager.addView(noteView,mLayoutParams)


            mLayoutParams.gravity = Gravity.START or Gravity.TOP

            noteView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        noteView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        //初始化位置坐标
                        val location = IntArray(2)
                        noteView.getLocationOnScreen(location)
                        mLayoutParams.x = location[0]/2
                        mLayoutParams.y = location[1]/2
                        mWindowManager.updateViewLayout(noteView,mLayoutParams)


                    }

                }
            )

        }
        
        return super.onStartCommand(intent, flags, startId)
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
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

        ideaWidth = ideaMinWidth
        ideaHeight = ideaMinHeight
        mLayoutParams.width = ideaWidth
        mLayoutParams.height = ideaHeight

        maxX = resources.displayMetrics.widthPixels*2-ideaWidth
        maxY = resources.displayMetrics.heightPixels*2-ideaHeight

    }

    private fun initView() {
        noteView = FakeNoteWidget(this)
        //设置手势监听
        noteView.setNoteGestureListener(this)
        //设置动画监听
        noteView.setNoteEventListener(this)


    }

    override fun onDestroy() {
        super.onDestroy()
        isAddView = false
        mWindowManager.removeView(noteView)

    }

    override fun windowMove(x: Int, y: Int) {
        if(x>maxX){
            mLayoutParams.x = maxX/2
        }else{
            mLayoutParams.x = x/2
        }

        if(y>maxY){
            mLayoutParams.y = maxY/2
        }else{
            mLayoutParams.y = y/2
        }

        mWindowManager.updateViewLayout(noteView,mLayoutParams)
    }

    override fun fling(velocityX: Float, velocityY: Float) {
        if(velocityY > 0){
           tempExit()
        }

    }


    private fun tempExit(){
        isAddView = false
        mWindowManager.removeViewImmediate(noteView)

    }

    override fun noteExit() {
        //TODO save idea content in workthread then to stopself service
        BitmapUtils.saveBitmap(noteView.getNoteContent(true),"test.png")

        stopSelf()
    }


}