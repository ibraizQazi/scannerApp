package com.innexiv.scannerapp.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.dataItem
import com.innexiv.scannerapp.extensions.inflate
import kotlinx.android.synthetic.main.nodes_row.view.*

class NodesAdapter(var siteList: List<dataItem>, val listener: (dataItem) -> Unit) : RecyclerView.Adapter<NodesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder = ViewHolder(p0.inflate(R.layout.nodes_row))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (siteList[position].isScanned) {
            //holder.itemView.nodesCard.setCardBackgroundColor(Color.GREEN)
            holder.itemView.setBackgroundColor(Color.GREEN)
        } else {
            //holder.itemView.nodesCard.setCardBackgroundColor(Color.RED)
            holder.itemView.setBackgroundColor(Color.MAGENTA)
        }
        holder.bindItems(siteList[position], listener)

    }

    override fun getItemCount(): Int = siteList.size

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
