package com.scanner.demo.ui.base

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment {

    @JvmOverloads
    constructor(contentLayoutId: Int=0) : super(contentLayoutId)

}