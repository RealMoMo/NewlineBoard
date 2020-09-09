package com.hhtpc.hhstrokesadr;

import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;


/**
 * @author Realmo
 * @version 1.0.0
 * @name HHTStrokelib
 * @email momo.weiye@gmail.com
 * @time 2020/7/7 9:16
 * @describe  单例 HHTStrokePath，该类作用生成硬笔与软笔效果的Path(软笔有笔锋效果,硬笔效果不明显)。
 * 由于是单例类，所以只支持单笔书写生成Path。 若需多笔同时书写请用{@link HHTMutilStrokePath}
 */
public final class HHTStrokePath {


    private static HHTStrokePath mInstance;

    private HHStrokesAdr strokesAdr;

    private Path path;
    private  Path tempPath;

    public static HHTStrokePath getInstance(){
        if(mInstance == null){
            synchronized (HHTStrokePath.class) {
                if (mInstance == null) {
                    mInstance = new HHTStrokePath();
                }
            }
        }
        return mInstance;
    }

    private HHTStrokePath(){
        strokesAdr = new HHStrokesAdr();
        path = new Path();
        tempPath = new Path();
    }


    /**
     *
     * @param event
     * @param penWidth
     * @param penType
     * @return 是否成功初始化本画笔
     */
    public boolean touchDown(MotionEvent event,float penWidth,@HHTStrokePathType int penType){
        if(strokesAdr.initStroke(1.0f,1.0f,penWidth,penType)){
            strokesAdr.startCreate(event.getX(),event.getY(),event.getEventTime());
            path.reset();
            return true;
        }
        return false;

    }

    /**
     *
     * @param event
     * @return Path，该Path是该库的单例对象.所以，调用者应该是 path.set(touchMove(MotionEvent event))
     */
    public Path touchMove(MotionEvent event){
        int length = event.getHistorySize()+1;
        float[] pointArray = new float[2*length];
        long[] timeArray = new long[length];

        for (int i = 0; i < length-1; i++) {
            pointArray[2*i] = event.getHistoricalX(i);
            pointArray[2*i+1] = event.getHistoricalY(i);
            timeArray[i] = event.getHistoricalEventTime(i);
        }

        pointArray[pointArray.length-2] = event.getX();
        pointArray[pointArray.length-1] = event.getY();
        timeArray[timeArray.length-1] = event.getEventTime();

        JSketchResult result = strokesAdr.creating2(pointArray, timeArray,length);
        return makePath(result);
    }

    /**
     *
     * @param event
     * @return Path，该Path是该库的单例对象.所以，调用者应该是 path.set(touchUp(MotionEvent event))
     */
    public Path touchUp(MotionEvent event){
        JSketchResult jSketchResult = strokesAdr.endCreate(event.getX(), event.getY(), event.getEventTime());
        Path temp = null;
        if(jSketchResult != null){
            temp = makePath(jSketchResult);
        }
        strokesAdr.cleanup();

        return temp;
    }


    private Path makePath(JSketchResult result){
        if (result != null) {

            result.getUpdateRect();

            int pointNum = result.getPointsNum();
            float[] points = result.getPointsData();
            int partNum = result.getPartsNum();
            int[] partPoints = result.getPartPointsData();
            int partOffset = 0;
           tempPath.reset();
            for (int partCode = 0; partCode < partNum && pointNum > 0; partCode++) {
                int partPointNum = partPoints[partCode];
                tempPath.moveTo(points[partOffset * 2], points[partOffset * 2 + 1]);
                for (int point = partOffset + 1; point < (partOffset + partPointNum); ++point) {
                    tempPath.lineTo(points[point * 2], points[point * 2 + 1]);
                }
                partOffset += partPointNum;
            }
            path.addPath(tempPath);
        }
        return path;
    }
} 
