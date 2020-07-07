package com.hhtpc.hhstrokesadr;


public class HHStrokesAdr {

    static {
        System.loadLibrary("StrokesAlgorithm");
    }

    public long getObjHandle() {
        return m_oStroke;
    }

    public void setObjHandle(long obj) {
        m_oStroke = obj;
    }

    //Call C++ so lib
    public native boolean initStroke(float scaleWidth, float scaleHeight,
        float strokesWidth, int strokesType);

    public native JSketchResult startCreate(float x, float y, long time);

    public native JSketchResult creating(float x, float y);

    public native JSketchResult creating2(float[] xyList, long[] timeList, int len);

    public native JSketchResult endCreate(float x, float y, long time);

    public native void cleanup();

    private long m_oStroke = 0;
}
