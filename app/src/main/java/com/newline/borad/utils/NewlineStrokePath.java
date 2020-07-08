package com.newline.borad.utils;

import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;

import com.hhtpc.hhstrokesadr.HHStrokesAdr;
import com.hhtpc.hhstrokesadr.JSketchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Realmo
 * @version 1.0.0
 * @name NewlineBoard
 * @email momo.weiye@gmail.com
 * @time 2020/7/7 9:16
 * @describe
 */
public class NewlineStrokePath {

    //硬笔
    private static final int TYPE_HEAD_PEN = 1;
    //软笔
    private static final int TYPE_SOFT_PEN = 2;

    private HHStrokesAdr strokesAdr;
    private int penType;
    private float penWidth;

    private Path path;
    private  Path tempPath;

    public NewlineStrokePath(){
        strokesAdr = new HHStrokesAdr();
        penType = TYPE_SOFT_PEN;
        penWidth = 5f;
        path = new Path();
        tempPath = new Path();
    }


    public void setStrokeType(boolean isHardPen){
        if(isHardPen){
            penType = TYPE_HEAD_PEN;
        }else{
            penType = TYPE_SOFT_PEN;
        }
    }

    public void touchDown(MotionEvent event){
        if(strokesAdr.initStroke(1.0f,1.0f,penWidth,penType)){
            strokesAdr.startCreate(event.getX(),event.getY(),event.getEventTime());
            path.reset();

        }


    }


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
//            this.updateRect = result.getUpdateRect();
//            curStrokeRect = updateRect;
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
