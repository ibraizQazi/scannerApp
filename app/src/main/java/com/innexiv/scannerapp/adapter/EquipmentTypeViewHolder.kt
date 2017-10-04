package com.innexiv.scannerapp.adapter

import android.view.View
import com.innexiv.scannerapp.data.dataItem
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.node_equipment_type_row.view.*
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation

class EquipmentTypeViewHolder(itemView: View) : GroupViewHolder (itemView) {

    fun setEquipmentLayerTitle (equipmentType: ExpandableGroup<*>){
        itemView.equipmentLayerTitle.text = equipmentType.title
    }
    override fun expand() {
        super.expand()
        animateExpand()
    }

    override fun collapse() {
        super.collapse()
        animateCollapse()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        itemView.list_item_genre_arrow.animation = rotate
    }
    private fun animateCollapse() {
        val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        itemView.list_item_genre_arrow.animation = rotate
    }
}