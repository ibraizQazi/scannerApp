package com.innexiv.scannerapp.data

import com.google.gson.annotations.SerializedName

data class BarcodePost(val barcode: String)
data class BarcodeResponse (@SerializedName("r") val r: String, @SerializedName("barcode") val barcode: String)
data class LoginPostBody(var email: String , var password: String, var imei: String)

data class LoginResponse (@SerializedName("status") val status: String, @SerializedName("token") val token: String)

//routes models
data class RoutesResponse(val result: String,
                          val freason: String,
                          val routesList: ArrayList<RouteObject>,
                          val routeSiteList: ArrayList<RouteSite>){

    inner class RouteObject(val id: String, val name: String)

    inner class RouteSite(val id: String, val routeId: String, val siteId: String, val latitude: String,val longitude: String)

}

//site models
data class SiteResponse(@SerializedName("result") val result: String,
                        @SerializedName("freason") val freason: String,
                        @SerializedName("site") val siteList: ArrayList<SiteObject>) {

    inner class SiteObject(@SerializedName("name") val name: String,
                           @SerializedName("short_name") val shortName: String,
                           @SerializedName("id") val id: Int,
                           @SerializedName("country_id") val countryId: Int,
                           @SerializedName("region_id") val regionId: Int,
                           @SerializedName("province_id") val provinceId: Int,
                           @SerializedName("city_id") val cityId: Int,
                           @SerializedName("latitude") val latitude: String,
                           @SerializedName("longitude") val longitude: String)
}
