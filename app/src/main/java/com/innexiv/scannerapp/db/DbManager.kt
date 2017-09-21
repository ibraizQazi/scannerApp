package com.innexiv.scannerapp.db

import android.database.SQLException
import com.innexiv.scannerapp.data.DbModel
import com.innexiv.scannerapp.data.DbNodeModel
import io.reactivex.Observable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select


class DbManager(var dbHelper: DbHelper) : DbSource {

    private val log = AnkoLogger("DbManager")

    override fun getDbNodesList(): List<DbNodeModel> {
        
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //var listDbNodes = mutableListOf<DbNodeModel>()
        /*select(NodeTable.TABLE_NAME).parseList(object: MapRowParser<List<DbNodeModel>> {
               override fun parseRow(columns: Map<String, Any?>): List<DbNodeModel> {

                  val routeID = columns.getValue(NodeTable.ROUTE_ID)
                  val siteId = columns.getValue(NodeTable.SITE_ID)
                  val activityId = columns.getValue(NodeTable.ACTIVITY_ID)
                  val equipmentId = columns.getValue(NodeTable.EQUIPMENT_ID)
                  val barcode = columns.getValue(NodeTable.BARCODE)
                  val scannedOn = columns.getValue(NodeTable.SCANNED_ON)
                  val scannedBy = columns.getValue(NodeTable.SCANNED_BY)
                  val deviceImei = columns.getValue(NodeTable.DEVICE_IMEI)

                   val dbNode = DbNodeModel(routeID, siteId, activityId,
                           equipmentId, barcode, scannedOn, scannedBy, deviceImei)

                   listDbNodes.add(dbNode)
                   return listDbNodes
               }
           })*/

        //return listDbNodes.size
    }


    override fun getCount(): Int {

        return dbHelper.use {
            select(NodeTable.TABLE_NAME).exec {
                this.count
            }
        }


    }

    override fun saveScannedNodes(dbNodeModel: DbNodeModel) {
        try {
            dbHelper.use {
                insert(NodeTable.TABLE_NAME,
                        NodeTable.TABLE_ID to dbNodeModel.hashCode(),
                        NodeTable.ROUTE_ID to dbNodeModel.routeID,
                        NodeTable.SITE_ID to dbNodeModel.siteId,
                        NodeTable.ACTIVITY_ID to dbNodeModel.activityId,
                        NodeTable.EQUIPMENT_ID to dbNodeModel.equipmentId,
                        NodeTable.DEVICE_IMEI to dbNodeModel.deviceImei,
                        NodeTable.BARCODE to dbNodeModel.barcode,
                        NodeTable.SCANNED_BY to dbNodeModel.scannedBy,
                        NodeTable.SCANNED_ON to dbNodeModel.scannedOn)
            }
        } catch (e: SQLException) {
            (e.message)
        }
    }

}