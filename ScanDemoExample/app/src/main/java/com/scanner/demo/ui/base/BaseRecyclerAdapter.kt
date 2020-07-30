package com.scanner.demo.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open abstract  class BaseRecyclerAdapter<T>() : RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder<T>>() {

    protected var itemList : ArrayList<T> = ArrayList()
    protected lateinit var context: Context
    protected lateinit var layoutInflater: LayoutInflater

    constructor(context: Context) : this() {
        this.context = context
        this.layoutInflater = LayoutInflater.from(context)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    fun addAllItems(itemList : List<T> ){
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    fun setItemList(itemList : List<T> ){
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    fun addItem(item : T, position: Int = itemCount){
        this.itemList.add(item)
        notifyItemInserted(position)
    }

    fun resetItems(itemList : ArrayList<T>){
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        this.itemList.removeAt(position)
        //Since position of item should be updated
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.updateView(getItemByPosition(position) , position)
    }


    private fun getItemByPosition(position: Int) : T {
        return itemList[position]
    }


    open abstract class BaseViewHolder<T>(context: Context, view: View) : RecyclerView.ViewHolder(view) {
        protected var context: Context = context
        abstract fun updateView(item: T, position: Int)
    }
}