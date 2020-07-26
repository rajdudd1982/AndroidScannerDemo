package com.scanner.demo.ui.base

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BaseRecyclerView @JvmOverloads constructor( context: Context, attrs: AttributeSet? =  null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    lateinit var touchHelper: ItemTouchHelper

    init {
        layoutManager = LinearLayoutManager(context)
    }

    /**
     * Apply drag drop and swipe functionality to recycler view
     */
    fun setTouchListener(dragDirs: Int =  (ItemTouchHelper.UP or ItemTouchHelper.DOWN), swipeDirs : Int = (ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)) {
        var touchCallBack = object: ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs){
            override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }
        touchHelper = ItemTouchHelper(touchCallBack)

        touchHelper.attachToRecyclerView(this)
    }
}