package com.newline.borad

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import com.jacky.commondraw.model.stroke.InsertableObjectStroke
import com.jacky.commondraw.views.doodleview.DoodleEnum
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
class IdeaService : Service(),NoteWidget.NoteGestureListener, NoteWidget.NoteAnimationListener
     {

    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams
    private lateinit var noteView : NoteWidget
    //private lateinit var drawBarManager: DrawBarManager

    /**
     * NoteView 最小宽度
     */
    private val ideaMinWidth :Int by lazy {
        (resources.displayMetrics.widthPixels /2.5f).toInt()
    }

    /**
     * NoteView 最小高度
     */
    private val ideaMinHeight :Int by lazy {
        (resources.displayMetrics.heightPixels /1.25f).toInt()
    }

    /**
     * NoteView 宽度
     */
    private var ideaWidth : Int = 0

    /**
     * NoteView 高度
     */
    private var ideaHeight : Int = 0

    /**
     * WindowManager是否添加了View标记
     */
    private var isAddView = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
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

//            //TODO realmo test
//            mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
//            mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
//            mWindowManager.addView(drawBarManager.setDrawBarOrientation(true),mLayoutParams)
//            //============

            mLayoutParams.gravity = Gravity.START or Gravity.TOP


            noteView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        noteView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        //初始化位置坐标
                        val location = IntArray(2)
                        noteView.getLocationOnScreen(location)
                        mLayoutParams.x = location[0]
                        mLayoutParams.y = location[1]


                    }

                }
            )

            noteView.startShowNoteAnimation()
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
        mLayoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

        ideaWidth = ideaMinWidth
        ideaHeight = ideaMinHeight
        mLayoutParams.width = ideaWidth
        mLayoutParams.height = ideaHeight



    }

    private fun initView() {
        noteView = NoteWidget(this)
        //设置手势监听
        noteView.setNoteGestureListener(this)
        //设置动画监听
        noteView.setNoteAnimationListener(this)


//        drawBarManager = DrawBarManager(this)
//        drawBarManager.setDrawBarEventListener(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        isAddView = false
        mWindowManager.removeView(noteView)
        //mWindowManager.removeView(drawBarManager.getDrawBarLayout())
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

        //tempExit()

        stopSelf()
    }

    override fun hideVortexAnimationStart() {
    }

    override fun hideVortexAnimationEnd() {
        //TODO save idea content in workthread then to stopself service

        tempExit()

        //stopSelf()
    }
    
    
    private fun tempExit(){
        isAddView = false
        mWindowManager.removeViewImmediate(noteView)
        //mWindowManager.removeViewImmediate(drawBarManager.getDrawBarLayout())
    }

//    override fun onDrawBarItemClick(barLayout: BaseDrawBarLayout, clickView: View, operation: Int) {
//        when(operation){
//            DrawOperation.SOFT_PEN_WITH_STROKE->{
//                noteView.doodleView.inputMode = DoodleEnum.InputMode.DRAW
//                noteView.doodleView.strokeType = InsertableObjectStroke.STOKRE_TYPE_SOFT_PEN
//            }
//            DrawOperation.HIGHTLIGHT_PEN->{
//                noteView.doodleView.inputMode = DoodleEnum.InputMode.DRAW
//                noteView.doodleView.strokeType = InsertableObjectStroke.STROKE_TYPE_AIRBRUSH
//            }
//            DrawOperation.EARSE->{
//                noteView.doodleView.inputMode = DoodleEnum.InputMode.ERASE
//            }
//            DrawOperation.ROLLBACK->{
//                noteView.doodleView.undo()
//            }
//            DrawOperation.RECOVER->{
//                noteView.doodleView.redo()
//            }
//            DrawOperation.CLEAR->{
//                noteView.doodleView.clear()
//            }
//            DrawOperation.EXIT->{
//                noteView.startHideNoteAnimation()
//            }
//        }
//    }
//
//    override fun onDrawBarItemLongClick(
//        barLayout: BaseDrawBarLayout,
//        longClickView: View,
//        operation: Int
//    ) {
//    }


}