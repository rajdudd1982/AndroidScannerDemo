package com.scanner.demo.scanlibrary.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.scanner.demo.helpers.AndroidHelper
import com.scanner.demo.helpers.FileHelper
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.logging.Logger

/**
 * Created by jhansi on 05/04/15.
 */
object Utils {
    private val TAG = Utils.javaClass.name
    @JvmStatic
    fun getUri(context: Context, bitmap: Bitmap, folderPath: String?): Uri? {
        //val bytes = ByteArrayOutputStream()
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        //val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return getUri(context, bitmap) //Uri.parse(path)//
    }
    @JvmStatic
    fun getUri(context: Context, bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "temp_${System.currentTimeMillis()}", null)
        return Uri.parse(path)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getBitmap(context: Context, uri: Uri?): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    fun insertCachedImage(bitmap: Bitmap) : Uri? {
        var fileName = FileHelper.createExternalStorageTempFile("temp_${System.currentTimeMillis()}")
        return initImageSaving(bitmap, FileHelper.getExternalStorageTempRelativePath(), fileName.name)
    }

    fun insertFinalImages(bitmap: Bitmap, folderPath: String) : Uri? {
        var file = FileHelper.createFile(folderPath,"final_${System.currentTimeMillis()}")
        var relativeFolderPath = FileHelper.getFinalRelativePath() + "/" + folderPath.split("/").last()
        return initImageSaving(bitmap, relativeFolderPath, file.name)
    }

    @SuppressLint("InlinedApi", "ObsoleteSdkInt")
    private fun initImageSaving(bitmap: Bitmap, folderPath: String, fileName: String)  : Uri? {
        val contentValues = getContentValues(folderPath, fileName)

        Log.d(TAG, " Folder Name: $folderPath uri: ${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}")

        val resolver = AndroidHelper.appContext().contentResolver
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

    private fun getContentValues(folderPath: String, fileName: String) : ContentValues {
        var relativeLocation = "$folderPath"

        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis());
            put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            } else {
                put(MediaStore.MediaColumns.DATA, relativeLocation)
            }
        }
    }

}