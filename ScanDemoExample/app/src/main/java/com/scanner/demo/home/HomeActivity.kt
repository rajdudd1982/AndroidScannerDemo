package com.scanner.demo.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.scanlibrary.BaseActivity
import com.scanlibrary.ScanActivity
import com.scanner.demo.R
import com.scanner.demo.helpers.FileHelper
import com.scanner.demo.helpers.PdfConverter
import com.scanner.demo.scanlibrary.ScanConstants
import com.scanner.demo.scanlibrary.helpers.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        requestPermission(PermissionHelper.PermissionCode.WRITE_PERMISSION)
        homeNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.gallery ->  startScan(ScanConstants.OPEN_MEDIA, getAndCreateFinalFolder())
                R.id.camera -> requestPermission(PermissionHelper.PermissionCode.CAMERA_PERMISSION)
                R.id.scan ->  startScan(0, getAndCreateFinalFolder())
                else -> {}
            }
            false
        }
    }

    fun getAndCreateFinalFolder() : String {
        var folderName = "final_${System.currentTimeMillis()}"
        var folder = File(FileHelper.getFinalPathDirectory(), folderName)
        FileHelper.makeDir(folder)
        return folder.path
    }

    override fun onPermissionGranted(permission: String, requestCode: Int, isGranted: Boolean) {
        super.onPermissionGranted(permission, requestCode, isGranted)
        when(permission) {
            android.Manifest.permission.CAMERA ->  startScan(ScanConstants.OPEN_CAMERA, getAndCreateFinalFolder())
        }
    }

    private fun startScan(preference: Int, folderName: String) {
        val intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        intent.putExtra(ScanConstants.FOLDER_PATH, folderName)
        startActivityForResult(intent, REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uris = data!!.extras!!.getParcelableArrayList<Uri>(ScanConstants.SCANNED_RESULT)
            var bitmap: Bitmap? = null
            try {
                ((homeFragment as NavHostFragment).childFragmentManager?.fragments?.first() as HomeFragment).reloadListItems()
                //bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
               // contentResolver.delete(uri!!, null, null)
                //PdfConverter.createPdfFromBitmap(bitmap,   "${cacheDir}/file.pdf")
                //convertToPdf(Collections.singletonList(File(uri.path)), "");
                //scannedImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun convertToPdf(fileList: List<File>, dest: String) {
        PdfConverter.createPdf(fileList, dest)
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
        const val REQUEST_CODE = 99
    }
}