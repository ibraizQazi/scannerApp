package com.innexiv.scannerapp.db


interface DbSource {

    //save scanned node
    fun saveScannedNodes(dbNodeModel: DbNodeModel)

    //retrieve scanned models data
    //fun retrieveScannedNodes() : Observable<List<DbNodeModel>>
    fun getDbNodesList(): List<DbNodeModel>

    fun getCount(): Int

}