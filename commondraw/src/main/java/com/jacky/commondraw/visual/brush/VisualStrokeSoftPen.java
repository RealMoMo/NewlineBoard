package com.jacky.commondraw.visual.brush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;

import com.hhtpc.hhstrokesadr.HHTStrokePath;
import com.jacky.commondraw.model.InsertableObjectBase;
import com.jacky.commondraw.model.stroke.StylusPoint;
import com.jacky.commondraw.views.doodleview.IInternalDoodle;

import java.util.List;

/**
 * @author Realmo
 * @version 1.0.0
 * @name NewlineBoard
 * @email momo.weiye@gmail.com
 * @time 2020/7/8 16:16
 * @describe
 */
public class VisualStrokeSoftPen extends VisualStrokeBase {

    protected Path mPath;
    protected RectF rectF;

    /**
     * @param context
     * @param internalDoodle
     * @param object
     */
    public VisualStrokeSoftPen(Context context, IInternalDoodle internalDoodle, InsertableObjectBase object) {
        super(context, internalDoodle, object);
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        rectF = new RectF();
    }

    @Override
    public void onDown(MotionElement mElement) {

    }

    @Override
    public void onMove(MotionElement mElement) {

    }

    @Override
    public void onUp(MotionElement mElement) {

    }

    @Override
    public void initFloatPoints(float[] fpoints, boolean isWithPressure) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
            // event会被下一次事件重用，这里必须生成新的，否则会有问题
            MotionEvent event2 = MotionEvent.obtain(event);
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:

                    HHTStrokePath.getInstance().touchDown(event2);

                    sendTouchOperation(event2);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    mPath.set(HHTStrokePath.getInstance().touchMove(event2));

                    sendTouchOperation(event2);
                    return true;
                case MotionEvent.ACTION_UP:
                    mPath.set(HHTStrokePath.getInstance().touchUp(event2));

                    sendTouchOperation(event2);
                    mInsertableObjectStroke.setPoints(getPoints());// up的时候，给模型层设置数据
                    mInsertableObjectStroke.setInitRectF(getBounds());
                    return true;
                default:
                    break;
            }
            return super.onTouchEvent(event);
    }

    @Override
    public void init() {

    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null){
            return;
        }
        Log.d("realmo","draw");
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public List<StylusPoint> getPoints() {
        return null;
    }

    @Override
    protected RectF getStrictBounds() {
        rectF.setEmpty();
        mPath.computeBounds(rectF,true);
        return rectF;
    }


}
