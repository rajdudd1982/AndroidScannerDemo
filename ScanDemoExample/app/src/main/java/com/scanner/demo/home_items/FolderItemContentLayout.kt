package com.scanner.demo.home_items

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.scanlibrary.helpers.FileHelper
import com.scanner.demo.helpers.FileInterimHelper
import com.scanner.demo.helpers.IntentHelper
import com.scanner.demo.home.SavedDocViewModel
import com.scanner.demo.listeners.ItemClickListener
import com.scanner.demo.scanlibrary.ScanConstants
import com.scanner.demo.ui.base.PdfIconsLayout
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.io.File

class FolderItemContentLayout : RelativeLayout {

    lateinit var adapter: FolderItemsAdapter
    var docViewModelList: MutableList<SavedDocViewModel> = ArrayList()
    lateinit var activity : Activity

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int=0) : super(context, attrs, defStyleAttr)
    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        recyclerView.setTouchListener()
        adapter = FolderItemsAdapter(context)
        recyclerView.adapter = adapter
    }

    fun listItems(filePath: String? = FileHelper.getDefaultFolderPath()) {
        FileInterimHelper.getImagesInFolder(filePath, itemClickListener)?.apply {
            adapter.addAllItems(this)
        }
        adapter.addItem(getAddMoreItem(filePath), adapter.itemCount)
    }

    private fun getAddMoreItem(folderPath: String?):  SavedDocViewModel {
        var addMoreItem = SavedDocViewModel(null)
        addMoreItem.addMoreItem = true
       // addMoreItem.file = File(folderPath)
        addMoreItem.itemClickListener = itemClickListener
        return addMoreItem
    }

    private val itemClickListener: ItemClickListener<SavedDocViewModel> = object : ItemClickListener<SavedDocViewModel>{
        override fun onItemClick(item: SavedDocViewModel) {
            when(item.lastClickedItemType) {
                SavedDocViewModel.ClickedItemType.Camera -> IntentHelper.startScan(activity, ScanConstants.OPEN_CAMERA, item)
            }
        }
    }
}