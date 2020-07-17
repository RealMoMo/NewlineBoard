package com.newline.borad.widget.note

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.gplibs.magicsurfaceview.MagicMultiSurface
import com.gplibs.magicsurfaceview.MagicSurface
import com.gplibs.magicsurfaceview.MagicSurfaceView
import com.gplibs.magicsurfaceview.MagicUpdaterListener
import com.jacky.commondraw.views.doodleview.DoodleView
import com.newline.borad.R
import com.newline.borad.animation.magic.Direction
import com.newline.borad.animation.magic.MultiScrapUpdater
import com.newline.borad.animation.magic.VortexAnimUpdater
import kotlin.math.absoluteValue


/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/8 14:36
 * @describe
 */
class NoteWidget : FrameLayout, View.OnClickListener {

    private lateinit var magicSurfaceView: MagicSurfaceView
    private lateinit var doodleView: DoodleView
    private lateinit var ivExit : AppCompatImageView

    private var noteAnimationListener : NoteAnimationListener? = null
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
        LayoutInflater.from(context).inflate(R.layout.layout_note,this,true)
        minVelocity = context.resources.displayMetrics.heightPixels/6
        initView()
    }


    private fun initView() {
        magicSurfaceView = findViewById(R.id.magic_surface_view)
        doodleView = findViewById(R.id.doodleView)

        ivExit = findViewById(R.id.iv_exit)

        doodleView.setVisibility(View.INVISIBLE)
        ivExit.setVisibility(View.INVISIBLE)

        ivExit.setOnClickListener(this)
    }


    /**
     * 开始 显示NoteView动画
     */
    fun startShowNoteAnimation() {
        val mMultiUpdater = MultiScrapUpdater(
            false,
            Direction.BOTTOM
        )
        mMultiUpdater.addListener(object : MagicUpdaterListener {
            override fun onStart() {
                noteAnimationListener?.showAnimationStart()
                doodleView.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
            }

            override fun onStop() {
                doodleView.setVisibility(View.VISIBLE)
                ivExit.setVisibility(View.VISIBLE)
                magicSurfaceView.setVisibility(View.GONE)
                magicSurfaceView.release()
                noteAnimationListener?.showAnimationEnd()
            }
        })
        magicSurfaceView.render(MagicMultiSurface(doodleView, 20, 10).setUpdater(mMultiUpdater))
    }


    /**
     * 开始 隐藏NoteView动画
     */
    fun startHideNoteAnimation() {
        val mMultiUpdater = MultiScrapUpdater(
            true,
            Direction.BOTTOM
        )
        mMultiUpdater.addListener(object : MagicUpdaterListener {
            override fun onStart() {
                noteAnimationListener?.hideAnimationStart()
                doodleView.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
            }

            override fun onStop() {
                doodleView.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
                magicSurfaceView.setVisibility(View.GONE)
                magicSurfaceView.release()
                noteAnimationListener?.hideAnimationEnd()

            }
        })
        doodleView.newWholeBitmap(true).let {
            if(it==null){
                magicSurfaceView.render(MagicMultiSurface(doodleView, 20, 10).setUpdater(mMultiUpdater))
            }else{
                magicSurfaceView.render(MagicMultiSurface(it, Rect(0,0,doodleView.width,doodleView.height), 20, 10).setUpdater(mMultiUpdater))
            }
        }
    }


    /**
     * 开始展开漩涡动画
     */
    fun startShowVortexAnimation(){
        val updater = VortexAnimUpdater(false)
        updater.addListener(object : MagicUpdaterListener {
            override fun onStart() {
                doodleView.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
            }

            override fun onStop() {
                doodleView.setVisibility(View.VISIBLE)
                ivExit.setVisibility(View.VISIBLE)
                magicSurfaceView.setVisibility(View.GONE)
            }
        })
        val s = MagicSurface(this)
            .setGrid(60, 60)
            .drawGrid(false)
            .setModelUpdater(updater)
        magicSurfaceView.setVisibility(View.VISIBLE)
        magicSurfaceView.render(s)
    }

    /**
     * 开始收起漩涡动画
     */
    fun startHideVortexAnimation(){
        val updater = VortexAnimUpdater(true)
        updater.addListener(object : MagicUpdaterListener {
            override fun onStart() {
                noteAnimationListener?.hideVortexAnimationStart()
                doodleView.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
            }

            override fun onStop() {
                doodleView.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
                magicSurfaceView.setVisibility(View.GONE)
                // 释放场景资源
                magicSurfaceView.release()
                noteAnimationListener?.hideVortexAnimationEnd()
            }
        })

        doodleView.newWholeBitmap(true).let {
            if(it==null){
                val s = MagicSurface(this)
                    .setGrid(60, 60)
                    .drawGrid(false)
                    .setModelUpdater(updater)
                magicSurfaceView.setVisibility(View.VISIBLE)
                magicSurfaceView.render(s)
            }else{
                val s = MagicSurface(it,Rect(0,0,doodleView.width,doodleView.height))
                    .setGrid(60, 60)
                    .drawGrid(false)
                    .setModelUpdater(updater)
                magicSurfaceView.setVisibility(View.VISIBLE)
                magicSurfaceView.render(s)
            }
        }

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
     * 设置Note动画监听
     */
    fun setNoteAnimationListener(listener: NoteAnimationListener){
        noteAnimationListener = listener
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




    interface NoteAnimationListener{
        fun showAnimationStart()
        fun showAnimationEnd()
        fun hideAnimationStart()
        fun hideAnimationEnd()
        fun hideVortexAnimationStart()
        fun hideVortexAnimationEnd()
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

    override fun onClick(v: View?) {
        when(v){
            ivExit->{
                startHideNoteAnimation()
            }
        }
    }


}