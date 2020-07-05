package com.scanlibrary;

import android.graphics.Bitmap;

public interface NativeListener {
    Bitmap getScannedBitmap1(Bitmap bitmap, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

    Bitmap getGrayBitmap1(Bitmap bitmap);

     Bitmap getMagicColorBitmap1(Bitmap bitmap);

    Bitmap getBWBitmap1(Bitmap bitmap);

    float[] getPoints1(Bitmap bitmap);
}
