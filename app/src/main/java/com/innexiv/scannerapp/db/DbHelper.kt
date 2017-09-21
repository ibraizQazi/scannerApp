package com.innexiv.scannerapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DbHelper(ctx: Context) : ManagedSQLiteOpenHelper( ctx, DB_NAME, null, DB_VERSION ){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(NodeTable.TABLE_NAME, true,
                NodeTable.TABLE_ID to INTEGER + PRIMARY_KEY,
                NodeTable.ROUTE_ID to INTEGER,
                NodeTable.SITE_ID to INTEGER,
                NodeTable.ACTIVITY_ID to INTEGER,
                NodeTable.DEVICE_IMEI to TEXT,
                NodeTable.BARCODE to TEXT,
                NodeTable.EQUIPMENT_ID to INTEGER,
                NodeTable.SCANNED_ON to TEXT,
                NodeTable.SCANNED_BY to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        /*db?.dropTable(NodeTable.TABLE_NAME)
        onCreate(db)*/
    }


    companion object {
        @JvmField
        val DB_VERSION = 1
        @JvmField
        val DB_NAME = "dbScanner"

        private var instance: DbHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DbHelper {
            if (instance == null) {
                instance = DbHelper(ctx.applicationContext)
            }
            return instance!!
        }

    }
}


val Context.database: DbHelper
    get() = DbHelper.getInstance(applicationContext)