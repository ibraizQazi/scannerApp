package com.innexiv.scannerapp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class RoutesResponse(@SerializedName("result") val result: String,
                          @SerializedName("freason") val freason: String,
                          @SerializedName("route") var routesList: MutableList<RouteObject>,
                          @SerializedName("route_site") var routeSiteList: MutableList<RouteSite>) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<RoutesResponse> = object : Parcelable.Creator<RoutesResponse>{
            override fun createFromParcel(p0: Parcel): RoutesResponse = RoutesResponse(p0)
            override fun newArray(p0: Int): Array<RoutesResponse?> = arrayOfNulls(p0)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(),
            source.createTypedArrayList(RouteObject.CREATOR), source.createTypedArrayList(RouteSite.CREATOR))

    override fun describeContents() = 0

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.let {
            with(it){
                writeString(result)
                writeString(freason)
                writeTypedList(routesList)
                writeTypedList(routeSiteList)
            }
        }
    }
}

data class RouteObject(@SerializedName("id") val id: Int = 0,
                       @SerializedName("name") val name: String?,
                       @SerializedName("route_date") val routeDate: String?,
                       @SerializedName("end_date") val endDate: String?,
                       @SerializedName("created_by_id") val createdById: Int = 0,
                       @SerializedName("team_id") val teamId: Int =0,
                       @SerializedName("total_total_days") val totalDays: String?,
                       @SerializedName("total_site_distance") val totalSiteDistance: String?,
                       @SerializedName("total_site_time") val siteTime: String?,
                       @SerializedName("total_fuel_cost") val fuelCost: String?,
                       @SerializedName("total_daily_allownce") val dailyAllowance: String?,
                       @SerializedName("total_toll_cost") val tollCost: String?,
                       @SerializedName("total_site_count") val siteCount: Int=0,
                       @SerializedName("route_status_id") val statusId: Int=0,
                       @SerializedName("teamlead_id") val teamLeadId: Int=0,
                       @SerializedName("created_on") val createdOn: String?,
                       @SerializedName("updated_on") val updatedOn: String?,
                       @SerializedName("updated_by_id") val updatedById: Int=0) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<RouteObject> = object : Parcelable.Creator<RouteObject> {
            override fun createFromParcel(p0: Parcel): RouteObject = RouteObject(p0)
            override fun newArray(p0: Int): Array<RouteObject?> = arrayOfNulls(p0)
        }
    }

    constructor(source: Parcel) : this (source.readInt(), source.readString(), source.readString(), source.readString(), source.readInt(),
                                source.readInt(), source.readString(), source.readString(), source.readString(), source.readString(),
                                source.readString(), source.readString(), source.readInt(), source.readInt(),source.readInt(),source.readString(),
                                source.readString(), source.readInt())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let{
            with(dest) {
                writeInt(id)
                writeString(name)
                writeString(routeDate)
                writeString(endDate)
                writeInt(createdById)
                writeInt(teamId)
                writeString(totalDays)
                writeString(totalSiteDistance)
                writeString(siteTime)
                writeString(fuelCost)
                writeString(dailyAllowance)
                writeString(tollCost)
                writeInt(siteCount)
                writeInt(statusId)
                writeInt(teamLeadId)
                writeString(createdOn)
                writeString(updatedOn)
                writeInt(updatedById)
            }
        }
    }
}

data class RouteSite(
        @SerializedName("id") val id: Int=0,
        @SerializedName("route_id") val routeId: Int=0,
        @SerializedName("site_id") val siteId: Int=0,
        @SerializedName("project_id") val projectId: String?,
        @SerializedName("activity_id") val activityId: Int=0,
        @SerializedName("route_site_distance") val routeSiteDistance: String?,
        @SerializedName("route_site_time") val routeSiteTime: String?,
        @SerializedName("fuel_cost") val fuelCost: String?,
        @SerializedName("daily_allownce") val dailyAllowance: String?,
        @SerializedName("toll_cost") val tollCost: String?,
        @SerializedName("latitude") val latitude: String?,
        @SerializedName("longitude") val longitude: String?,
        @SerializedName("SiteShortCode") val siteShortCode: String?,
        @SerializedName("visit_date") val visitDate: String?,
        @SerializedName("visit_order") val visitOrder: Int=0) : Parcelable {

    companion object {
        @JvmField val CREATOR : Parcelable.Creator<RouteSite> = object : Parcelable.Creator<RouteSite> {
            override fun createFromParcel(p0: Parcel): RouteSite = RouteSite(p0)
            override fun newArray(p0: Int): Array<RouteSite?> = arrayOfNulls(p0)
        }
    }

    constructor(source: Parcel) : this(source.readInt(), source.readInt(), source.readInt(), source.readString(), source.readInt(),
            source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(),
            source.readString(), source.readString(), source.readString(), source.readInt())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.let{
            with(dest) {
                writeInt(id)
                writeInt(routeId)
                writeInt(siteId)
                writeString(projectId)
                writeInt(activityId)
                writeString(routeSiteDistance)
                writeString(routeSiteTime)
                writeString(fuelCost)
                writeString(dailyAllowance)
                writeString(tollCost)
                writeString(latitude)
                writeString(longitude)
                writeString(siteShortCode)
                writeString(visitDate)
                writeInt(visitOrder)
            }
        }
    }
}
