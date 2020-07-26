package com.scanner.demo.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scanner.demo.ui.base.PdfIconsLayout
import java.io.File
import java.io.Serializable

class SavedDocViewModel : ViewModel(), Serializable {

    var position: Int = -1
    lateinit var file: File
    var selected: Boolean = false
    var addMoreItem: Boolean = false
    @Transient
    var lastClickedItemType: MutableLiveData<ClickedItemType> = MutableLiveData()

    @Transient
    lateinit var itemClickAction: PdfIconsLayout.PdfIconListener

    fun getImagePath() : String =  when(file.isDirectory) {true -> file.listFiles()[0].path false ->    file.path }


    enum class ClickedItemType {
        None, Delete, CreatePdf, Share
    }

}