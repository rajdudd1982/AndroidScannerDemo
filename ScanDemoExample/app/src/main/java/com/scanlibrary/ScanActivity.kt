package com.scanlibrary

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import com.scanlibrary.helpers.MediaHelper
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.BaseMediaScannerActivity
import com.scanner.demo.scanlibrary.ScanConstants
import java.util.logging.Logger

class ScanActivity : BaseMediaScannerActivity() {
    private lateinit var fileUri: Uri

    companion object {
        init {
            try {
               // System.loadLibrary("opencv_java3")
                System.loadLibrary("Scanner")
            } catch (e: java.lang.Exception){
                Logger.getLogger(e.message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_layout)

        // open camera or gallery
        when(intent.getIntExtra(ScanConstants.OPEN_INTENT_PREFERENCE, 0)){
            ScanConstants.OPEN_MEDIA -> MediaHelper.openMediaContent(this)
            ScanConstants.OPEN_CAMERA -> {
                fileUri = Uri.fromFile(MediaHelper.openCamera(this, getFolderPath()))
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
            bitmap?.let { MediaHelper.postImagePick(this, it, getFolderPath()) }
        } else {
            finish()
        }
    }

    private fun getFolderPath() : String {
        return  intent.getStringExtra(ScanConstants.FOLDER_PATH) ?: ""
    }

    external fun getScannedBitmap(bitmap: Bitmap, x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Bitmap

    external fun getGrayBitmap(bitmap: Bitmap): Bitmap

    external fun getMagicColorBitmap(bitmap: Bitmap): Bitmap

    external fun getBWBitmap(bitmap: Bitmap): Bitmap

    external fun getPoints(bitmap: Bitmap): FloatArray

}