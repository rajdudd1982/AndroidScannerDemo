package com.scanner.demo.scanlibrary

import android.graphics.Bitmap
import android.net.Uri
import androidx.fragment.app.Fragment
import com.scanlibrary.helpers.Utils
import java.io.IOException

open class BaseScanFragment @JvmOverloads constructor(contentLayoutId: Int=0): Fragment(contentLayoutId) {

    protected val bitmap: Bitmap?
        protected get() {
            val uri = uri
            try {
                val bitmap = Utils.getBitmap(activity!!, uri)
                activity!!.contentResolver.delete(uri!!, null, null)
                return bitmap
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

    protected val uri: Uri?
        protected get() = arguments!!.getParcelable(ScanConstants.SELECTED_BITMAP)

    protected val folderPath: String?
        protected get() = arguments!!.getString(ScanConstants.FOLDER_PATH)


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