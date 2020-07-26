package com.scanner.demo.helpers

import android.widget.Toast
import com.scanner.demo.DocScannerApplication

object AndroidHelper {

    fun appContext() = DocScannerApplication.context

    fun  showToast(message: String = "Hi") = Toast.makeText(appContext(), message, Toast.LENGTH_LONG).show()
}