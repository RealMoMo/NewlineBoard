package com.hhtpc.hhstrokesadr;

import java.util.Arrays;

public class JSketchResult {

    private int nPointNum;
    private float[] points;
    private int nPartNum;
    private int[] partPoints;
    private float[] rect;

    public int getPointsNum() {
        return nPointNum;
    }

    public float[] getPointsData() {
        return points;
    }

    public float[] getUpdateRect() {
        return rect;
    }

    public int getPartsNum() {
        return nPartNum;
    }

    public int[] getPartPointsData() {
        return partPoints;
    }

    public JSketchResult(int nPointNum, float[] rect, float[] points, int partNum,
        int[] partPoints) {
        this.nPointNum = nPointNum;
        this.rect = rect;
        this.points = points;
        this.nPartNum = partNum;
        this.partPoints = partPoints;
    }

    @Override public String toString() {
        return "JSketchResult{" +"\n"+
            ", nPartNum=" + nPartNum +"\n"+
            ", partPoints=" + Arrays.toString(partPoints) +"\n"+
            ", rect=" + Arrays.toString(rect) + "\n"+
            "nPointNum=" + nPointNum + "\n"+
            ", points=" + Arrays.toString(points) +
            '}';
    }
}
