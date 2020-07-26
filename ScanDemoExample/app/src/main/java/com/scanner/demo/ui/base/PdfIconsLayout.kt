package com.scanner.demo.ui.base

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.scanner.demo.home.SavedDocViewModel
import kotlinx.android.synthetic.main.icon_wrapper_layout.view.*

class PdfIconsLayout : LinearLayout {

    lateinit var savedDocViewModel: SavedDocViewModel

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    @TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        deleteDoc.setOnClickListener {
          // item got deleted
            savedDocViewModel.lastClickedItemType = SavedDocViewModel.ClickedItemType.Delete
            savedDocViewModel.itemClickListener.onItemClick(savedDocViewModel)
        }

        createPdf.setOnClickListener {
            // item got deleted
            savedDocViewModel.lastClickedItemType = SavedDocViewModel.ClickedItemType.CreatePdf
            savedDocViewModel.itemClickListener.onItemClick(savedDocViewModel)
        }

        shareDoc.setOnClickListener {
            // item got deleted
            savedDocViewModel.lastClickedItemType = SavedDocViewModel.ClickedItemType.Share
            savedDocViewModel.itemClickListener.onItemClick(savedDocViewModel)
        }
    }

}