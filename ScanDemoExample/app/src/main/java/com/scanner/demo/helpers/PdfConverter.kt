package com.scanner.demo.helpers

import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object PdfConverter {

    @JvmStatic
    @Throws(IOException::class)
    fun createPdf(fileList: List<File>, dest: String?) {

        var document = PdfDocument();

        // crate a page description
        var pageInfo = PdfDocument.PageInfo.Builder(100, 100, 1).create();

        // start a page
        var page = document.startPage(pageInfo);


        val matrix = Matrix()
        matrix.setScale(500F, 500F)

        // draw something on the page
        for(file in fileList) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            page.canvas.drawBitmap(bitmap, 0F,0F, Paint())
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
}