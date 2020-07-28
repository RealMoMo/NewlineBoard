package com.jacky.commondraw.visual.brush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;

import com.jacky.commondraw.model.InsertableObjectBase;
import com.jacky.commondraw.views.doodleview.IInternalDoodle;

/**
 * @author Realmo
 * @version 1.0.0
 * @name NewlineBoard
 * @email momo.weiye@gmail.com
 * @time 2020/7/24 15:07
 * @describe  板擦
 */
public class VisualStrokePhysicErase extends VisualStrokeErase {

    private static final int MIN_PATH_WIDTH = 12;

    private float width;

    public VisualStrokePhysicErase(Context context, IInternalDoodle internalDoodle, InsertableObjectBase object) {
        super(context, internalDoodle, object);

    }



    @Override
    protected void updatePaint() {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mInsertableObjectStroke.getColor());
        mPaint.setStrokeWidth(mInsertableObjectStroke.getStrokeWidth());
//        mPaint.setStrokeWidth(mLastWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setPathEffect(null);
        mPaint.setAlpha(0xFF);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null){
            return;
        }
        canvas.drawPath(mPath, mPaint);

        if(isUp){
            return;
        }
        canvas.drawCircle(mEndX,mEndY, width /2,mPaintIndicator);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        // event会被下一次事件重用，这里必须生成新的，否则会有问题
        MotionEvent event2 = MotionEvent.obtain(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                calcPhysicWidth(event2);
                onDown(createMotionElement(event2));
                sendTouchOperation(event2);
                return true;
            case MotionEvent.ACTION_MOVE:
                onMove(createMotionElement(event2));
                sendTouchOperation(event2);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isUp = true;
                onUp(createMotionElement(event2));
                sendTouchOperation(event2);
                mInsertableObjectStroke.setPoints(getPoints());// up的时候，给模型层设置数据
                mInsertableObjectStroke.setInitRectF(getBounds());

                return true;
            default:
                break;
        }
        return super.onTouchEvent(event2);
    }

    /**
     * 计算板擦宽度
     * @param event
     */
    private void calcPhysicWidth(MotionEvent event) {

        float size = event.getSize();
        if( size<0.02){
            width =  mInsertableObjectStroke.getStrokeWidth();
        }else if(size<=0.03){
            width =  MIN_PATH_WIDTH*6;
        }else if(size<=0.05){
            width =  MIN_PATH_WIDTH*9;
        }else{
            width =  MIN_PATH_WIDTH*14;
        }
        mPaint.setStrokeWidth(width);


    }


}
