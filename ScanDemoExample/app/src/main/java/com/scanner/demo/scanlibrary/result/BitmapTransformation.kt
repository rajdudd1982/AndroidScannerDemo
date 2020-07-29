package com.scanner.demo.scanlibrary.result

import android.graphics.Bitmap
import android.graphics.PointF
import android.util.Log
import android.widget.ImageView
import com.scanlibrary.ScanActivity
import java.util.HashMap

class BitmapTransformation {

    fun bitmapTransformationByType(activity: ScanActivity, original: Bitmap, type: TransformationType): Bitmap {
        var bitmap: Bitmap = original
        try {
            when (type) {
                TransformationType.Magic -> bitmap = activity.getMagicColorBitmap(original)
                TransformationType.BW -> bitmap = activity.getBWBitmap(original)
                TransformationType.Gray -> bitmap = activity.getGrayBitmap(original)
                TransformationType.Original -> original
            }
        } catch (e: Exception) {

        }
        return bitmap
    }

    fun getOutlinePoints(tempBitmap: Bitmap): Map<Int, PointF> {
        val outlinePoints: MutableMap<Int, PointF> = HashMap()
        outlinePoints[0] = PointF(0F, 0F)
        outlinePoints[1] = PointF(tempBitmap.width.toFloat(), 0F)
        outlinePoints[2] = PointF(0F, tempBitmap.height.toFloat())
        outlinePoints[3] = PointF(tempBitmap.width.toFloat(), tempBitmap.height.toFloat())
        return outlinePoints
    }

    fun getScannedBitmap(activity: ScanActivity, sourceImageView: ImageView, original: Bitmap?, points: Map<Int, PointF>): Bitmap {
        val width = original!!.width
        val height = original.height
        val xRatio = original.width.toFloat() / sourceImageView!!.width
        val yRatio = original.height.toFloat() / sourceImageView!!.height
        val x1 = points[0]!!.x * xRatio
        val x2 = points[1]!!.x * xRatio
        val x3 = points[2]!!.x * xRatio
        val x4 = points[3]!!.x * xRatio
        val y1 = points[0]!!.y * yRatio
        val y2 = points[1]!!.y * yRatio
        val y3 = points[2]!!.y * yRatio
        val y4 = points[3]!!.y * yRatio
        Log.d("", "POints($x1,$y1)($x2,$y2)($x3,$y3)($x4,$y4)")
        return activity.getScannedBitmap(original, x1, y1, x2, y2, x3, y3, x4, y4)
    }

    enum class TransformationType {
        Original, Gray, Magic, BW
    }

}