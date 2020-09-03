package com.scanner.demo.helpers

import android.os.Environment
import java.io.File


object FileHelper {

    init {
        makeDir(File(FileConstant.imageCacheParentDir))
        makeDir(File(FileConstant.imageParentDir))
        makeDir(File(FileConstant.tempExternalParentDir))
    }

    object FileConstant {
        private const val scannedImagePath = "scanned_images"
        private const val tempScannedImagePath = "temp_scanned_images"
        private val parentDir = AndroidHelper.appContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageParentDir = "${parentDir}/${scannedImagePath}"
        val imageCacheParentDir = "${AndroidHelper.appContext().cacheDir.path}/${scannedImagePath}"
        val tempExternalParentDir = "${parentDir}/${tempScannedImagePath}"
        val tempExternalParentRelativePath = "${Environment.DIRECTORY_PICTURES}/${tempScannedImagePath}"
    }

    @JvmStatic
    fun createSharedCacheFile(fileName: String) : File {
        val file = File(FileConstant.imageCacheParentDir, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    @JvmStatic
    fun getCacheDirPath() : String {
        return FileConstant.imageCacheParentDir
    }
    @JvmStatic
    fun getExternalStorageTempRelativePath() : String {
        return FileConstant.tempExternalParentRelativePath
    }

    @JvmStatic
    fun createExternalStorageFile(dirPath: String, fileName: String) : File {
        var file = makeDir(File(FileConstant.imageCacheParentDir, dirPath))
        file = File(file, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    @JvmStatic
    fun createExternalStorageTempFile( fileName: String) : File {
        var file  = File(FileConstant.tempExternalParentDir, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    fun makeDir(file : File, delete: Boolean = false) : File {
        if (delete) file.delete()
        if (!file.exists()) file.mkdir()
        return file
    }

    fun clearCacheDir() {
        makeDir(File(FileConstant.imageCacheParentDir), true)
    }
}



//Note: Documents
//http://androidvogella.blogspot.com/2015/12/getfilesdir-vs-getexternalfilesdir-vs.html
//getFilesDir() returns /data/data/[packagename]/files while getExternalFilesDir()returns /Android/data/[packagename]/files
//https://stackoverflow.com/questions/10123812/difference-between-getexternalfilesdir-and-getexternalstoragedirectory
//https://developer.android.com/reference/android/content/Context
//https://data-flair.training/blogs/storage-in-android/
//https://www.grokkingandroid.com/how-to-correctly-store-app-specific-files-in-android/