package com.scanner.demo.home

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.scanlibrary.helpers.FileHelper
import com.scanner.demo.helpers.FileInterimHelper
import com.scanner.demo.ui.base.PdfIconsLayout
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.io.File

open class  HomeContentLayout : RelativeLayout {
    lateinit var adapter: FolderListAdapter
    var docViewModelList: MutableList<SavedDocViewModel> = ArrayList()

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
        FileInterimHelper.listItems(filePath, pdfIconListener)?.apply {
            adapter.addAllItems(this)
        }
    }

    private val pdfIconListener: PdfIconsLayout.PdfIconListener = object : PdfIconsLayout.PdfIconListener {
        override fun onDeleteItemClicked(savedDocViewModel: SavedDocViewModel) {
            savedDocViewModel.file.delete()
            try {
                adapter.removeItem(savedDocViewModel.position)
            } catch (e: Exception){
                adapter.notifyDataSetChanged()
            }


        }
    }
}