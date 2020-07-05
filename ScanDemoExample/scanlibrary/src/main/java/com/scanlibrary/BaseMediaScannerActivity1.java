package com.scanlibrary;

import android.graphics.Bitmap;

import java.util.logging.Logger;

public class BaseMediaScannerActivity1 extends  BaseActivity implements  NativeListener {

    static {
        try {
            System.loadLibrary("opencv_java3");
            System.loadLibrary("Scanner");
        } catch (Exception e){
            Logger.getLogger(e.getMessage());
        }
    }

    @Override
    public Bitmap getScannedBitmap1(Bitmap bitmap, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        return getScannedBitmap( bitmap,  x1,  y1,  x2,  y2,  x3,  y3,  x4,  y4);
    }

    @Override
    public Bitmap getGrayBitmap1(Bitmap bitmap) {
        return null;
    }

    @Override
    public Bitmap getMagicColorBitmap1(Bitmap bitmap) {
        return null;
    }

    @Override
    public Bitmap getBWBitmap1(Bitmap bitmap) {
        return null;
    }

    @Override
    public float[] getPoints1(Bitmap bitmap) {
        return getPoints(bitmap);
    }

    public native Bitmap getScannedBitmap(Bitmap bitmap, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    public native Bitmap getGrayBitmap(Bitmap bitmap);

    public native Bitmap getMagicColorBitmap(Bitmap bitmap);

    public native Bitmap getBWBitmap(Bitmap bitmap);

    public native float[] getPoints(Bitmap bitmap);
}

