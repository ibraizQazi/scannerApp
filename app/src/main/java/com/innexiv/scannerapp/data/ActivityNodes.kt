package com.innexiv.scannerapp.data

import com.google.gson.annotations.SerializedName



data class ActivityNodes(@SerializedName("equipment_layer") val equipmentLayer: List<EquipmentLayer>,
                         @SerializedName("data") val data: nodeData)

data class nodeData (@SerializedName("1") val nodeList: List<dataItem>)

data class dataItem(@SerializedName("equipment_id") val equipmentId: Int,
                    @SerializedName("item_quantity") val itemQuantity: Int,
                    @SerializedName("name") val name: String,
                    @SerializedName("shortcode") val shortCode: String,
                    @SerializedName("part_number") val partNumber: String,
                    @SerializedName("equipment_layer_id") val equipmentLayerId: Int,
                    @SerializedName("equipment_layer_name") val equipmentLayerName: String,
                    var isScanned: Boolean = false )



data class EquipmentLayer(@SerializedName("id") val id: Int,
                          @SerializedName("name") val name: String,
                          @SerializedName("detail") val details: String)