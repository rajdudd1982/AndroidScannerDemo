package com.scanner.demo.camerax.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scanner.demo.camerax.CachedFileViewModel
import com.scanner.demo.camerax.CachedFilesAdapter
import com.scanner.demo.helpers.CameraXHelper
import com.scanner.demo.helpers.FileHelper
import com.scanner.demo.listeners.ItemClickListener
import com.scanner.demo.ui.base.BaseRecyclerAdapter
import com.scanner.demo.ui.base.SpacesItemDecoration
import kotlinx.android.synthetic.main.camera_layout.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ImageCaptureALayout  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr){

    private lateinit var cameraXHelper: CameraXHelper
    private lateinit var activity: AppCompatActivity
    private lateinit var file: File
    var cachedViewModels: ArrayList<CachedFileViewModel> = ArrayList()

    override fun onFinishInflate() {
        super.onFinishInflate()
        (capturedImagesRecyclerView.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        capturedImagesRecyclerView.adapter = CachedFilesAdapter(context)
        capturedImagesRecyclerView.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.HORIZONTAL, 16))

        //Deleting file for uri and also delete file
        (capturedImagesRecyclerView.adapter as CachedFilesAdapter).itemClickListener = itemClickListener
    }

    fun setData(activity: AppCompatActivity){
        this.activity = activity

        cameraXHelper = CameraXHelper()
        cameraXHelper.startCamera(activity, (cameraPreview as PreviewView))
    }

    fun onImageCaptureClick() {
        this.file = FileHelper.createSharedCacheFile("doc_${Calendar.getInstance().timeInMillis}.png")
        cameraXHelper.takePicture(file)
        cameraXHelper.savedImageUriLiveData.observe(activity, Observer {

            var cachedFileViewModel = CachedFileViewModel()
            cachedFileViewModel.fileUri = this.file.toUri()
            cachedViewModels.add(cachedFileViewModel)
            showCaptureImages()
        })
    }

    private fun showCaptureImages() {
        (capturedImagesRecyclerView.adapter as BaseRecyclerAdapter<CachedFileViewModel>).itemList = cachedViewModels
        (capturedImagesRecyclerView.adapter as BaseRecyclerAdapter<CachedFileViewModel>).notifyDataSetChanged()
    }

    // When delete icon is clicked then remove item for local cache also delete file.
    private val itemClickListener: ItemClickListener<CachedFileViewModel> = object : ItemClickListener<CachedFileViewModel> {
        override fun onItemClick(item: CachedFileViewModel) {
            if (item.actionPerformed == CachedFileViewModel.LastAction.DELETE){
                cachedViewModels.remove(item)
                (capturedImagesRecyclerView.adapter as CachedFilesAdapter).notifyDataSetChanged()
                if (item.localCache){
                    File(item.fileUri.toString()).delete()
                }
            }
        }
    }

    fun addUris(cachedItems: ArrayList<CachedFileViewModel>) {
        cachedViewModels.addAll(cachedItems)
        showCaptureImages()
    }

}