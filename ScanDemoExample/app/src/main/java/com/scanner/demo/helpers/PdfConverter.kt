package com.scanner.demo.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.provider.MediaStore
import androidx.core.net.toUri
import com.scanner.demo.home.SavedDocViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object PdfConverter {

    @JvmStatic
    @Throws(IOException::class)
    fun createPdf(fileList: List<File>, dest: String?) {

        var document = PdfDocument();

        // crate a page description
        var pageInfo = PdfDocument.PageInfo.Builder(3000, 8000, 1).create();

        // start a page
        var page = document.startPage(pageInfo);


        val matrix = Matrix()
        matrix.setScale(500F, 500F)

        // draw something on the page
        var top = 0F
        for(file in fileList) {
            val bitmap = BitmapFactory.decodeFile(file.path)
            page.canvas.drawBitmap(bitmap, 0F, top, Paint())
            top += 2000F
        }

        // finish the page
        document.finishPage(page);

        // write the document content
        try {
            document.writeTo(FileOutputStream(dest))
        } catch (e: IOException) {
            //callback.onWriteFailed(e.toString())
            return
        } finally {
            document.close()
        }
        //callback.onWriteFinished(pages);
    }

    @JvmStatic
    @Throws(IOException::class)
    fun createPdfFromBitmap(imageList: ArrayList<SavedDocViewModel>, dest: String?) {

        var document = PdfDocument();

        // crate a page description
        var pageInfo = PdfDocument.PageInfo.Builder(1000, 1000, 1).create();

        // start a page
        var page = document.startPage(pageInfo);


        val matrix = Matrix()
        matrix.setScale(500F, 500F)

        var bitmap: Bitmap
        for (viewModel in imageList) {
            bitmap = MediaStore.Images.Media.getBitmap(AndroidHelper.appContext().contentResolver, viewModel?.image?.uri);
            page.canvas.drawBitmap(bitmap, 0F,0F, Paint())
            bitmap.recycle()
        }

        // draw something on the page


        // finish the page
        document.finishPage(page);

        // write the document content
        try {
            document.writeTo(FileOutputStream(dest))
        } catch (e: IOException) {
            //callback.onWriteFailed(e.toString())
            return
        } finally {
            document.close()
        }
        //callback.onWriteFinished(pages);
    }
}