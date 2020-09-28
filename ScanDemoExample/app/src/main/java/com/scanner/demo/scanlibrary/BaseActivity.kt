package com.scanlibrary

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.scanner.demo.scanlibrary.helpers.PermissionHelper

open abstract class BaseActivity : AppCompatActivity() {

    fun requestPermission(permissionCode: PermissionHelper.PermissionCode) {
        var permission = permissionCode.getPermission()
        var requestCode = permissionCode.getRequestCode()
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

    protected  fun getActivity() : Activity {
        return this
    }

}


