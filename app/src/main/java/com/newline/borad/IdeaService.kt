package com.newline.borad

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.*
import androidx.appcompat.widget.AppCompatImageView
import com.gplibs.magicsurfaceview.MagicMultiSurface
import com.gplibs.magicsurfaceview.MagicSurfaceView
import com.gplibs.magicsurfaceview.MagicUpdaterListener
import com.jacky.commondraw.views.doodleview.DoodleEnum
import com.jacky.commondraw.views.doodleview.DoodleView
import com.newline.borad.animation.magic.Direction
import com.newline.borad.animation.magic.MultiScrapUpdater
import com.newline.borad.touch.FloatBarTouchEvent
import com.newline.borad.touch.FloatBarTouchManager
import com.newline.borad.utils.BitmapUtils


/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/2 16:14
 * @describe
 */
class IdeaService : Service(), FloatBarTouchEvent, View.OnClickListener, View.OnTouchListener,
    ScaleGestureDetector.OnScaleGestureListener {
    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams
    private lateinit var magicSurfaceView: MagicSurfaceView
    private lateinit var noteView : View
    private lateinit var scaleView : View
    private lateinit var doodleView: DoodleView
    private lateinit var ivMove : AppCompatImageView
    private lateinit var ivScale : AppCompatImageView
    private lateinit var ivExit : AppCompatImageView

    private lateinit var scaleGestureDetector : ScaleGestureDetector

    private val minWidth :Int by lazy {
        (resources.displayMetrics.widthPixels /2.5f).toInt()
    }

    private val minHeight :Int by lazy {
        (resources.displayMetrics.heightPixels /1.5f).toInt()
    }

    private var width : Int = 0
    private var height : Int = 0

    private val handler = Handler{
        scaleView.visibility = View.GONE
        false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
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
        mLayoutParams.windowAnimations = android.R.style.Animation_Toast

        initView()
        mWindowManager.addView(noteView,mLayoutParams)

        mLayoutParams.gravity = Gravity.START or Gravity.TOP

        show()

    }

    private fun initView() {
        noteView = LayoutInflater.from(this).inflate(R.layout.layout_note, null, false)
        magicSurfaceView = noteView.findViewById(R.id.magic_surface_view)
        scaleView = noteView.findViewById(R.id.layout_scale)
        doodleView = noteView.findViewById(R.id.doodleView)
        ivMove = noteView.findViewById(R.id.iv_move)
        ivScale = noteView.findViewById(R.id.iv_scale)
        ivExit = noteView.findViewById(R.id.iv_exit)

        doodleView.setVisibility(View.INVISIBLE)
        ivMove.setVisibility(View.INVISIBLE)
        ivScale.setVisibility(View.INVISIBLE)
        ivExit.setVisibility(View.INVISIBLE)

        FloatBarTouchManager.instance.init()
        FloatBarTouchManager.instance.addChildViewTouchListener(this,this,ivMove)

        ivScale.setOnClickListener(this)
        ivExit.setOnClickListener(this)

        scaleView.setOnTouchListener(this)
        scaleGestureDetector = ScaleGestureDetector(this,this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mWindowManager.removeView(noteView)
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
        mLayoutParams.x = parentX
        mLayoutParams.y = parentY
        mWindowManager.updateViewLayout(noteView,mLayoutParams)
    }

    override fun onFling(view: View, x: Int, y: Int, velocityX: Float, velocityY: Float) {
    }

    override fun onClick(v: View?) {
        when(v){
            ivScale->{
                scaleView.visibility = View.VISIBLE

            }
            ivExit->{
                doodleView.newBitmap().let {
                    if(it!=null){
                        BitmapUtils.saveBitmap(it,"new.png")
                        BitmapUtils.saveBitmap(doodleView.newWholeBitmap(true),"newbg.png")
                        hide(it)
                    }else{
                        hide(null)
                    }

                }
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return scaleGestureDetector.onTouchEvent(event)
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        width = mLayoutParams.width
        height = mLayoutParams.height
        handler.removeCallbacksAndMessages(null)
        handler.sendEmptyMessageDelayed(0x100,3000)
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        detector?.let {
            (width * it.scaleFactor).toInt().also {
                if(it<minWidth){
                    mLayoutParams.width = minWidth
                }else{
                    mLayoutParams.width = it
                }
            }
            (height * it.scaleFactor).toInt().also {
                mLayoutParams.height
                if(it<minHeight){
                    mLayoutParams.height = minHeight
                }else{
                    mLayoutParams.height = it
                }
            }
            mWindowManager.updateViewLayout(noteView,mLayoutParams)
        }

        return false
    }


    private fun show() {
        val mMultiUpdater = MultiScrapUpdater(
            false,
            Direction.BOTTOM
        )
        mMultiUpdater.addListener(object : MagicUpdaterListener {
            override fun onStart() {
                doodleView.setVisibility(View.INVISIBLE)
                ivMove.setVisibility(View.INVISIBLE)
                ivScale.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
            }

            override fun onStop() {
                doodleView.setVisibility(View.VISIBLE)
                ivMove.setVisibility(View.VISIBLE)
                ivScale.setVisibility(View.VISIBLE)
                ivExit.setVisibility(View.VISIBLE)
                magicSurfaceView.setVisibility(View.GONE)
                magicSurfaceView.release()

            }
        })
        magicSurfaceView.render(MagicMultiSurface(doodleView, 20, 10).setUpdater(mMultiUpdater))
    }

    private fun hide(bitmap : Bitmap?) {
        val mMultiUpdater = MultiScrapUpdater(
            true,
            Direction.BOTTOM
        )
        mMultiUpdater.addListener(object : MagicUpdaterListener {
            override fun onStart() {
                doodleView.setVisibility(View.INVISIBLE)
                ivMove.setVisibility(View.INVISIBLE)
                ivScale.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
            }

            override fun onStop() {
                doodleView.setVisibility(View.INVISIBLE)
                ivMove.setVisibility(View.INVISIBLE)
                ivScale.setVisibility(View.INVISIBLE)
                ivExit.setVisibility(View.INVISIBLE)
                magicSurfaceView.setVisibility(View.GONE)
                magicSurfaceView.release()
                stopSelf()
            }
        })
        if(bitmap == null){
            magicSurfaceView.render(MagicMultiSurface(doodleView, 20, 10).setUpdater(mMultiUpdater))
        }else{
            magicSurfaceView.render(MagicMultiSurface(doodleView.newWholeBitmap(true), Rect(0,0,doodleView.width,doodleView.height), 20, 10).setUpdater(mMultiUpdater))
        }

    }
}