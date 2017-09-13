package com.innexiv.scannerapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.innexiv.scannerapp.R
import com.innexiv.scannerapp.data.SiteResponse
import com.innexiv.scannerapp.extensions.inflate
import kotlinx.android.synthetic.main.sites_row.view.*

class SitesAdapter (var siteList: ArrayList<SiteResponse.SiteObject>,  val listener: (SiteResponse.SiteObject) -> Unit) : RecyclerView.Adapter<SitesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(p0.inflate(R.layout.sites_row))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItems(siteList[p1], listener)
    }

    override fun getItemCount(): Int = siteList.size

    fun addSites(updatedSiteList: List<SiteResponse.SiteObject>){
        siteList.addAll(updatedSiteList)
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(siteObject: SiteResponse.SiteObject, listener: (SiteResponse.SiteObject) -> Unit) {
            with(itemView) {
                siteName.text = "Site: ${siteObject.name}"
                siteId.text = "Site Id: ${siteObject.id}"
                setOnClickListener { listener(siteObject) }
            }
        }
    }
}
