package com.scanlibrary

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

open abstract class BaseActivity : FragmentActivity() {

    fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            onPermissionGranted(permission, requestCode, true)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isPermissionGranted = grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED
        onPermissionGranted(permissions[0], requestCode, isPermissionGranted)
    }

    protected open fun onPermissionGranted(permission: String, requestCode: Int, isGranted: Boolean){}

}


