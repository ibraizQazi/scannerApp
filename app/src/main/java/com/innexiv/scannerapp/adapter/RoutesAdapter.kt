package com.innexiv.scannerapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.RoutesResponse
import com.innexiv.scannerapp.extensions.inflate
import kotlinx.android.synthetic.main.routes_row.view.*

class RoutesAdapter(var routesList: ArrayList<RoutesResponse.RouteSite>) : RecyclerView.Adapter<RoutesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        //val view = LayoutInflater.from(p0.context).inflate(R.layout.routes_row, p0, false)
        //return ViewHolder(view)
        return ViewHolder(p0.inflate(R.layout.routes_row))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItems(routesList[p1])
    }

    override fun getItemCount(): Int =  routesList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(routeSite: RoutesResponse.RouteSite) {
            itemView.r_id.text = routeSite.id
            itemView.routeId.text = routeSite.routeId
            itemView.siteId.text = routeSite.siteId
        }
    }
}