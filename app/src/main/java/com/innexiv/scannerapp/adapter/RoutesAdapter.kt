package com.innexiv.scannerapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.RouteSite
import com.innexiv.scannerapp.data.RoutesResponse
import com.innexiv.scannerapp.extensions.inflate
import kotlinx.android.synthetic.main.routes_row.view.*




class RoutesAdapter(private val routeSiteList: List<RouteSite>, val listener: (RouteSite) -> Unit) : RecyclerView.Adapter<RoutesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(p0.inflate(R.layout.routes_row))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItems(routeSiteList[p1], listener)
    }

    override fun getItemCount(): Int =  routeSiteList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(routeSite: RouteSite,listener: (RouteSite) -> Unit) {
            with(itemView) {
                r_id.text = "Id: ${routeSite.id}"
                routeId.text = "Route Id: ${routeSite.routeId}"
                siteId.text = "Site Id: ${routeSite.siteId}"
                setOnClickListener{listener(routeSite)}
            }
        }


    }
}