package com.scanner.demo.camerax.views

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class ImageCaptureBottomLayout : LinearLayout {
    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)
    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

    }
}