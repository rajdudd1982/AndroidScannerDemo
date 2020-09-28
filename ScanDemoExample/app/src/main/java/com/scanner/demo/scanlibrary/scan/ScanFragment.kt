package com.scanner.demo.scanlibrary.scan

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.PointF
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scanlibrary.ScanActivity
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.BaseScanFragment
import com.scanner.demo.scanlibrary.IScanner
import com.scanner.demo.scanlibrary.ProgressDialogFragment
import com.scanner.demo.scanlibrary.SingleButtonDialogFragment
import com.scanner.demo.scanlibrary.helpers.BitmapTransformationHelper
import com.scanner.demo.scanlibrary.helpers.Utils
import kotlinx.android.synthetic.main.scan_fragment_bottom_layout.*
import kotlinx.android.synthetic.main.scan_fragment_layout.*

/**
 * Created by jhansi on 29/03/15.
 */
class ScanFragment : BaseScanFragment() {
    private var progressDialogFragment: ProgressDialogFragment? = null

    private var scanner: IScanner? = null
    private var original: Bitmap? = null

    private lateinit var bitmapTransformation: BitmapTransformationHelper

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity !is IScanner) {
            throw ClassCastException("Activity must implement IScanner")
        }
        scanner = activity

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bitmapTransformation = BitmapTransformationHelper(activity!!  as ScanActivity)
        return inflater.inflate(R.layout.scan_fragment_layout, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanLayout.bitmapTransformation = bitmapTransformation
        original = getCurrentBitmap(currentPosition)
        scanLayout.updateImageView(original)
        nextScan.setOnClickListener(ScanButtonClickListener(uris[currentPosition]))

    }


    private inner class ScanButtonClickListener(val uri: Uri?) : View.OnClickListener {
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

        override fun doInBackground(vararg params: Uri?): Bitmap {
            val bitmap = getScannedBitmap(original, points)
            val uri = Utils.getUri(activity!!, bitmap)
            finalUris.add(uri)
            currentPosition++
            if (currentPosition != uris.size){
                original = getCurrentBitmap(currentPosition)
                scanLayout.updateImageView(original)
            } else {
                scanner!!.onScanFinish(finalUris)
            }
            return bitmap
        }

        override fun onPostExecute(bitmap: Bitmap) {
            super.onPostExecute(bitmap)
            bitmap.recycle()
            dismissDialog()
        }
    }

}