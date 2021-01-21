package com.example.tugasbesarapmob.db_saving

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tugasbesarapmob.db_saving.DatabaseContractS.TabunganColumns.Companion.TABLE_NAME

class DatabaseHelperS(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbMySaving"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_TABUNGANKU = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContractS.TabunganColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContractS.TabunganColumns.DATE} TEXT NOT NULL," +
                " ${DatabaseContractS.TabunganColumns.KETERANGAN} TEXT NOT NULL," +
                " ${DatabaseContractS.TabunganColumns.PEMASUKKAN} INTEGER," +
                " ${DatabaseContractS.TabunganColumns.TOTAL} INTEGER)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_TABUNGANKU)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}