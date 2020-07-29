package com.scanner.demo.helpers

import android.app.Activity
import android.content.Intent
import com.scanner.demo.home.HomeActivity
import com.scanner.demo.home.SavedDocViewModel
import com.scanner.demo.scanlibrary.ScanActivity
import com.scanner.demo.scanlibrary.ScanConstants

object IntentHelper {

     fun startScan(activity: Activity, preference: Int, savedDocViewModel : SavedDocViewModel) {
        val intent = Intent(activity, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
         //intent.putExtra(ScanConstants.FOLDER_PATH, savedDocViewModel.file.path)
        activity.startActivityForResult(intent, HomeActivity.REQUEST_CODE)
    }
}