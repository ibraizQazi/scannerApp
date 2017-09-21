package com.innexiv.scannerapp.db


import com.innexiv.scannerapp.data.DbNodeModel
import io.reactivex.Observable

interface DbSource {

    //save scanned node
    fun saveScannedNodes(dbNodeModel: DbNodeModel)

    //retrieve scanned models data
    //fun retrieveScannedNodes() : Observable<List<DbNodeModel>>
    fun getDbNodesList(): List<DbNodeModel>

    fun getCount(): Int

}