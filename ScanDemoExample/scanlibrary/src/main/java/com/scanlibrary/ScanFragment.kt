package com.scanlibrary

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.scanlibrary.Utils.getBitmap
import com.scanlibrary.Utils.getUri
import java.io.IOException
import java.util.*

/**
 * Created by jhansi on 29/03/15.
 */
class ScanFragment : Fragment() {
    private var scanButton: Button? = null
    private var sourceImageView: ImageView? = null
    private var sourceFrame: FrameLayout? = null
    private var polygonView: PolygonView? = null
    private var mainView: View? = null
    private var progressDialogFragment: ProgressDialogFragment? = null
    private var scanner: IScanner? = null
    private var original: Bitmap? = null
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity !is IScanner) {
            throw ClassCastException("Activity must implement IScanner")
        }
        scanner = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.scan_fragment_layout, null)
        init()
        return mainView
    }

    private fun init() {
        sourceImageView = mainView!!.findViewById<View>(R.id.sourceImageView) as ImageView
        scanButton = mainView!!.findViewById<View>(R.id.scanButton) as Button
        scanButton!!.setOnClickListener(ScanButtonClickListener())
        sourceFrame = mainView!!.findViewById<View>(R.id.sourceFrame) as FrameLayout
        polygonView = mainView!!.findViewById<View>(R.id.polygonView) as PolygonView
        sourceFrame!!.post {
            original = bitmap
            if (original != null) {
                setBitmap(original!!)
            }
        }
    }

    private val bitmap: Bitmap?
        private get() {
            val uri = uri
            try {
                val bitmap = getBitmap(activity!!, uri)
                activity!!.contentResolver.delete(uri!!, null, null)
                return bitmap
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

    private val uri: Uri?
        private get() = arguments!!.getParcelable(ScanConstants.SELECTED_BITMAP)

    private fun setBitmap(original: Bitmap) {
        val scaledBitmap = scaledBitmap(original, sourceFrame!!.width, sourceFrame!!.height)
        sourceImageView!!.setImageBitmap(scaledBitmap)
        val tempBitmap = (sourceImageView!!.drawable as BitmapDrawable).bitmap
        val pointFs = getEdgePoints(tempBitmap)
        polygonView!!.points = pointFs
        polygonView!!.visibility = View.VISIBLE
        val padding = resources.getDimension(R.dimen.scanPadding).toInt()
        val layoutParams = FrameLayout.LayoutParams(tempBitmap.width + 2 * padding, tempBitmap.height + 2 * padding)
        layoutParams.gravity = Gravity.CENTER
        polygonView!!.layoutParams = layoutParams
    }

    private fun getEdgePoints(tempBitmap: Bitmap): Map<Int, PointF> {
        val pointFs = getContourEdgePoints(tempBitmap)
        return orderedValidEdgePoints(tempBitmap, pointFs)
    }

    private fun getContourEdgePoints(tempBitmap: Bitmap): List<PointF> {
        val points = (activity as ScanActivity?)!!.getPoints(tempBitmap)
        val x1 = points[0]
        val x2 = points[1]
        val x3 = points[2]
        val x4 = points[3]
        val y1 = points[4]
        val y2 = points[5]
        val y3 = points[6]
        val y4 = points[7]
        val pointFs: MutableList<PointF> = ArrayList()
        pointFs.add(PointF(x1, y1))
        pointFs.add(PointF(x2, y2))
        pointFs.add(PointF(x3, y3))
        pointFs.add(PointF(x4, y4))
        return pointFs
    }

    private fun getOutlinePoints(tempBitmap: Bitmap): Map<Int, PointF> {
        val outlinePoints: MutableMap<Int, PointF> = HashMap()
        outlinePoints[0] = PointF(0F, 0F)
        outlinePoints[1] = PointF(tempBitmap.width.toFloat(), 0F)
        outlinePoints[2] = PointF(0F, tempBitmap.height.toFloat())
        outlinePoints[3] = PointF(tempBitmap.width.toFloat(), tempBitmap.height.toFloat())
        return outlinePoints
    }

    private fun orderedValidEdgePoints(tempBitmap: Bitmap, pointFs: List<PointF>): Map<Int, PointF> {
        var orderedPoints = polygonView!!.getOrderedPoints(pointFs)
        if (!polygonView!!.isValidShape(orderedPoints)) {
            orderedPoints = getOutlinePoints(tempBitmap)
        }
        return orderedPoints
    }

    private inner class ScanButtonClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val points = polygonView!!.points
            if (isScanPointsValid(points)) {
                ScanAsyncTask(points).execute()
            } else {
                showErrorDialog()
            }
        }
    }

    private fun showErrorDialog() {
        val fragment = SingleButtonDialogFragment(R.string.ok, getString(R.string.cantCrop), "Error", true)
        val fm = activity!!.fragmentManager
        fragment.show(fm, SingleButtonDialogFragment::class.java.toString())
    }

    private fun isScanPointsValid(points: Map<Int, PointF>): Boolean {
        return points.size == 4
    }

    private fun scaledBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        val m = Matrix()
        m.setRectToRect(RectF(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat()), RectF(0F, 0F, width.toFloat(), height.toFloat()), Matrix.ScaleToFit.CENTER)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    private fun getScannedBitmap(original: Bitmap?, points: Map<Int, PointF>): Bitmap {
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
        return (activity as ScanActivity?)!!.getScannedBitmap(original, x1, y1, x2, y2, x3, y3, x4, y4)
    }

    private inner class ScanAsyncTask(private val points: Map<Int, PointF>) : AsyncTask<Void, Void, Bitmap>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog(getString(R.string.scanning))
        }

        protected override fun doInBackground(vararg params: Void): Bitmap {
            val bitmap = getScannedBitmap(original, points)
            val uri = getUri(activity!!, bitmap)
            scanner!!.onScanFinish(uri)
            return bitmap
        }

        override fun onPostExecute(bitmap: Bitmap) {
            super.onPostExecute(bitmap)
            bitmap.recycle()
            dismissDialog()
        }

    }

    protected fun showProgressDialog(message: String?) {
        progressDialogFragment = ProgressDialogFragment(message)
        val fm = fragmentManager
        progressDialogFragment!!.show(fm!!, ProgressDialogFragment::class.java.toString())
    }

    protected fun dismissDialog() {
        progressDialogFragment!!.dismissAllowingStateLoss()
    }
}