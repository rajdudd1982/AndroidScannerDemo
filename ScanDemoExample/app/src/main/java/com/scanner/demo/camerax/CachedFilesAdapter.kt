package com.scanner.demo.camerax

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.scanner.demo.R
import com.scanner.demo.helpers.Image
import com.scanner.demo.home.SavedDocViewModel
import com.scanner.demo.listeners.ItemClickListener
import com.scanner.demo.ui.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.captured_images_layout.view.*
import kotlinx.android.synthetic.main.icon_wrapper_layout.view.*

class CachedFilesAdapter (context: Context) : BaseRecyclerAdapter<CachedFileViewModel>(context) {

    var viewType: ViewType = ViewType.Linear
    lateinit var itemClickListener: ItemClickListener<CachedFileViewModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CachedFileViewModel> {
        return SavedDocViewHolder(context, layoutInflater.inflate(R.layout.captured_images_layout, parent, false), itemClickListener)
    }



    class SavedDocViewHolder(context: Context, view: View, val itemClickListener: ItemClickListener<CachedFileViewModel>) : BaseViewHolder<CachedFileViewModel>(context, view) {

        override fun updateView(item: CachedFileViewModel, position: Int) {

            item.fileUri?.let {
                val bitmap = Image.getBitmapFromUri(context.contentResolver, it)
                itemView.capturedImage.setImageBitmap(bitmap)
            }

            itemView.deleteCapturedImage.setOnClickListener {
                item.actionPerformed = CachedFileViewModel.LastAction.DELETE
                itemClickListener.onItemClick(item)
            }

            //setIconLayout(item, position)
        }

        private fun setIconLayout(savedDocViewModel: SavedDocViewModel, position: Int) {
            savedDocViewModel.position = position
            itemView.pdfIconsLayout.savedDocViewModel = savedDocViewModel
        }
    }

    enum class ViewType {
        Grid, Linear, ViewPager
    }
}