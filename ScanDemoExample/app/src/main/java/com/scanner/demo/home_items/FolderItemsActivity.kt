package com.scanner.demo.home_items

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.scanlibrary.BaseActivity
import com.scanner.demo.R
import com.scanner.demo.helpers.AndroidHelper
import com.scanner.demo.helpers.PdfConverter
import com.scanner.demo.home.HomeActivity
import com.scanner.demo.home.HomeFragment
import com.scanner.demo.scanlibrary.ScanConstants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.folder_items_main_layout.*
import kotlinx.android.synthetic.main.home_fragment.recyclerView
import java.io.File
import java.io.IOException
//https://github.com/treesouth/AndroidLibrary
class FolderItemsActivity :  BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.folder_items_main_layout)

        folderItemLayout.activity = this
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        setItemList()
    }

     private fun setItemList() {
        val safeArgs: FolderItemsActivityArgs by navArgs()
        val docViewModel = safeArgs.savedDocViewModel

        folderItemLayout.adapter.viewType = FolderItemsAdapter.ViewType.Grid
        folderItemLayout.listItems(docViewModel.image)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HomeActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data!!.extras!!.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
            try {
                setItemList()
                var pdfFile = File(cacheDir, "doc.pdf")
                pdfFile.createNewFile()
                PdfConverter.createPdfFromBitmap(folderItemLayout.getItemsList(),   pdfFile.path)
                AndroidHelper.openPdfApplication(pdfFile.path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}