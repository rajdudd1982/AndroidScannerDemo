package com.scanner.demo.camerax

import android.content.Intent
import android.os.Bundle
import com.scanlibrary.BaseActivity
import com.scanlibrary.helpers.MediaHelper
import com.scanner.demo.R
import com.scanner.demo.scanlibrary.ScanConstants
import com.scanner.demo.scanlibrary.helpers.PermissionHelper
import kotlinx.android.synthetic.main.camera_bottom_layout.*
import kotlinx.android.synthetic.main.camera_layout.*

class ImageCaptureActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_layout)
        imageCaptureLayout.setData(this)
        captureImage.setOnClickListener {
            requestPermission(PermissionHelper.PermissionCode.CAMERA_PERMISSION)
        }
        pickPhotos.setOnClickListener {
            requestPermission(PermissionHelper.PermissionCode.WRITE_PERMISSION)
        }
    }

    override fun onPermissionGranted(permission: String, requestCode: Int, isGranted: Boolean) {
        super.onPermissionGranted(permission, requestCode, isGranted)
        when(requestCode){
            PermissionHelper.PermissionCode.CAMERA_PERMISSION.getRequestCode() -> imageCaptureLayout.onImageCaptureClick()
            PermissionHelper.PermissionCode.WRITE_PERMISSION.getRequestCode() -> MediaHelper.openGallery(getActivity())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) return
        when (requestCode){
            ScanConstants.PICKFILE_REQUEST_CODE -> {
                imageCaptureLayout.addUris(MediaHelper.getSelectedFiles(data))
            }
        }
    }
}