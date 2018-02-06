package com.innexiv.scannerapp.db

import java.time.LocalDateTime
import java.util.*


class DbNodeModel (val routeID: Int,
                   val siteId: Int,
                   val activityId: Int,
                   val equipmentId: Int,
                   val barcode: String,
                   val scannedOn: String,
                   val scannedBy: String,
                   val deviceImei: String)

class DbModel (var map: MutableMap<String, Any?>){
    var _id : Long by map
    var routeId: Int by map
    var siteId: Int by map
    var activityId: Int by map
    var equipmentId: Int by map
    var barcode: String by map
    var scannedOn: String by map
    var scannedBy: String by map
    var deviceImei: String by map

    constructor(routeId: Int, siteId: Int, activityId: Int, equipmentId: Int,
                barcode: String, scannedOn: String, scannedBy: String, deviceImei: String) :this(HashMap()){
        this.routeId = routeId
        this.siteId = siteId
        this.activityId = activityId
        this.equipmentId = equipmentId
        this.barcode = barcode
        this.scannedOn = scannedOn
        this.scannedBy = scannedBy
        this.deviceImei = deviceImei
    }

}