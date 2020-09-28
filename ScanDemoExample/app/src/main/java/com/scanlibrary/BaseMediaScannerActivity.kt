package com.scanlibrary

import android.app.AlertDialog
import android.content.ComponentCallbacks2
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.scanner.demo.R

import com.scanner.demo.scanlibrary.IScanner
import com.scanner.demo.scanlibrary.ScanConstants
import com.scanner.demo.scanlibrary.result.ResultFragment
import com.scanner.demo.scanlibrary.scan.ScanFragment

open abstract class BaseMediaScannerActivity : FragmentActivity(), IScanner, ComponentCallbacks2 {

    override fun onBitmapSelect(uri: List<Uri?>) {
        val fragment = ScanFragment()
        fragment.arguments = getBundle(uri)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.content, fragment)
        fragmentTransaction.addToBackStack(ScanFragment::class.java.toString())
        fragmentTransaction.commit()
    }



    override fun onScanFinish(uri: List<Uri?>) {
        val fragment = ResultFragment()
        fragment.arguments = getBundle(uri)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.content, fragment)
        fragmentTransaction.addToBackStack(ResultFragment::class.java.toString())
        fragmentTransaction.commit()
    }

    private fun getBundle(uri: List<Uri?>) : Bundle {
        val bundle = Bundle()
        //scanned bitmap
        bundle.putParcelableArrayList(ScanConstants.SELECTED_BITMAP, ArrayList(uri))
        bundle.putString(ScanConstants.FOLDER_PATH, getFolderPath())
        return bundle
    }

    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE, ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW, ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND, ComponentCallbacks2.TRIM_MEMORY_MODERATE, ComponentCallbacks2.TRIM_MEMORY_COMPLETE ->                 /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */AlertDialog.Builder(this)
                    .setTitle(R.string.low_memory)
                    .setMessage(R.string.low_memory_message)
                    .create()
                    .show()
            else -> {
            }
        }
    }

    protected fun getFolderPath() : String {
        return  intent.getStringExtra(ScanConstants.FOLDER_PATH) ?: ""
    }

}