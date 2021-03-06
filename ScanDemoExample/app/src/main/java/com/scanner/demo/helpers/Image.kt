package com.scanner.demo.helpers

import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.parcel.Parcelize
import java.io.FileDescriptor
import java.io.IOException

@Parcelize
data class Image(val uri: Uri,
                 val name: String,
                 val duration: Int,
                 val dateModified: Long,
                 val bucketDisplayName: String,
                 val path: String?) : Parcelable {



    companion object {
        fun append(arr: Array<String>, element: String): Array<String> {
            val list: MutableList<String> = arr.toMutableList()
            list.add(element)
            return list.toTypedArray()
        }


        fun getSavedMediaFiles(hasSubFolder: Boolean, folderPath: String?) : MutableList<Image> {
            val imageList = mutableListOf<Image>()
            val projection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.RELATIVE_PATH
                )

            } else {
                arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.DATA
                )

            }

            var selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { "${MediaStore.Images.Media.RELATIVE_PATH}  like ?"} else "${MediaStore.Images.Media.DATA}  like ?"

            if (hasSubFolder) {
                selection += " and " +  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { "${MediaStore.Images.Media.RELATIVE_PATH}  NOT like ?"} else "${MediaStore.Images.Media.DATA}  NOT like ?"
            }
            val selectionArgs = if(hasSubFolder) arrayOf("%${folderPath}%", "%@@%") else arrayOf("%${folderPath}%")

            val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

            val query = AndroidHelper.appContext().contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            )

            query?.use { cursor ->
                // Cache column indices.
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                val dateModifiedId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val bucketNameId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val pathId =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cursor.getColumnIndexOrThrow( MediaStore.Images.Media.RELATIVE_PATH)
                } else {
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                }

                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val dateModified = cursor.getLong(dateModifiedId)
                    val size = cursor.getInt(sizeColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                    )

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    imageList += Image(contentUri, name, 0, dateModified, cursor.getString(bucketNameId),  cursor.getString(pathId))
                    Log.d("Demo", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)))
                }
            }
            return imageList
        }

        @Throws(IOException::class)
        fun getBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
            val parcelFileDescriptor: ParcelFileDescriptor? =
                    contentResolver.openFileDescriptor(uri, "r")
            parcelFileDescriptor?.apply {
                val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
                val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                parcelFileDescriptor.close()
                return image
            }
            return null

        }
    }


    fun deleteFile(hasSubFolder: Boolean) {
        var selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { "${MediaStore.Images.Media.RELATIVE_PATH}  like ?"} else "${MediaStore.Images.Media.DATA}  like ?"
        val selectionArgs = arrayOf("%${path}%")

        val itemCount = AndroidHelper.appContext().contentResolver.delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                selection,
                selectionArgs)
        Log.d("Items deleted count", itemCount?.toString())
    }
}