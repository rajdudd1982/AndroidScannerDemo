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
import com.scanner.demo.scanlibrary.IScanner
import com.scanner.demo.scanlibrary.ScanConstants
import java.io.File
import java.io.IOException


object MediaHelper {

    @JvmStatic
    fun openCamera(activity: Activity, folderPath: String = FileHelper.getDefaultFolderPath()) : File? {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val file: File? = FileHelper.createTempImageFile(activity)
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


    @JvmStatic
    fun openMediaContent(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), ScanConstants.PICKFILE_REQUEST_CODE)

//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "image/*"
//        activity.startActivityForResult(intent, ScanConstants.PICKFILE_REQUEST_CODE)
    }



    fun postImagePick(activity: Activity, bitmap: Bitmap, folderPath: String) {
        val uri = Utils.getUri(activity, bitmap)
        bitmap.recycle()
        (activity as IScanner).onBitmapSelect(uri)
    }

    @Throws(IOException::class)
    fun getBitmap(activity: Activity, selectedImg: Uri?): Bitmap? {
        val options = BitmapFactory.Options()
        options.inSampleSize = 3
        var fileDescriptor: AssetFileDescriptor? = null
        fileDescriptor = activity.contentResolver.openAssetFileDescriptor(selectedImg!!, "r")
        return BitmapFactory.decodeFileDescriptor(fileDescriptor?.fileDescriptor, null, options)
    }
}