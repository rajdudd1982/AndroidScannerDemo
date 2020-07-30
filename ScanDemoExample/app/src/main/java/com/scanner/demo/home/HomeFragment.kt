package com.scanner.demo.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scanner.demo.R
import com.scanner.demo.ui.base.BaseFragment
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment {

    @JvmOverloads
    constructor(contentLayoutId: Int=0) : super(contentLayoutId)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeLayout.adapter.viewType = FolderListAdapter.ViewType.Linear
        reloadListItems()
    }

    fun reloadListItems() {
        homeLayout.listItems()
    }
}