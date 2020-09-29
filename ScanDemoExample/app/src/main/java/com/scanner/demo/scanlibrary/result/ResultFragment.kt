package com.scanner.demo.scanlibrary.result

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scanner.demo.scanlibrary.helpers.Utils
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.BaseScanFragment
import com.scanlibrary.ScanActivity
import com.scanner.demo.scanlibrary.ScanConstants
import com.scanner.demo.scanlibrary.helpers.BitmapTransformationHelper
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
        doneButton!!.setOnClickListener(DoneButtonClickListener(uris[currentPosition]))

        imageGradingNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.original) {
                BWButtonClickListener(BitmapTransformationHelper.TransformationType.Original).onClick(view)
            } else if (item.itemId == R.id.magicColor) {
                BWButtonClickListener(BitmapTransformationHelper.TransformationType.Magic).onClick(view)
            } else if (item.itemId == R.id.black_white) {
                BWButtonClickListener(BitmapTransformationHelper.TransformationType.BW).onClick(view)
            } else if (item.itemId == R.id.gray_mode) {
                BWButtonClickListener(BitmapTransformationHelper.TransformationType.Gray).onClick(view)
            }
            false
        }
    }

    private inner class BWButtonClickListener(val type: BitmapTransformationHelper.TransformationType) : View.OnClickListener {
        override fun onClick(v: View) {
            showProgressDialog(resources.getString(R.string.applying_filter))
            AsyncTask.execute {
                try {
                    transformed = BitmapTransformationHelper(activity!! as ScanActivity).bitmapTransformationByType(original!!, type)
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

    private inner class DoneButtonClickListener(val uri: Uri?) : View.OnClickListener {
        override fun onClick(v: View) {
            showProgressDialog(resources.getString(R.string.loading))
            AsyncTask.execute {
                try {
                    onDoneClick()
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissDialog()
                }
            }
        }
    }

    @Throws(java.lang.Exception::class)
    private fun onDoneClick() {
        var bitmap = transformed ?: original
        val uri = Utils.insertFinalImages(bitmap!!, folderPath!!)
        original!!.recycle()
        System.gc()
        finalUris.add(uri)
        currentPosition++
        if (currentPosition != uris.size) {
            Handler(Looper.getMainLooper()).post{
                original = getCurrentBitmap(currentPosition)
                scannedImageView.setImageBitmap(original)
                dismissDialog()
            }
            return
        }

        val data = Intent()
        data.putParcelableArrayListExtra(ScanConstants.SCANNED_RESULT, finalUris)
        activity!!.setResult(Activity.RESULT_OK, data)
        activity!!.runOnUiThread {
            dismissDialog()
            activity!!.finish()
        }
    }
}