package com.scanner.demo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.scanlibrary.BaseMediaScannerActivity
import com.scanlibrary.ScanConstants
import com.scanner.demo.helpers.MediaHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class HomeActivity : BaseMediaScannerActivity() {

    private val scannedImageView: ImageView? = null
    private lateinit var fileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 13)
        homeNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.gallery -> {
                    startScan(ScanConstants.OPEN_MEDIA)
                }
                R.id.camera -> {
                    requestPermission(android.Manifest.permission.CAMERA, 12)
                }
                R.id.scan -> {
                    startScan(0)
                }
                else -> {
                }
            }
            false
        }
    }

    override fun onPermissionGranted(permission: String, requestCode: Int, isGranted: Boolean) {
        super.onPermissionGranted(permission, requestCode, isGranted)
        when(permission) {
            android.Manifest.permission.CAMERA ->  startScan(ScanConstants.OPEN_CAMERA)
        }
    }

    private fun startScan(preference: Int) {
        when(preference){
            ScanConstants.OPEN_MEDIA -> MediaHelper.openMediaContent(this)
            ScanConstants.OPEN_CAMERA -> fileUri = Uri.fromFile(MediaHelper.openCamera(this))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data!!.extras!!.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                contentResolver.delete(uri!!, null, null)
                scannedImageView!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (resultCode == Activity.RESULT_OK) {
            var bitmap: Bitmap? = null
            try {
                when (requestCode) {
                    ScanConstants.START_CAMERA_REQUEST_CODE -> bitmap = MediaHelper.getBitmap(this, fileUri)
                    ScanConstants.PICKFILE_REQUEST_CODE -> bitmap = MediaHelper.getBitmap(this, data!!.data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            bitmap?.let { MediaHelper.postImagePick(this, it) }
        } else {
           finish()
        }

    }

    private fun convertByteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    companion object {
        private const val REQUEST_CODE = 99
    }
}