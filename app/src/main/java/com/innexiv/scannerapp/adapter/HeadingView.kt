package com.innexiv.scannerapp.adapter

import android.content.Context
import com.innexiv.scannerapp.R
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.Resolve
import android.widget.TextView
import com.mindorks.placeholderview.annotations.expand.*


@Parent
@SingleTop
@Layout(R.layout.node_equipment_type_row)
class HeadingView(private val mHeading: String)  {

    @View(R.id.equipmentLayerTitle)
    private var headingTxt: TextView? = null

    @ParentPosition
    private var mParentPosition: Int = 0

    @Resolve
    private fun onResolved() {
        headingTxt!!.text = mHeading
    }

    @Expand
    private fun onExpand() {
    }

    @Collapse
    private fun onCollapse() {
    }
}