package com.scanner.demo.home_items

import android.os.Bundle
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.scanlibrary.BaseActivity
import com.scanner.demo.R
import kotlinx.android.synthetic.main.folder_items_main_layout.*
import kotlinx.android.synthetic.main.home_fragment.recyclerView

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
}