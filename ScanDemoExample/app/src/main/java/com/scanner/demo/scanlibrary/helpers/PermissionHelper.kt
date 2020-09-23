package com.scanner.demo.scanlibrary.helpers

object PermissionHelper {

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


}