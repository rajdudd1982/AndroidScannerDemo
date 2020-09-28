package com.scanner.demo.camerax

import android.net.Uri
import androidx.lifecycle.ViewModel

class CachedFileViewModel : ViewModel() {
    var fileUri: Uri? = null
    var localCache = true
    var actionPerformed: LastAction = LastAction.NA

     enum class LastAction {
         DELETE, NA;
     }
}