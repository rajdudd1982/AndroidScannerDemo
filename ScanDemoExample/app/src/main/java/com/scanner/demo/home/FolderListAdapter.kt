package com.scanner.demo.home

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.scanner.demo.R
import com.scanner.demo.helpers.Image
import com.scanner.demo.ui.base.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.home_list_item_layout.view.*
import kotlinx.android.synthetic.main.icon_wrapper_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


class FolderListAdapter(context: Context) : BaseRecyclerAdapter<SavedDocViewModel>(context) {

    var viewType: ViewType = ViewType.Linear

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SavedDocViewModel> {
        return SavedDocViewHolder(context, layoutInflater.inflate(R.layout.home_list_item_layout, parent, false))
    }

    class SavedDocViewHolder(context: Context, view: View) : BaseViewHolder<SavedDocViewModel>(context, view) {

        override fun updateView(item: SavedDocViewModel, position: Int) {

            var cal = Calendar.getInstance()
            item.image?.apply {
                val bitmap = Image.getBitmapFromUri(context.contentResolver, this.uri)//BitmapFactory.decodeFile(item.getImagePath())
                itemView.imageView.setImageBitmap(bitmap)

                cal.timeInMillis = item.image.dateModified.toLong() *1000

            }

            itemView.fileName.text = item.image?.name

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

    enum class ViewType {
        Grid, Linear, ViewPager
    }
}