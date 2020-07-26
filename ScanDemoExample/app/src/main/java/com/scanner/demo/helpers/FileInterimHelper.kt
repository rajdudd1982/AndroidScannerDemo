package com.scanner.demo.helpers

import com.scanlibrary.helpers.FileHelper
import com.scanner.demo.home.SavedDocViewModel
import com.scanner.demo.ui.base.PdfIconsLayout
import java.io.File

object FileInterimHelper {

    fun listItems(filePath: String = FileHelper.getDefaultFolderPath(), pdfIconListener: PdfIconsLayout.PdfIconListener) : ArrayList<SavedDocViewModel>? {
        var savedDocViewModel: SavedDocViewModel
        var docViewModelList = ArrayList<SavedDocViewModel>()
        if (!File(filePath).isDirectory) return null

        for (file in  File(filePath).listFiles()) {
            savedDocViewModel = SavedDocViewModel()
            savedDocViewModel.file = file
            savedDocViewModel.itemClickAction = pdfIconListener
            docViewModelList.add(savedDocViewModel)
        }
        return docViewModelList
    }
}