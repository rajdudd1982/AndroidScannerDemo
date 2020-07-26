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
        createDefaultFolder()
        var newFolderPath: String = getDefaultFolderPath()
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

    private fun createDefaultFolder() {
        var file =  File(getDefaultFolderPath())
        if (!file.exists()) {
            file.mkdir()
        }
    }

    private fun createImageFile(folderPath: String): File {
        var file =  File(folderPath)
        if (!file.exists()) {
            file.mkdir()
        }
        return File(folderPath, "doc_${Calendar.getInstance().timeInMillis}.jpg")
    }
}