package com.scanner.demo.helpers

import com.scanlibrary.helpers.FileHelper
import com.scanner.demo.home.SavedDocViewModel
import com.scanner.demo.listeners.ItemClickListener
import java.io.File

object FileInterimHelper {

    fun listItems(filePath: String = FileHelper.getDefaultFolderPath(), itemClickListener: ItemClickListener<SavedDocViewModel>) : ArrayList<SavedDocViewModel>? {
        var savedDocViewModel: SavedDocViewModel
        var docViewModelList = ArrayList<SavedDocViewModel>()
        if (!File(filePath).isDirectory) return null

//        for (file in  File(filePath).listFiles()) {
//            savedDocViewModel = SavedDocViewModel(file)
//            savedDocViewModel.file = file
//            savedDocViewModel.itemClickListener = itemClickListener
//            docViewModelList.add(savedDocViewModel)
//        }
        return docViewModelList
    }


    fun getImagesInFolder(folderPath: String?, itemClickListener: ItemClickListener<SavedDocViewModel>) : ArrayList<SavedDocViewModel>? {
        var savedDocViewModel: SavedDocViewModel
        var docViewModelList = ArrayList<SavedDocViewModel>()
        //if (!File(folderPath).isDirectory) return null


        for (file in  Image.getSavedMediaFiles(folderPath)) {
            savedDocViewModel = SavedDocViewModel(file)
            savedDocViewModel.itemClickListener = itemClickListener
            docViewModelList.add(savedDocViewModel)
        }
        return docViewModelList
    }



}