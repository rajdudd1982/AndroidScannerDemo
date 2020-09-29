package com.scanner.demo.home

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.scanner.demo.helpers.*
import com.scanner.demo.listeners.ItemClickListener
import com.scanner.demo.scanlibrary.ScanConstants
import kotlinx.android.synthetic.main.home_fragment.view.*

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

    fun listItems() {
        FileInterimHelper.getImagesInFolder(false, FileHelper.getFinalRelativePath().split("/").last(), itemClickListener)?.apply {
            adapter.setItemList(this)
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
        savedDocViewModel.image?.deleteFile(true)
        try {
            adapter.removeItem(savedDocViewModel.position)
        } catch (e: Exception){
            adapter.notifyDataSetChanged()
        }
    }

    private fun onCreatePdf(savedDocViewModel: SavedDocViewModel) {
        var pdfFile = com.scanner.demo.helpers.FileHelper.createExternalStorageFile("", savedDocViewModel.file.name)
        PdfConverter.createPdf(savedDocViewModel.file.listFiles().asList(), pdfFile.path)
        AndroidHelper.openPdfApplication(pdfFile.path)
    }
}