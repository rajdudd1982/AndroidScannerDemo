package com.scanlibrary.helpers

import android.app.Activity
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.scanlibrary.IScanner
import com.scanlibrary.ScanConstants
import com.scanlibrary.Utils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object MediaHelper {

    @JvmStatic
    fun openCamera(activity: Activity) : File {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createImageFile()
        val isDirectoryCreated = file.parentFile.mkdirs()
        Log.d("", "openCamera: isDirectoryCreated: $isDirectoryCreated")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val tempFileUri = FileProvider.getUriForFile(activity,
                    "com.scanlibrary.provider",  // As defined in Manifest
                    file)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
        } else {
            val tempFileUri = Uri.fromFile(file)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri)
        }
        activity.startActivityForResult(cameraIntent, ScanConstants.START_CAMERA_REQUEST_CODE)
        return file
    }

    private fun createImageFile(): File {
        clearTempFolder()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File(ScanConstants.IMAGE_PATH, "IMG_" + timeStamp + ".jpg")
    }


    private fun clearTempFolder() {
        try {
            val tempFolder = File(ScanConstants.IMAGE_PATH)
            for (f in tempFolder.listFiles()) f.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun openMediaContent(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        activity.startActivityForResult(intent, ScanConstants.PICKFILE_REQUEST_CODE)
    }

    fun postImagePick(activity: Activity, bitmap: Bitmap) {
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