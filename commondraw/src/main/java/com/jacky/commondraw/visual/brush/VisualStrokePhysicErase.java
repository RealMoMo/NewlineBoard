package com.jacky.commondraw.visual.brush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.jacky.commondraw.model.InsertableObjectBase;
import com.jacky.commondraw.views.doodleview.IInternalDoodle;
import com.jacky.commondraw.visual.VisualStrokeSpot;
import com.jacky.commondraw.visual.brush.operation.EraseTouchOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Realmo
 * @version 1.0.0
 * @name NewlineBoard
 * @email momo.weiye@gmail.com
 * @time 2020/7/24 15:07
 * @describe  板擦
 */
public class VisualStrokePhysicErase extends VisualStrokeErase {

    private DashPathEffect mEffects = null;
    private Paint mPaintIndicator = null;
    private boolean isUp = false;

    private static final int MIN_PATH_WIDTH = 10;
    private static final int MAX_PATH_WIDTH = 100;

    private ArrayList<HWPoint> mPointList;
    private ArrayList<HWPoint> mHWPointList;

    private double mLastVel;
    private double mLastWidth;

    private ArrayList<HWPoint> mOnTimeDrawList;
    private HWPoint mLastPoint;
    private HWPoint curPoint;
    private QuadBezierSpline mBezier;
    protected int BOUND_TOLERANCE = 30;
    protected int INVALIDATE_MARGIN = 15;
    protected float DIS_VEL_CAL_FACTOR = 0.02f;
    protected float WIDTH_THRES_MAX = 0.6f;
    protected int STEPFACTOR = 8;


    public VisualStrokePhysicErase(Context context, IInternalDoodle internalDoodle, InsertableObjectBase object) {
        super(context, internalDoodle, object);

        mPointList = new ArrayList<HWPoint>();
        mHWPointList = new ArrayList<HWPoint>();
        mOnTimeDrawList = new ArrayList<HWPoint>();
        mLastPoint = new HWPoint(0, 0);

        mBezier = new QuadBezierSpline();

        mEffects = new DashPathEffect(new float[] {5, 5, 5, 5}, 1);

        mPaintIndicator = new Paint();
        mPaintIndicator.setDither(true);
        mPaintIndicator.setAntiAlias(true);
        mPaintIndicator.setStyle(Paint.Style.STROKE);
        mPaintIndicator.setColor(Color.parseColor("#1296db"));
        mPaintIndicator.setPathEffect(mEffects);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null){
            return;
        }

        if (mHWPointList == null || mHWPointList.size() < 1){
            return;
        }

        if (mHWPointList.size() < 2) {
            HWPoint point = mHWPointList.get(0);
            canvas.drawCircle(point.x, point.y, point.width, mPaint);
        } else {
            curPoint = mHWPointList.get(0);
            for (int i = 1; i < mHWPointList.size(); i++) {
                HWPoint point = mHWPointList.get(i);
                drawToPoint(canvas, point, mPaint);
                curPoint = point;
            }
        }

        if(isUp){
            return;
        }

        //TODO draw earse path

        canvas.drawCircle(mLastPoint.x,mLastPoint.y,mLastPoint.width/2,mPaintIndicator);
    }

    @Override
    protected void updatePaint() {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mInsertableObjectStroke.getColor());
        mPaint.setStrokeWidth(mInsertableObjectStroke.getStrokeWidth());
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setPathEffect(null);
        mPaint.setAlpha(0xFF);
    }


    @Override
    public void onDown(MotionElement mElement) {

        mPath = new Path();
        mPointList.clear();
        mHWPointList.clear();

        HWPoint curPoint = new HWPoint(mElement.x, mElement.y,
                mElement.timestamp);


        mLastWidth =  MIN_PATH_WIDTH;
        
        curPoint.width = (float) mLastWidth;
        mLastVel = 0;

        mPointList.add(curPoint);
        mLastPoint = curPoint;
        mPath.moveTo(mElement.x, mElement.y);

        mOnTimeDrawList.clear();
    }

    @Override
    public void onMove(MotionElement mElement) {
        HWPoint curPoint = new HWPoint(mElement.x, mElement.y,
                mElement.timestamp);

        // V->W
        double deltaX = curPoint.x - mLastPoint.x;
        double deltaY = curPoint.y - mLastPoint.y;
        double curDis = Math.hypot(deltaX, deltaY);
        double curVel = curDis * DIS_VEL_CAL_FACTOR;
        double curWidth;

        if (mPointList.size() < 2) {

            curWidth = calcNewWidth(curVel, mLastVel, curDis, 1.5,
                        mLastWidth);
            
            curPoint.width = (float) curWidth;
            mBezier.Init(mLastPoint, curPoint);
        } else {
            mLastVel = curVel;

//            curWidth = calcNewWidth(curVel, mLastVel, curDis, 1.5,
//                        mLastWidth);
            //realmo test
            curWidth = mLastWidth + 2;
            curPoint.width = (float) curWidth;
            mBezier.AddNode(curPoint);
        }
        mLastWidth = curWidth;

        mPointList.add(curPoint);

        mOnTimeDrawList.clear();
        int steps = 1 + (int) curDis / STEPFACTOR;
        double step = 1.0 / steps;
        for (double t = 0; t < 1.0; t += step) {
            HWPoint point = mBezier.GetPoint(t);
            mHWPointList.add(point);
            mOnTimeDrawList.add(point);
        }
        mOnTimeDrawList.add(mBezier.GetPoint(1.0));
        calcNewDirtyRect(mOnTimeDrawList.get(0),
                mOnTimeDrawList.get(mOnTimeDrawList.size() - 1));

        mPath.quadTo(mLastPoint.x, mLastPoint.y,
                (mElement.x + mLastPoint.x) / 2,
                (mElement.y + mLastPoint.y) / 2);

        mLastPoint = curPoint;
    }

    @Override
    public void onUp(MotionElement mElement) {
        // TODO Auto-generated method stub
        isUp = true;
        HWPoint curPoint = new HWPoint(mElement.x, mElement.y,
                mElement.timestamp);
        mOnTimeDrawList.clear();
        double deltaX = curPoint.x - mLastPoint.x;
        double deltaY = curPoint.y - mLastPoint.y;
        double curDis = Math.hypot(deltaX, deltaY);


        curPoint.width = 0;
        

        mPointList.add(curPoint);

        mBezier.AddNode(curPoint);

        int steps = 1 + (int) curDis / STEPFACTOR;
        double step = 1.0 / steps;
        for (double t = 0; t < 1.0; t += step) {
            HWPoint point = mBezier.GetPoint(t);
            mHWPointList.add(point);
            mOnTimeDrawList.add(point);
        }

        mBezier.End();
        for (double t = 0; t < 1.0; t += step) {
            HWPoint point = mBezier.GetPoint(t);
            mHWPointList.add(point);
            mOnTimeDrawList.add(point);
        }

        calcNewDirtyRect(mOnTimeDrawList.get(0),
                mOnTimeDrawList.get(mOnTimeDrawList.size() - 1));
        mPath.quadTo(mLastPoint.x, mLastPoint.y,
                (mElement.x + mLastPoint.x) / 2,
                (mElement.y + mLastPoint.y) / 2);
        mPath.lineTo(mElement.x, mElement.y);
    }



    private void drawToPoint(Canvas canvas, HWPoint point, Paint paint) {
        if ((curPoint.x == point.x) && (curPoint.y == point.y)){
            return;
        }
        drawLine(canvas, curPoint.x, curPoint.y, curPoint.width, point.x,
                point.y, point.width, paint);
    }


    private void drawLine(Canvas canvas, double x0, double y0, double w0,
                            double x1, double y1, double w1, Paint paint) {
        double curDis = Math.hypot(x0 - x1, y0 - y1);
        int steps = 1 + (int) (curDis / 2.0);
        double deltaX = (x1 - x0) / steps;
        double deltaY = (y1 - y0) / steps;
        double deltaW = (w1 - w0) / steps;
        double x = x0;
        double y = y0;
        double w = w0;

        for (int i = 0; i < steps; i++) {
            canvas.drawCircle((float) x, (float) y, (float) w / 2.0f, paint);
            x += deltaX;
            y += deltaY;
            w += deltaW;
        }
    }


    private void calcNewDirtyRect(HWPoint p0, HWPoint p1) {
        int margin = getMargin();
        mDirtyRect = new Rect();
        mDirtyRect.left = (p0.x < p1.x) ? (int) p0.x - margin : (int) p1.x
                - margin;
        mDirtyRect.right = (p0.x > p1.x) ? (int) p0.x + margin : (int) p1.x
                + margin;
        mDirtyRect.top = (p0.y < p1.y) ? (int) p0.y - margin : (int) p1.y
                - margin;
        mDirtyRect.bottom = (p0.y > p1.y) ? (int) p0.y + margin : (int) p1.y
                + margin;
    }

    protected int getMargin() {
        return INVALIDATE_MARGIN + (int) (mPaint.getStrokeWidth());
    }


    protected double calcNewWidth(double curVel, double lastVel, double curDis,
                                  double factor, double lastWidth) {
        // A simple low pass filter to mitigate velocity aberrations.
        double calVel = curVel * 0.6 + lastVel * (1 - 0.6);
        double vfac = Math.log(factor * 2.0f) * (-calVel);
        double calWidth = MIN_PATH_WIDTH * Math.exp(vfac);

        double mMoveThres = curDis * 0.01f;
        if (mMoveThres > WIDTH_THRES_MAX) {
            mMoveThres = WIDTH_THRES_MAX;
        }

        if (Math.abs(calWidth - MIN_PATH_WIDTH) / MIN_PATH_WIDTH > mMoveThres) {
            if (calWidth > MIN_PATH_WIDTH) {
                calWidth = MIN_PATH_WIDTH * (1 + mMoveThres);
            } else {
                calWidth = MIN_PATH_WIDTH * (1 - mMoveThres);
            }
        } else if (Math.abs(calWidth - lastWidth) / lastWidth > mMoveThres) {
            if (calWidth > lastWidth) {
                calWidth = lastWidth * (1 + mMoveThres);
            } else {
                calWidth = lastWidth * (1 - mMoveThres);
            }
        }
        return calWidth;
    }
}
