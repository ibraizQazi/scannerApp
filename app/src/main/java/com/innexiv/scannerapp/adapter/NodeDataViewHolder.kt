package com.innexiv.scannerapp.adapter

import android.view.View
import com.innexiv.scannerapp.data.dataItem
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import kotlinx.android.synthetic.main.nodes_row.view.*

class NodeDataViewHolder(itemView: View) : ChildViewHolder(itemView) {

    fun setFields (dataObj: dataItem){
        with(itemView) {
            nodeName.text = "Name: ${dataObj.name}"
            nodeId.text = "Shortcode: ${dataObj.shortCode}"
            nodeType.text = "Type: ${dataObj.equipmentLayerName}"
        }
    }
}
