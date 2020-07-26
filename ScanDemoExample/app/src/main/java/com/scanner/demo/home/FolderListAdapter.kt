package com.scanner.demo.home

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.scanner.demo.R
import com.scanner.demo.ui.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.home_list_item_layout.view.*
import kotlinx.android.synthetic.main.icon_wrapper_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


class FolderListAdapter(context: Context) : BaseRecyclerAdapter<SavedDocViewModel>(context) {

    var viewType: ViewType = ViewType.Linear

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SavedDocViewModel> {

         if (viewType == ViewType.Linear.ordinal) {
             return SavedDocViewHolder(context, layoutInflater.inflate(R.layout.home_list_item_layout, parent, false))
         } else if(viewType == ViewType.Grid.ordinal) {
             return SavedGridDocViewHolder(context, layoutInflater.inflate(R.layout.doc_grid_item_layout, parent, false))
         }
        return SavedDocViewHolder(context, layoutInflater.inflate(R.layout.home_list_item_layout, parent, false))
    }

    class SavedDocViewHolder(context: Context, view: View) : BaseViewHolder<SavedDocViewModel>(context, view) {

        override fun updateView(item: SavedDocViewModel, position: Int) {

            val bitmap = BitmapFactory.decodeFile(item.getImagePath())
            itemView.imageView.setImageBitmap(bitmap)
            itemView.fileName.text = item.file.nameWithoutExtension

            var cal = Calendar.getInstance()
            cal.timeInMillis = item.file.lastModified()

            itemView.creationTime.text = SimpleDateFormat("dd-MMM-yyyy HH:mm").format(cal.time)

            itemView.setOnClickListener {
                var action = HomeFragmentDirections.docItemClicked(item)
                Navigation.findNavController(it).navigate(action)

            }

            setIconLayout(item, position)
        }

        private fun setIconLayout(savedDocViewModel: SavedDocViewModel, position: Int) {
            savedDocViewModel.position = position
            itemView.pdfIconsLayout.savedDocViewModel = savedDocViewModel
        }
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
        Grid, Linear, ViewPager
    }
}