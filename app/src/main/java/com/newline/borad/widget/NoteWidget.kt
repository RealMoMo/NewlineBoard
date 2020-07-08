package com.newline.borad.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.gplibs.magicsurfaceview.MagicMultiSurface
import com.gplibs.magicsurfaceview.MagicSurfaceView
import com.gplibs.magicsurfaceview.MagicUpdaterListener
import com.jacky.commondraw.views.doodleview.DoodleView
import com.newline.borad.R
import com.newline.borad.animation.magic.Direction
import com.newline.borad.animation.magic.MultiScrapUpdater

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/8 14:36
 * @describe
 */
class NoteWidget : FrameLayout {

    private lateinit var magicSurfaceView: MagicSurfaceView
    private lateinit var scaleView : View
    private lateinit var doodleView: DoodleView
    private lateinit var ivMove : AppCompatImageView
    private lateinit var ivScale : AppCompatImageView
    private lateinit var ivExit : AppCompatImageView

    private var noteAnimationListener : NoteAnimationListener? = null

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
        LayoutInflater.from(context).inflate(R.layout.layout_note, this, true)
        initView()
    }

    private fun initView() {
        magicSurfaceView = findViewById(R.id.magic_surface_view)
        scaleView = findViewById(R.id.layout_scale)
        doodleView = findViewById(R.id.doodleView)
        ivMove = findViewById(R.id.iv_move)
        ivScale = findViewById(R.id.iv_scale)
        ivExit = findViewById(R.id.iv_exit)
    }


    /**
     * 开始 显示NoteView动画
     */
    public fun startShowNoteAnimation() {
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
                noteAnimationListener?.showAnimationStart()
            }

            override fun onStop() {
                doodleView.setVisibility(View.VISIBLE)
                ivMove.setVisibility(View.VISIBLE)
                ivScale.setVisibility(View.VISIBLE)
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
    public fun startHideNoteAnimation() {
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
                noteAnimationListener?.hideAnimationStart()
            }

            override fun onStop() {
                doodleView.setVisibility(View.INVISIBLE)
                ivMove.setVisibility(View.INVISIBLE)
                ivScale.setVisibility(View.INVISIBLE)
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
     * 获取NoteView书写内容
     * @param needBg 背景是否需要绘制进Bitmap
     * @return Bitmap,若没有书写内容则Bitmap为null
     */
    public fun getNoteContent(needBg : Boolean):Bitmap?{
        return doodleView.newWholeBitmap(needBg)
    }

    /**
     * 设置Note动画监听
     */
    public fun setNoteAnimationListener(listener: NoteAnimationListener){
        noteAnimationListener = listener
    }


    interface NoteAnimationListener{
        fun showAnimationStart()
        fun showAnimationEnd()
        fun hideAnimationStart()
        fun hideAnimationEnd()
    }
}