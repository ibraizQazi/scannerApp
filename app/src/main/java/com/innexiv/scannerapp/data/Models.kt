package com.innexiv.scannerapp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BarcodePost(val barcode: String)
data class BarcodeResponse (@SerializedName("r") val r: String, @SerializedName("barcode") val barcode: String) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<BarcodeResponse> = object : Parcelable.Creator<BarcodeResponse> {
            override fun createFromParcel(p0: Parcel): BarcodeResponse = BarcodeResponse(p0)
            override fun newArray(p0: Int): Array<BarcodeResponse?> = arrayOfNulls(p0)
        }
    }
    constructor(source: Parcel) : this (source.readString(), source.readString())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.let{
            with(dest) {
                writeString(r)
                writeString(barcode)
            }
        }
    }
}


data class LoginPostBody(var email: String , var password: String, var imei: String)
data class LoginResponse (@SerializedName("status") val status: String, @SerializedName("token") val token: String) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<LoginResponse> = object : Parcelable.Creator<LoginResponse> {
            override fun createFromParcel(source: Parcel): LoginResponse = LoginResponse(source)
            override fun newArray(size: Int): Array<LoginResponse?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let{
            with(dest) {
                writeString(status)
                writeString(token)
            }
        }
    }
}

//site models
data class SiteResponse(@SerializedName("result") val result: String,
                        @SerializedName("freason") val freason: String,
                        @SerializedName("site") val siteList: ArrayList<SiteObject>) {

    data class SiteObject(@SerializedName("name") val name: String,
                           @SerializedName("short_name") val shortName: String,
                           @SerializedName("id") val id: Int,
                           @SerializedName("country_id") val countryId: Int,
                           @SerializedName("region_id") val regionId: Int,
                           @SerializedName("province_id") val provinceId: Int,
                           @SerializedName("city_id") val cityId: Int,
                           @SerializedName("latitude") val latitude: String,
                           @SerializedName("longitude") val longitude: String)
}
