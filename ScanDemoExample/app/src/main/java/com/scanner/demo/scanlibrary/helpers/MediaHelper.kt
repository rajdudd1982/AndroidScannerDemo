package com.scanlibrary.helpers

import android.app.Activity
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.scanner.demo.R
import com.scanner.demo.camerax.CachedFileViewModel
import com.scanner.demo.helpers.FileHelper
import com.scanner.demo.scanlibrary.ScanConstants
import kotlinx.android.synthetic.main.camera_layout.*
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object MediaHelper {

    /**
     * Open camera..
     */
    @JvmStatic
    fun openCamera(activity: Activity) : File? {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File? = FileHelper.createSharedCacheFile("doc_${Calendar.getInstance().timeInMillis}.png")
        file?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val tempFileUri = FileProvider.getUriForFile(activity,
                        activity.getString(R.string.authority),
                        file)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
            } else {
                val tempFileUri = Uri.fromFile(file)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
            }
            activity.startActivityForResult(cameraIntent, ScanConstants.START_CAMERA_REQUEST_CODE)
        }

        return file
    }

    /**
     * Open Gallery
     */
    @JvmStatic
    fun openGallery(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Pictures"), ScanConstants.PICKFILE_REQUEST_CODE)
    }


    @Throws(IOException::class)
    fun getBitmap(activity: Activity, selectedImg: Uri?): Bitmap? {
        val options = BitmapFactory.Options()
        options.inSampleSize = 3
        var fileDescriptor: AssetFileDescriptor? = null
        fileDescriptor = activity.contentResolver.openAssetFileDescriptor(selectedImg!!, "r")
        return BitmapFactory.decodeFileDescriptor(fileDescriptor?.fileDescriptor, null, options)
    }

    @JvmStatic
    fun getSelectedFiles(data: Intent?): ArrayList<CachedFileViewModel> {
        var cachedItems: ArrayList<CachedFileViewModel>  = ArrayList()
        data?.clipData?.let {
            var cachedFileViewModel: CachedFileViewModel
            for (index in 0 until it.itemCount) {
                cachedFileViewModel = CachedFileViewModel()
                cachedFileViewModel.fileUri = it.getItemAt(index).uri
                cachedFileViewModel.localCache = false
                cachedItems.add(cachedFileViewModel)
            }
        }
        return cachedItems
    }
}