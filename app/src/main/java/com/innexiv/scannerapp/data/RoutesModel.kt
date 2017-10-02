package com.innexiv.scannerapp.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json


data class RoutesResponse(
        @Json (name = "result")
        val result: String,
        @Json(name = "freason")
        val freason: String,
        @Json(name = "route")
        var routesList: List<RouteObject>,
        @Json(name = "route_site")
        var routeSiteList: List<RouteSite>) : Parcelable {

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

data class RouteObject(
        @Json(name = "id")
        val id: Int = 0,
        @Json(name = "name")
        val name: String?,
        @Json(name = "route_date")
        val routeDate: String?,
        @Json(name = "end_date")
        val endDate: String?,
        @Json(name = "created_by_id")
        val createdById: Int = 0,
        @Json(name = "team_id")
        val teamId: Int =0,
        @Json(name = "total_total_days")
        val totalDays: String?,
        @Json(name = "total_site_distance")
        val totalSiteDistance: String?,
        @Json(name = "total_site_time")
        val siteTime: String?,
        @Json(name = "total_fuel_cost")
        val fuelCost: String?,
        @Json(name = "total_daily_allownce")
        val dailyAllowance: String?,
        @Json(name = "total_toll_cost")
        val tollCost: String?,
        @Json(name = "total_site_count")
        val siteCount: Int=0,
        @Json(name = "route_status_id")
        val statusId: Int=0,
        @Json(name = "teamlead_id")
        val teamLeadId: Int=0,
        @Json(name = "created_on")
        val createdOn: String?,
        @Json(name = "updated_on")
        val updatedOn: String?,
        @Json(name = "updated_by_id")
        val updatedById: Int=0) : Parcelable {

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
        @Json(name = "id")
        val id: Int=0,
        @Json(name = "route_id")
        val routeId: Int=0,
        @Json(name = "site_id")
        val siteId: Int=0,
        @Json(name = "project_id")
        val projectId: String?,
        @Json(name = "activity_id")
        val activityId: Int=0,
        @Json(name = "route_site_distance")
        val routeSiteDistance: String?,
        @Json(name = "route_site_time")
        val routeSiteTime: String?,
        @Json(name = "fuel_cost")
        val fuelCost: String?,
        @Json(name = "daily_allownce")
        val dailyAllowance: String?,
        @Json(name = "toll_cost")
        val tollCost: String?,
        @Json(name = "latitude")
        val latitude: String?,
        @Json(name = "longitude")
        val longitude: String?,
        @Json(name = "SiteShortCode")
        val siteShortCode: String?,
        @Json(name = "visit_date")
        val visitDate: String?,
        @Json(name = "visit_order")
        val visitOrder: Int=0) : Parcelable {

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
