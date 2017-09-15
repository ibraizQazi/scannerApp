package com.innexiv.scannerapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.SiteObject
import com.innexiv.scannerapp.data.dataItem
import com.innexiv.scannerapp.extensions.inflate
import kotlinx.android.synthetic.main.nodes_row.view.*

class NodesAdapter(var siteList: List<dataItem>, val listener: (dataItem) -> Unit) : RecyclerView.Adapter<NodesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(p0.inflate(R.layout.nodes_row))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItems(siteList[p1], listener)
    }

    override fun getItemCount(): Int = siteList.size

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(dataObj: dataItem, listener: (dataItem) -> Unit) {
            with(itemView) {
                nodeName.text = "Name: ${dataObj.name}"
                nodeId.text = "Shortcode: ${dataObj.shortCode}"
                nodeType.text = "Type: ${dataObj.equipmentLayerName}"
                setOnClickListener { listener(dataObj) }
            }
        }
    }
}
