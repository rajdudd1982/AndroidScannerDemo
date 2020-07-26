package com.scanner.demo.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.scanner.demo.DocScannerApplication
import com.scanner.demo.R
import java.io.File


object AndroidHelper {

    fun appContext() = DocScannerApplication.context

    fun  showToast(message: String = "Hi") = Toast.makeText(appContext(), message, Toast.LENGTH_LONG).show()

    fun openPdfApplication(filePath: String) {
        var intent: Intent
        var context: Context = appContext()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val file = File(filePath)
            val uri: Uri = FileProvider.getUriForFile(context, getAuthority(), file)
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(filePath), "application/pdf")
            intent = Intent.createChooser(intent, "Open File")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun getAuthority() = appContext().getString(com.scanlibrary.R.string.authority)

}