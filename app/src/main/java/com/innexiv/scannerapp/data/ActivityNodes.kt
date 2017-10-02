package com.innexiv.scannerapp.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class ActivityNodes(
        @Json(name = "equipment_layer")
        val equipmentLayer: List<EquipmentLayer>,
        @Json(name = "data")
        val data: List<dataItem>) : Parcelable {

    companion object {
        @JvmField val CREATOR : Parcelable.Creator<ActivityNodes> = object : Parcelable.Creator<ActivityNodes> {
            override fun createFromParcel(p0: Parcel): ActivityNodes = ActivityNodes(p0)
            override fun newArray(p0: Int): Array<ActivityNodes?> = arrayOfNulls(p0)
        }
    }
    constructor(source: Parcel) : this (source.createTypedArrayList(EquipmentLayer.CREATOR), source.createTypedArrayList(dataItem.CREATOR))

    override fun describeContents(): Int = 0

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.let {
            with(p0){
                writeTypedList(equipmentLayer)
                writeTypedList(data)
            }
        }
    }
}

data class dataItem(
        @Json(name = "equipment_id")
        val equipmentId: Int,
        @Json(name = "item_quantity")
        val itemQuantity: Int,
        @Json(name = "name")
        val name: String,
        @Json(name = "shortcode")
        val shortCode: String,
        @Json(name = "part_number")
        val partNumber: String,
        @Json(name = "equipment_layer_id")
        val equipmentLayerId: Int,
        @Json(name = "equipment_layer_name")
        val equipmentLayerName: String,
        var isScanned: Boolean = false ) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<dataItem> = object : Parcelable.Creator<dataItem> {
            override fun createFromParcel(source: Parcel): dataItem = dataItem(source)
            override fun newArray(size: Int): Array<dataItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this (source.readInt(),source.readInt(), source.readString(), source.readString(),
            source.readString(), source.readInt(),source.readString(),source.hasFileDescriptors())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.let {
            with(dest){
                writeInt(equipmentId)
                writeInt(itemQuantity)
                writeString(name)
                writeString(shortCode)
                writeString(partNumber)
                writeInt(equipmentLayerId)
                writeString(equipmentLayerName)
                writeValue(isScanned)
            }
        }
    }
}


data class EquipmentLayer(
        @Json(name = "id")
        val id: Int,
        @Json(name = "name")
        val name: String,
        @Json(name = "detail")
        val details: String) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<EquipmentLayer> = object : Parcelable.Creator<EquipmentLayer> {
            override fun createFromParcel(source: Parcel): EquipmentLayer = EquipmentLayer(source)
            override fun newArray(size: Int): Array<EquipmentLayer?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this (source.readInt(),source.readString(),source.readString())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.let{
            with(dest) {
                writeInt(id)
                writeString(name)
                writeString(details)
            }
        }
    }
}