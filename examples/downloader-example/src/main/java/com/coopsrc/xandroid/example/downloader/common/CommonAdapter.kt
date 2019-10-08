package com.coopsrc.xandroid.example.downloader.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by tingkuo.
 * Date: 2018-07-30
 * Time: 14:08
 */
class CommonAdapter<T>(val resource: Int, private val dataSet: List<T>, private val bindData: (View, T) -> Unit) :
        RecyclerView.Adapter<CommonAdapter.ViewHolder<T>>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val itemView = LayoutInflater.from(parent.context).inflate(resource, parent, false)

        return ViewHolder(itemView, bindData)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bindData(dataSet[position])
    }

    class ViewHolder<T>(view: View, private val bindData: (View, T) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindData(data: T) {
            with(data) {
                bindData(itemView, data)
            }
        }
    }
}