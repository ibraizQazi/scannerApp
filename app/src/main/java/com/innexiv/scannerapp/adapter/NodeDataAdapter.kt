package com.innexiv.scannerapp.adapter

import android.view.ViewGroup
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.EquipmentType
import com.innexiv.scannerapp.data.dataItem
import com.innexiv.scannerapp.extensions.inflate
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class NodeDataAdapter(groups: List<ExpandableGroup<*>>) : ExpandableRecyclerViewAdapter<EquipmentTypeViewHolder, NodeDataViewHolder>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): EquipmentTypeViewHolder =
            EquipmentTypeViewHolder(parent.inflate(R.layout.node_equipment_type_row))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): NodeDataViewHolder =
            NodeDataViewHolder(parent.inflate(R.layout.nodes_row))


    override fun onBindGroupViewHolder(holder: EquipmentTypeViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.setEquipmentLayerTitle(group)
    }

    override fun onBindChildViewHolder(holder: NodeDataViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val data: dataItem = (group as EquipmentType).items[childIndex]
        holder.setFields(data)
    }

}
