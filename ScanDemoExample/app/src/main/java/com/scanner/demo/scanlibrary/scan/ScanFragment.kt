package com.scanlibrary

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.scanlibrary.helpers.Utils.getUri
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.*
import com.scanner.demo.scanlibrary.result.BitmapTransformation
import kotlinx.android.synthetic.main.scan_fragment_layout.*

/**
 * Created by jhansi on 29/03/15.
 */
class ScanFragment : BaseScanFragment() {
    private var progressDialogFragment: ProgressDialogFragment? = null

    private var scanner: IScanner? = null
    private var original: Bitmap? = null

    private lateinit var bitmapTransformation: BitmapTransformation

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity !is IScanner) {
            throw ClassCastException("Activity must implement IScanner")
        }
        scanner = activity
        bitmapTransformation = BitmapTransformation(activity!! as ScanActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.scan_fragment_layout, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanButton.setOnClickListener(ScanButtonClickListener(uri))

        sourceFrame.post {
            original = bitmap
            if (original != null) {
                setBitmap(original!!)
            }
        }
    }

    private fun setBitmap(original: Bitmap) {
        val scaledBitmap = bitmapTransformation.scaledBitmap(original, sourceFrame!!.width, sourceFrame!!.height)
        sourceImageView.setImageBitmap(scaledBitmap)
        val tempBitmap = (sourceImageView!!.drawable as BitmapDrawable).bitmap
        val pointFs = getEdgePoints(tempBitmap)
        polygonView.points = pointFs
        polygonView.visibility = View.VISIBLE
        val padding = resources.getDimension(R.dimen.scanPadding).toInt()
        val layoutParams = FrameLayout.LayoutParams(tempBitmap.width + 2 * padding, tempBitmap.height + 2 * padding)
        layoutParams.gravity = Gravity.CENTER
        polygonView.layoutParams = layoutParams
    }

    private fun getEdgePoints(tempBitmap: Bitmap): Map<Int, PointF> {
        val pointFs = bitmapTransformation.getContourEdgePoints(tempBitmap)
        return orderedValidEdgePoints(tempBitmap, pointFs)
    }

    private fun orderedValidEdgePoints(tempBitmap: Bitmap, pointFs: List<PointF>): Map<Int, PointF> {
        var orderedPoints = polygonView!!.getOrderedPoints(pointFs)
        if (!polygonView!!.isValidShape(orderedPoints)) {
            orderedPoints = bitmapTransformation.getOutlinePoints(tempBitmap)
        }
        return orderedPoints
    }

    private inner class ScanButtonClickListener(uri: Uri?) : View.OnClickListener {
        override fun onClick(v: View) {
            val points = polygonView!!.points
            if (isScanPointsValid(points)) {
                ScanAsyncTask(points).execute(uri)
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

    private fun getScannedBitmap(original: Bitmap?, points: Map<Int, PointF>): Bitmap {
        return bitmapTransformation.getScannedBitmap(sourceImageView!!, original, points)
    }

    private inner class ScanAsyncTask(private val points: Map<Int, PointF>) : AsyncTask<Uri?, Void, Bitmap>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog(getString(R.string.scanning))
        }

        protected override fun doInBackground(vararg params: Uri?): Bitmap {
            val bitmap = getScannedBitmap(original, points)
            var path: String? = params?.get(0)?.path
            val finalPath: String = path ?: "dummy"
            val uri = getUri(activity!!, bitmap, finalPath)
            scanner!!.onScanFinish(uri)
            return bitmap
        }

        override fun onPostExecute(bitmap: Bitmap) {
            super.onPostExecute(bitmap)
            bitmap.recycle()
            dismissDialog()
        }
    }

}