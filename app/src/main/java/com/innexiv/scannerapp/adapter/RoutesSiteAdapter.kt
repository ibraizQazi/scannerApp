package com.innexiv.scannerapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.RouteObject
import com.innexiv.scannerapp.data.RoutesResponse
import com.innexiv.scannerapp.extensions.inflate
import kotlinx.android.synthetic.main.sites_row.view.*

class RoutesSiteAdapter (private var routesSiteList: List<RouteObject>, private val listener: (RouteObject) -> Unit) : RecyclerView.Adapter<RoutesSiteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RoutesSiteAdapter.ViewHolder {
        return ViewHolder(p0.inflate(R.layout.sites_row))
    }

    override fun getItemCount(): Int = routesSiteList.size

    override fun onBindViewHolder(p0: RoutesSiteAdapter.ViewHolder, p1: Int) {
        p0.bindItems(routesSiteList[p1], listener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(routeObject: RouteObject, listener: (RouteObject) -> Unit){
            with(itemView){
                siteName.text = "Site Name: ${routeObject.name}"
                siteId.text = "Site ID: ${routeObject.id}"
                setOnClickListener { listener(routeObject) }
            }
        }

    }
}
