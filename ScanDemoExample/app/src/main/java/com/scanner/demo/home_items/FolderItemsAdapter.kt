package com.scanner.demo.home_items

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import com.scanner.demo.R
import com.scanner.demo.home.SavedDocViewModel
import com.scanner.demo.ui.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.add_more_layout.view.*
import kotlinx.android.synthetic.main.home_list_item_layout.view.*
import kotlinx.android.synthetic.main.icon_wrapper_layout.view.*


class FolderItemsAdapter(context: Context) : BaseRecyclerAdapter<SavedDocViewModel>(context) {

    var viewType: ViewType = ViewType.Linear

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SavedDocViewModel> {
        if(viewType == ViewType.Grid.ordinal){
            return SavedGridDocViewHolder(context, layoutInflater.inflate(R.layout.doc_grid_item_layout, parent, false))
        }
        return AddMoreDocViewHolder(context, layoutInflater.inflate(R.layout.add_more_layout, parent, false))
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


    class AddMoreDocViewHolder(context: Context, view: View) : BaseViewHolder<SavedDocViewModel>(context, view) {
        override fun updateView(item: SavedDocViewModel, position: Int) {

            itemView.camera.setOnClickListener {
                item.lastClickedItemType = SavedDocViewModel.ClickedItemType.Camera
                item.itemClickListener.onItemClick(item)
            }

            itemView.gallery.setOnClickListener {
                item.lastClickedItemType = SavedDocViewModel.ClickedItemType.Gallery
                item.itemClickListener.onItemClick(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (itemList[position].addMoreItem) {
            return ViewType.AddMore.ordinal
        }
        return viewType.ordinal
    }

    enum class ViewType {
        Grid, Linear, ViewPager, AddMore
    }
}