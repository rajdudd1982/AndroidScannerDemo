package com.scanlibrary.helpers

import com.scanlibrary.ScanConstants
import java.io.File
import java.util.*

object FileHelper {

    fun getDefaultFolderPath() : String =  ScanConstants.IMAGE_PATH

    /**
     * If folder path is default, it means we are creating new folder and adding image to it.
     * otherwise we are adding image to existing path
     */
    @JvmStatic
    fun createImage(folderPath: String = getDefaultFolderPath()) : File? {
        // creating default folder
        createFolder(getDefaultFolderPath())
        var newFolderPath: String = folderPath
        if (getDefaultFolderPath().equals(folderPath, true)) {
            newFolderPath = "${getDefaultFolderPath()}/ScannedDoc_${Calendar.getInstance().timeInMillis}"

        }
        return return createImageFile(newFolderPath)
    }



    private fun createNewFolder(folderName: String = "ScannedDoc_${Calendar.getInstance().timeInMillis}") : String? {
        var file =  File(getDefaultFolderPath(), folderName)
        if (!file.exists()) {
            file.mkdir()
        } else {
           // AndroidHelper.showToast(AndroidHelper.appContext().getString(R.string.folder_error_message))
            return null
        }
        return file.path
    }


    fun createFolder(folderPath: String) {
        var file =  File(folderPath)
        if (!file.exists()) {
            file.mkdir()
        }
    }

    private fun createImageFile(folderPath: String): File {
        createFolder(folderPath)
        return File(folderPath, "doc_${Calendar.getInstance().timeInMillis}.jpg")
    }
}