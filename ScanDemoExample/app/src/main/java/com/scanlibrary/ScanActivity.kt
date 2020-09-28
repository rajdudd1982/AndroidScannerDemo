package com.scanlibrary

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import com.scanlibrary.helpers.MediaHelper
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.ScanConstants
import com.scanner.demo.scanlibrary.helpers.Utils
import java.util.*
import java.util.logging.Logger

class ScanActivity : BaseMediaScannerActivity() {
    private lateinit var fileUri: Uri
    init {

        try {
            // System.loadLibrary("opencv_java3")
            System.loadLibrary("Scanner")
        } catch (e: java.lang.Exception){
            Logger.getLogger(e.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_layout)

        if( intent.hasExtra(ScanConstants.SELECTED_BITMAP) != null) {
            onBitmapSelect(intent.getParcelableArrayListExtra(ScanConstants.SELECTED_BITMAP)!!)
        } else {

            // Clear cache directory
            com.scanner.demo.helpers.FileHelper.clearCacheDir()
            openCameraOrGallery()

        }
    }

    private fun openCameraOrGallery(){
        // open camera or gallery
        when(intent.getIntExtra(ScanConstants.OPEN_INTENT_PREFERENCE, 0)){
            ScanConstants.OPEN_MEDIA -> MediaHelper.openGallery(this)
            ScanConstants.OPEN_CAMERA -> {
                fileUri = Uri.fromFile(MediaHelper.openCamera(this))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var bitmap: Bitmap? = null
            try {
                when (requestCode) {
                    ScanConstants.START_CAMERA_REQUEST_CODE -> bitmap = MediaHelper.getBitmap(this, fileUri)
                    ScanConstants.PICKFILE_REQUEST_CODE -> bitmap = MediaHelper.getBitmap(this, data!!.data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // Perform operation for saving final image after cropping and graying out
            bitmap?.let {postImagePick(this, it) }
        } else {
            finish()
        }
    }

    /**
     * Save cached temporary image into media
     */
    private fun postImagePick(activity: Activity, bitmap: Bitmap) {
        val uri = Utils.insertCachedImage(bitmap)
        bitmap.recycle()
        onBitmapSelect(Collections.singletonList(uri))
    }

    external fun getScannedBitmap(bitmap: Bitmap, x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Bitmap
    external fun getGrayBitmap(bitmap: Bitmap): Bitmap
    external fun getMagicColorBitmap(bitmap: Bitmap): Bitmap
    external fun getBWBitmap(bitmap: Bitmap): Bitmap
    external fun getPoints(bitmap: Bitmap): FloatArray

}