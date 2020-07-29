package com.scanner.demo.home

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.scanner.demo.helpers.Image
import com.scanner.demo.listeners.ItemClickListener
import kotlinx.android.parcel.Parcelize
import java.io.File
import java.io.Serializable

@Parcelize
data class SavedDocViewModel(val image: Image?) : ViewModel(), Parcelable {

    var position: Int = -1
    lateinit var file: File
    var selected: Boolean = false
    var addMoreItem: Boolean = false

    @Transient
    var lastClickedItemType: ClickedItemType = ClickedItemType.None


    @Transient
    lateinit var  itemClickListener:  ItemClickListener<SavedDocViewModel>

    fun getImagePath() : String =  when(file.isDirectory && file.listFiles().isNotEmpty()) {true -> file.listFiles()[0].path false ->    file.path }

    enum class ClickedItemType {
        None, Delete, CreatePdf, Share, Camera, Gallery
    }

}