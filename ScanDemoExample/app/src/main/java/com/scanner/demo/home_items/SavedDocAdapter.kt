package com.scanner.demo.home_items

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import com.scanner.demo.R
import com.scanner.demo.home.SavedDocViewModel
import com.scanner.demo.ui.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.home_list_item_layout.view.*
import kotlinx.android.synthetic.main.icon_wrapper_layout.view.*


class SavedDocAdapter(context: Context) : BaseRecyclerAdapter<SavedDocViewModel>(context) {

    var viewType: ViewType = ViewType.Linear

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SavedDocViewModel> {
        return return SavedGridDocViewHolder(context, layoutInflater.inflate(R.layout.doc_grid_item_layout, parent, false))
    }

    class SavedGridDocViewHolder(context: Context, view: View) : BaseViewHolder<SavedDocViewModel>(context, view) {
        override fun updateView(item: SavedDocViewModel, position: Int) {
            val bitmap = BitmapFactory.decodeFile(item.file.path)
            itemView.imageView.background = BitmapDrawable(context.resources, bitmap)
            setIconLayout(item, position)
        }

        private fun setIconLayout(savedDocViewModel: SavedDocViewModel, position: Int) {
            savedDocViewModel.position = position
            itemView.pdfIconsLayout.savedDocViewModel = savedDocViewModel
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    enum class ViewType {
        Grid, Linear, ViewPager, AddMore
    }
}