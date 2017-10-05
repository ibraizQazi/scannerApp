package com.innexiv.scannerapp.adapter

import android.widget.TextView
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.dataItem
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.expand.ChildPosition
import com.mindorks.placeholderview.annotations.expand.ParentPosition
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug


@Layout(R.layout.nodes_row)
class NodeView ( private val nodeItem: dataItem){

    @ParentPosition
    private var mParentPosition: Int = 0

    @ChildPosition
    private var mChildPosition: Int = 0

    @View(R.id.nodeName)
    private var nodeName: TextView? = null

    @View(R.id.nodeId)
    private var nodeId: TextView? = null

    @View(R.id.nodeType)
    private var nodeType: TextView? = null

    @View(R.id.scanStatus)
    private var scanStatus: TextView? = null

    @Resolve
    private fun onResolved() {

        nodeName?.text = nodeItem.name
        nodeId?.text = nodeItem.equipmentId.toString()
        nodeType?.text = nodeItem.equipmentLayerName
        scanStatus?.text = nodeItem.isScanned.toString()

    }

}
