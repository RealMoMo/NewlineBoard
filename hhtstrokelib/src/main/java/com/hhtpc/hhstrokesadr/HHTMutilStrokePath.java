package com.hhtpc.hhstrokesadr;

import android.graphics.Path;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;


/**
 * @author Realmo
 * @version 1.0.0
 * @name HHTStrokelib
 * @email momo.weiye@gmail.com
 * @time 2020/7/7 9:16
 * @describe  单例 HHTStrokePath，该类作用生成硬笔与软笔效果的Path(软笔有笔锋效果)。
 * 支持多笔书写生成Path。
 */
public class HHTMutilStrokePath {

    //硬笔类型
    private static final int TYPE_HEAD_PEN = 1;
    //软笔类型
    private static final int TYPE_SOFT_PEN = 2;

    private static HHTMutilStrokePath mInstance;

    private SparseArray<StrokePath> pathArray;

    public static HHTMutilStrokePath getInstance(){
        if(mInstance == null){
            synchronized (HHTMutilStrokePath.class) {
                if (mInstance == null) {
                    mInstance = new HHTMutilStrokePath();
                }
            }
        }
        return mInstance;
    }

    private HHTMutilStrokePath(){
        pathArray = new SparseArray(10);
    }


    public boolean touchDown(float x,float y,int touchId,long eventTime,float penWidth,int penType){
        if(pathArray.get(touchId) == null){
            pathArray.put(touchId,new StrokePath());
        }
        StrokePath strokePath = pathArray.get(touchId);
        if(strokePath.hhStrokesAdr.initStroke(1.0f,1.0f,penWidth,penType)){
            strokePath.hhStrokesAdr.startCreate(x,y,eventTime);
            strokePath.path.reset();
            return true;
        }

        return false;
    }


    public Path touchMove(MotionEvent event,int touchId){
        int length = event.getHistorySize()+1;
        float[] pointArray = new float[2*length];
        long[] timeArray = new long[length];

        for (int i = 0; i < length-1; i++) {
            pointArray[2*i] = event.getHistoricalX(touchId,i);
            pointArray[2*i+1] = event.getHistoricalY(touchId,i);
            timeArray[i] = event.getHistoricalEventTime(i);
        }

        pointArray[pointArray.length-2] = event.getX();
        pointArray[pointArray.length-1] = event.getY();
        timeArray[timeArray.length-1] = event.getEventTime();

        StrokePath strokePath = pathArray.get(touchId);
        JSketchResult result = strokePath.hhStrokesAdr.creating2(pointArray, timeArray,length);
        makePath(result,strokePath.tempPath,strokePath.path);
        return strokePath.path;
    }

    public Path touchUp(float x,float y,int touchId,long eventTime){
        StrokePath strokePath = pathArray.get(touchId);
        JSketchResult jSketchResult = strokePath.hhStrokesAdr.endCreate(x, y, eventTime);
        if(jSketchResult!= null){
            makePath(jSketchResult,strokePath.tempPath,strokePath.path);
        }
        strokePath.hhStrokesAdr.cleanup();

        return strokePath.path;
    }



    private void makePath(JSketchResult result,Path tempPath,Path newPath){
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
            newPath.addPath(tempPath);
        }

    }

    final static class StrokePath{
        HHStrokesAdr hhStrokesAdr;
        Path path;
        Path tempPath;

        public StrokePath() {
            hhStrokesAdr = new HHStrokesAdr();
            path = new Path();
            tempPath = new Path();
        }
    }
} 
