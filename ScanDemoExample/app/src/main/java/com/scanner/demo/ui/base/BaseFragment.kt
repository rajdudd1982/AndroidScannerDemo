package com.scanner.demo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment {

    @JvmOverloads
    constructor(contentLayoutId: Int=0) : super(contentLayoutId)

}