package com.newline.borad.widget.note

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.jacky.commondraw.views.doodleview.DoodleView
import com.newline.borad.R

import kotlin.math.absoluteValue


/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/8 14:36
 * @describe
 *
 * 假4k适配用
 */
class FakeNoteWidget : FrameLayout, View.OnClickListener {

    private lateinit var doodleView: DoodleView
    private lateinit var ivExit : AppCompatImageView

    private var noteEventListener : NoteEventListener? = null
    private var noteGestureListener : NoteGestureListener? = null

    private var isSelfTouch = false
    private var timeStamp : Long = 0L
    private var lastX = -1F
    private var lastY = -1F
    private var rawX = -1F
    private var rawY = -1F

    private var velocityTracker: VelocityTracker? = null
    //目前，只考量y方向的加速度
    private var minVelocity : Int
    private var vX:Float = 0F
    private var vY:Float = 0F

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        LayoutInflater.from(context).inflate(R.layout.layout_fake_note,this,true)
        minVelocity = context.resources.displayMetrics.heightPixels/6
        initView()
    }


    private fun initView() {
        doodleView = findViewById(R.id.doodleView)

        ivExit = findViewById(R.id.iv_exit)
        ivExit.setOnClickListener(this)
    }



    /**
     * 获取NoteView书写内容
     * @param needBg 背景是否需要绘制进Bitmap
     * @return Bitmap,若没有书写内容则Bitmap为null
     */
    fun getNoteContent(needBg : Boolean):Bitmap?{
        return doodleView.newWholeBitmap(needBg)
    }

    /**
     * 设置Note事件监听
     */
    fun setNoteEventListener(listener: NoteEventListener){
        noteEventListener = listener
    }

    /**
     * 设置Note手势监听
     */
    fun setNoteGestureListener(listener: NoteGestureListener){
        noteGestureListener = listener
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when( MotionEvent.ACTION_MASK and ev!!.action){
            MotionEvent.ACTION_DOWN->{
                lastX = ev.getX()
                lastY = ev.getY()
                velocityTracker = VelocityTracker.obtain()
                velocityTracker?.addMovement(ev)
                timeStamp = System.currentTimeMillis()
            }
            MotionEvent.ACTION_POINTER_DOWN->{
                if(isSelfTouch){
                    return true
                }
                if(System.currentTimeMillis() - timeStamp <200){
                    isSelfTouch = true
                }else{
                    isSelfTouch = false
                }

            }
        }
        return isSelfTouch
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        rawX = event!!.getRawX()
        rawY = event!!.getRawY()
        when(MotionEvent.ACTION_MASK and event!!.action){
            MotionEvent.ACTION_MOVE->{

                noteGestureListener?.windowMove((rawX-lastX).toInt(),(rawY-lastY).toInt())
                velocityTracker?.addMovement(event)
            }
            MotionEvent.ACTION_UP ->{

                isSelfTouch = false
                doodleView.refresh()
                velocityTracker?.let {
                    it.addMovement(event)
                    it.computeCurrentVelocity(200)
                    vX = it.xVelocity
                    vY = it.yVelocity

                    if(vY.absoluteValue>= minVelocity ){
                        if((rawY-lastY)>=minVelocity){
                            noteGestureListener?.fling(vX,vY)
                        }
                    }

                }
            }
            MotionEvent.ACTION_POINTER_UP->{

            }
            MotionEvent.ACTION_CANCEL->{
                isSelfTouch = false
                velocityTracker?.recycle()
                velocityTracker = null
            }
        }
        return true
    }


    interface NoteGestureListener{
        /**
         * @param x :notewidget 对应window 左上角坐标x
         * @param y :notewidget 对应window 左上角坐标y
         */
        fun windowMove(x:Int, y:Int)
        /**
         * @param velocityX:x轴移动的加速度
         * @param velocityY:y轴移动的加速度
         */
        fun fling(velocityX:Float,velocityY: Float)
    }

    interface NoteEventListener{

        fun noteExit()
    }

    override fun onClick(v: View?) {
        when(v){
            ivExit->{
                noteEventListener?.noteExit()
            }
        }
    }


}