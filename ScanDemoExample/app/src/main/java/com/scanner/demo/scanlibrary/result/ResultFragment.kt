package com.scanlibrary

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.scanlibrary.helpers.Utils
import com.scanlibrary.helpers.Utils.getBitmap
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.ProgressDialogFragment
import com.scanner.demo.scanlibrary.ScanConstants
import com.scanner.demo.scanlibrary.result.BitmapTransformation
import java.io.IOException

/**
 * Created by jhansi on 29/03/15.
 */
class ResultFragment : Fragment() {
    private var mainView: View? = null
    private var scannedImageView: ImageView? = null
    private var doneButton: Button? = null
    private var original: Bitmap? = null
    private var transformed: Bitmap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.result_layout, null)
        init()
        return mainView
    }

    private fun init() {
        scannedImageView = mainView!!.findViewById<View>(R.id.scannedImage) as ImageView
        val bitmap = bitmap
        setScannedImage(bitmap)
        doneButton = mainView!!.findViewById<View>(R.id.doneButton) as Button
        doneButton!!.setOnClickListener(DoneButtonClickListener(uri))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.findViewById<View>(R.id.imageGradingNavigationView) as BottomNavigationView).setOnNavigationItemSelectedListener { item ->
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

    private val bitmap: Bitmap?
        private get() {
            val uri = uri
            try {
                original = getBitmap(activity!!, uri)
                activity!!.contentResolver.delete(uri!!, null, null)
                return original
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

    private val uri: Uri?
        private get() = arguments!!.getParcelable(ScanConstants.SCANNED_RESULT)

    fun setScannedImage(scannedImage: Bitmap?) {
        scannedImageView!!.setImageBitmap(scannedImage)
    }

    private inner class DoneButtonClickListener(uri: Uri?) : View.OnClickListener {
        override fun onClick(v: View) {
            showProgressDialog(resources.getString(R.string.loading))
            AsyncTask.execute {
                try {
                    val data = Intent()
                    var bitmap = transformed
                    if (bitmap == null) {
                        bitmap = original
                    }
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


    @Synchronized
    protected fun showProgressDialog(message: String?) {
        if (progressDialogFragment != null && progressDialogFragment!!.isVisible) {
            // Before creating another loading dialog, close all opened loading dialogs (if any)
            progressDialogFragment!!.dismissAllowingStateLoss()
        }
        progressDialogFragment = null
        progressDialogFragment = ProgressDialogFragment(message)
        val fm = fragmentManager
        progressDialogFragment!!.show(fm!!, ProgressDialogFragment::class.java.toString())
    }

    @Synchronized
    protected fun dismissDialog() {
        progressDialogFragment!!.dismissAllowingStateLoss()
    }

    companion object {
        private var progressDialogFragment: ProgressDialogFragment? = null
    }
}