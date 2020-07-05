package com.scanner.demo

import android.annotation.TargetApi
import android.content.Context

import android.util.AttributeSet
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.view.*

open class  HomeContentLayout : RelativeLayout {
    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int=0) : super(context, attrs, defStyleAttr)
    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

    }
}