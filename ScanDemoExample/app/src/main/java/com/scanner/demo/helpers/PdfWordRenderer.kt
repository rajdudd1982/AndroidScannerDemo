package com.scanner.demo.helpers

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.annotation.RequiresApi

import java.io.File
import java.io.IOException

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class PdfWordRenderer(file: File) {

    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var parcelFileDescriptor: ParcelFileDescriptor
    private var currentPage: PdfRenderer.Page? = null
    var pageIndex = 0

    init {
        openRenderer(file)
    }

    @Throws(IOException::class)
    private fun openRenderer(file: File)  {
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        // This is the PdfRenderer we use to render the PDF.
        if (parcelFileDescriptor != null) {
            pdfRenderer = PdfRenderer(parcelFileDescriptor)
        }
    }

     fun closeRenderer() {
         try {
             closeCurrentPage()
             pdfRenderer.close()
             parcelFileDescriptor.close()
         } catch (e: Exception) {
            // FirebaseCrashlyticsManager.logException(e)
         }
    }

     fun showPage(index: Int)  : Bitmap? {
        if (pdfRenderer.pageCount <= index) {
            return null
        }
        // Make sure to close the current page before opening another one.
        closeCurrentPage()
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index)
         currentPage?.apply {
             // Important: the destination bitmap must be ARGB (not RGB).
             val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
             // Here, we render the page onto the Bitmap.
             // To render a portion of the page, use the second and third parameter. Pass nulls to get
             // the default result.
             // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
             render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
             // We are ready to show the Bitmap to user.
             return bitmap
         }
         return null

    }

    private fun closeCurrentPage() {
        if (null != currentPage) {
            currentPage?.close()
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun getPageCount(): Int {
        return pdfRenderer.pageCount
    }

}