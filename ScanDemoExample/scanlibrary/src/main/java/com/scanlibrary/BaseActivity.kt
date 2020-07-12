package com.scanlibrary

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

open abstract class BaseActivity : AppCompatActivity() {

     enum class PermissionCode {
         WRITE_PERMISSION {
             override fun getPermission(): String {
                 return android.Manifest.permission.WRITE_EXTERNAL_STORAGE
             }

             override fun getRequestCode(): Int {
                 return 1001
             }
         },

         CAMERA_PERMISSION {
             override fun getPermission(): String {
                 return android.Manifest.permission.CAMERA
             }

             override fun getRequestCode(): Int {
                 return 1002
             }
         };

         abstract fun getPermission() : String
         abstract fun getRequestCode() : Int
     }

    fun requestPermission(permissionCode: PermissionCode) {
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

}


