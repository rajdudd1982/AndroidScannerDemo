package com.scanner.demo.scanlibrary.result

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scanlibrary.helpers.Utils
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.BaseScanFragment
import com.scanlibrary.ScanActivity
import com.scanner.demo.scanlibrary.ScanConstants
import kotlinx.android.synthetic.main.result_layout.*

/**
 * Created by jhansi on 29/03/15.
 */
class ResultFragment : BaseScanFragment() {

    private var original: Bitmap? = null
    private var transformed: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.result_layout, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        original = bitmap

        scannedImageView.setImageBitmap(original)
        doneButton!!.setOnClickListener(DoneButtonClickListener(uri))

        imageGradingNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.original) {
                BWButtonClickListener(BitmapTransformation.TransformationType.Original).onClick(view)
            } else if (item.itemId == R.id.magicColor) {
                BWButtonClickListener(BitmapTransformation.TransformationType.Magic).onClick(view)
            } else if (item.itemId == R.id.black_white) {
                BWButtonClickListener(BitmapTransformation.TransformationType.BW).onClick(view)
            } else if (item.itemId == R.id.gray_mode) {
                BWButtonClickListener(BitmapTransformation.TransformationType.Gray).onClick(view)
            }
            false
        }
    }

    private inner class BWButtonClickListener(val type: BitmapTransformation.TransformationType) : View.OnClickListener {
        override fun onClick(v: View) {
            showProgressDialog(resources.getString(R.string.applying_filter))
            AsyncTask.execute {
                try {
                    transformed = BitmapTransformation(activity!! as ScanActivity).bitmapTransformationByType(original!!, type)
                } catch (e: OutOfMemoryError) {
                    activity!!.runOnUiThread {
                        transformed = original
                        scannedImageView!!.setImageBitmap(original)
                        dismissDialog()
                        onClick(v)
                    }
                }
                activity!!.runOnUiThread {
                    scannedImageView!!.setImageBitmap(transformed)
                    dismissDialog()
                }
            }
        }
    }

    private inner class DoneButtonClickListener(uri: Uri?) : View.OnClickListener {
        override fun onClick(v: View) {
            showProgressDialog(resources.getString(R.string.loading))
            AsyncTask.execute {
                try {
                    val data = Intent()
                    var bitmap = transformed ?: original

                    var path: String? = uri?.path
                    val finalPath: String = path ?: "dummy"
                    val uri = Utils.getUri(activity!!, bitmap!!, finalPath)
                    data.putExtra(ScanConstants.SCANNED_RESULT, uri)
                    activity!!.setResult(Activity.RESULT_OK, data)
                    original!!.recycle()
                    System.gc()
                    activity!!.runOnUiThread {
                        dismissDialog()
                        activity!!.finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}