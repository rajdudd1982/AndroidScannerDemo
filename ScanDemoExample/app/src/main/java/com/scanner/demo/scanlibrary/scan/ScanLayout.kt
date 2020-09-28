package com.scanner.demo.scanlibrary.scan

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.helpers.BitmapTransformationHelper
import kotlinx.android.synthetic.main.scan_fragment_bottom_layout.view.*
import kotlinx.android.synthetic.main.scan_fragment_layout.*
import kotlinx.android.synthetic.main.scan_fragment_layout.view.*


class ScanLayout : RelativeLayout {

    lateinit var bitmapTransformation: BitmapTransformationHelper

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)
    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        rotateLeft.setOnClickListener { rotateImage(-90f) }
        rotateRight.setOnClickListener { rotateImage(90f) }
    }


    fun updateImageView(bitmap: Bitmap?){
        sourceFrame.post {
            if (bitmap != null) {
                scanLayout.setBitmap(bitmapTransformation, bitmap!!)
            }
        }
    }


    private fun rotateImage(degree: Float) {
        val matrix = Matrix()
        matrix.postRotate(degree)
        val bitmap: Bitmap = (sourceImageView.drawable as BitmapDrawable).bitmap
        val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        sourceImageView.setImageBitmap(rotated)
        setBitmap(bitmapTransformation, rotated)

    }


    fun setBitmap(bitmapTransformation: BitmapTransformationHelper, original: Bitmap) {
        val scaledBitmap = bitmapTransformation.scaledBitmap(original, sourceFrame!!.width, sourceFrame!!.height)
        sourceImageView.setImageBitmap(scaledBitmap)
        val tempBitmap = (sourceImageView!!.drawable as BitmapDrawable).bitmap
        val pointFs = getEdgePoints(bitmapTransformation, tempBitmap)
        polygonView.points = pointFs
        polygonView.visibility = View.VISIBLE
        val padding = resources.getDimension(R.dimen.scanPadding).toInt()
        val layoutParams = FrameLayout.LayoutParams(tempBitmap.width + 2 * padding, tempBitmap.height + 2 * padding)
        layoutParams.gravity = Gravity.CENTER
        polygonView.layoutParams = layoutParams
    }

    private fun getEdgePoints(bitmapTransformation: BitmapTransformationHelper, tempBitmap: Bitmap): Map<Int, PointF> {
        val pointFs = bitmapTransformation.getContourEdgePoints(tempBitmap)
        return orderedValidEdgePoints(bitmapTransformation, tempBitmap, pointFs)
    }

    private fun orderedValidEdgePoints(bitmapTransformation: BitmapTransformationHelper, tempBitmap: Bitmap, pointFs: List<PointF>): Map<Int, PointF> {
        var orderedPoints = polygonView!!.getOrderedPoints(pointFs)
        if (!polygonView!!.isValidShape(orderedPoints)) {
            orderedPoints = bitmapTransformation.getOutlinePoints(tempBitmap)
        }
        return orderedPoints
    }

}