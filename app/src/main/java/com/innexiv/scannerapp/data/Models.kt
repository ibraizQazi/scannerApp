package com.innexiv.scannerapp.data

import com.google.gson.annotations.SerializedName

data class BarcodePost(val barcode: String)
data class BarcodeResponse (@SerializedName("r") val r: String, @SerializedName("barcode") val barcode: String)
data class LoginPostBody(var email: String , var password: String, var imei: String)

data class LoginResponse (@SerializedName("status") val status: String, @SerializedName("token") val token: String)