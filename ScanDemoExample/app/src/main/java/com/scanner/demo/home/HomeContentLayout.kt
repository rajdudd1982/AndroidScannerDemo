package com.scanner.demo.home

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.scanlibrary.helpers.FileHelper
import com.scanner.demo.helpers.*
import com.scanner.demo.listeners.ItemClickListener
import com.scanner.demo.scanlibrary.ScanConstants
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.io.File

open class  HomeContentLayout : RelativeLayout {
    lateinit var adapter: FolderListAdapter
    var docViewModelList: MutableList<SavedDocViewModel> = ArrayList()
    lateinit var activity : Activity

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int=0) : super(context, attrs, defStyleAttr)
    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        recyclerView.setTouchListener()
        adapter = FolderListAdapter(context)
        recyclerView.adapter = adapter
    }

    fun listItems(filePath: String = FileHelper.getDefaultFolderPath()) {
        FileInterimHelper.getImagesInFolder(ScanConstants.FINAL_IMAGE_FOLDER_PREFIX_PATH, itemClickListener)?.apply {
            adapter.addAllItems(this)
        }
    }


    private val itemClickListener: ItemClickListener<SavedDocViewModel> = object : ItemClickListener<SavedDocViewModel> {
        override fun onItemClick(item: SavedDocViewModel) {
            when(item.lastClickedItemType) {
                SavedDocViewModel.ClickedItemType.Camera -> IntentHelper.startScan(activity, ScanConstants.OPEN_CAMERA, item)
                SavedDocViewModel.ClickedItemType.Delete -> onDeleteItem( item)
                SavedDocViewModel.ClickedItemType.CreatePdf -> onCreatePdf( item)
            }
        }
    }

    private fun onDeleteItem(savedDocViewModel: SavedDocViewModel) {
        savedDocViewModel.file.delete()
        try {
            adapter.removeItem(savedDocViewModel.position)
        } catch (e: Exception){
            adapter.notifyDataSetChanged()
        }
    }

    private fun onCreatePdf(savedDocViewModel: SavedDocViewModel) {
        var pdfFolder = File(context.cacheDir, "shared_document")
        FileHelper.createFolder(pdfFolder.path)
        var pdfFile = File(pdfFolder, "doc.pdf")
        pdfFile.createNewFile()
        PdfConverter.createPdf(savedDocViewModel.file.listFiles().asList(), pdfFile.path)
        AndroidHelper.openPdfApplication(pdfFile.path)
    }
}