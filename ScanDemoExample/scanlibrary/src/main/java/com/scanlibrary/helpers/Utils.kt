package com.scanlibrary.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import com.scanlibrary.ScanConstants
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

/**
 * Created by jhansi on 05/04/15.
 */
//https://github.com/nextcloud/android/issues/6150
//https://android-developers.googleblog.com/2019/04/android-q-scoped-storage-best-practices.html
//Scoped Storage
https://www.raywenderlich.com/9577211-scoped-storage-in-android-10-getting-started#:~:text=In%20Android%2010%2C%20Google%20introduced,from%20the%20device's%20shared%20storage.
object Utils {
    @JvmStatic
    fun getUri(context: Context, bitmap: Bitmap, folderPath: String): Uri? {
        //val bytes = ByteArrayOutputStream()
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
       //val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return initImageSaving(context, bitmap, folderPath) //Uri.parse(path)//
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getBitmap(context: Context, uri: Uri?): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    @SuppressLint("InlinedApi", "ObsoleteSdkInt")
    private fun initImageSaving(context: Context, bitmap: Bitmap, folderName: String)  : Uri? {
        FileHelper.createFolder(ScanConstants.FINAL_IMAGE_FOLDER_PREFIX_PATH)
        var folderNameNew = folderName
        if (TextUtils.isEmpty(folderName)) {
            folderNameNew = "${ScanConstants.INTERMEDIATE_FOLDERS_PREFIX}_${System.currentTimeMillis()}"
            FileHelper.createFolder(File(ScanConstants.FINAL_IMAGE_FOLDER_PREFIX_PATH, folderNameNew).path)
        }
        val relativeLocation = "${ScanConstants.FINAL_IMAGE_FOLDER_PREFIX_PATH}${File.pathSeparator}$folderNameNew"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${ScanConstants.FINAL_IMAGE_PREFIX}_${System.currentTimeMillis().toString()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            } else {
                put(MediaStore.MediaColumns.DATA, relativeLocation)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {

            uri?.let { uri ->
                val stream = resolver.openOutputStream(uri)

                stream?.let { stream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream)) {
                        throw IOException("Failed to save bitmap.")
                    }
                } ?: throw IOException("Failed to get output stream.")

            } ?: throw IOException("Failed to create new MediaStore record")

        } catch (e: IOException) {
            if (uri != null) {
                resolver.delete(uri, null, null)
            }
            throw IOException(e)
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        return uri
    }
}