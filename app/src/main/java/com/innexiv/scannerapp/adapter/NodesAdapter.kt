package com.innexiv.scannerapp.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.commons.ViewType
import com.innexiv.scannerapp.data.DataItem
import com.innexiv.scannerapp.data.DataListItem
import com.innexiv.scannerapp.data.EquipmentItem
import com.innexiv.scannerapp.extensions.inflate
import kotlinx.android.synthetic.main.node_equipment_type_row.view.*
import kotlinx.android.synthetic.main.nodes_row.view.*

class NodesAdapter( val items: List<ViewType>, val listener: (DataItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when(viewType){

            ViewType.TYPE_EQUIPMENT -> {
                val equipment = items[position] as EquipmentItem
                val equipmentViewHolder = holder as EquipmentViewHolder
                equipmentViewHolder.bindItems(equipment.equipmentItem.name)
            }
            ViewType.TYPE_NODE -> {
                val node = items[position] as DataListItem
                val nodeViewHolder = holder as NodeViewHolder
                if (node.dataItem.isScanned) {
                    nodeViewHolder.itemView.setBackgroundColor(Color.parseColor("#FF4CAF50"))
                    nodeViewHolder.itemView.scanStatus.text = "Re-scan"
                }
                else
                    nodeViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFEF5350"))

                nodeViewHolder.bindItems(node.dataItem, listener)
            }

            else -> throw IllegalStateException("unsupported item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.TYPE_EQUIPMENT ->
                EquipmentViewHolder(parent.inflate(R.layout.node_equipment_type_row))

            ViewType.TYPE_NODE ->
                NodeViewHolder(parent.inflate(R.layout.nodes_row))

            else -> throw IllegalStateException("unsupported item type")
        }
    }


    override fun getItemViewType(position: Int): Int = items[position].getViewType()

    override fun getItemCount(): Int = items.size

    inner class NodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(dataObj: DataItem, listener: (DataItem) -> Unit) {
            with(itemView) {
                nodeName.text = "Name: ${dataObj.name}"
                nodeId.text = "ShortCode: ${dataObj.shortCode}"
                setOnClickListener { listener(dataObj) }
            }
        }
    }

    inner class EquipmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(equipmentTitle: String) {
            with(itemView) {
                equipmentLayerTitle.text = equipmentTitle
            }
        }
    }
}
