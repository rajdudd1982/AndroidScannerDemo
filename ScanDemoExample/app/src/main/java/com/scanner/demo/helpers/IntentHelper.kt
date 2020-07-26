package com.scanner.demo.helpers

import android.app.Activity
import android.content.Intent
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import com.scanner.demo.home.HomeActivity

object IntentHelper {

    private fun startScan(activity: Activity, preference: Int) {
        val intent = Intent(activity, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        activity.startActivityForResult(intent, HomeActivity.REQUEST_CODE)
    }
}