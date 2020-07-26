package com.scanner.demo

import android.app.Application
import android.content.Context
import com.mikepenz.iconics.Iconics
import com.scanner.demo.helpers.IconsFont

class DocScannerApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        //only required if you add a custom or generic font on your own
        Iconics.init(applicationContext)

        //register custom fonts like this (or also provide a font definition file)
        Iconics.registerFont(IconsFont)
    }

}