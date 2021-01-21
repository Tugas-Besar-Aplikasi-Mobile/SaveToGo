package com.example.tugasbesarapmob.db_perhitungan

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tugasbesarapmob.db_perhitungan.DatabaseContract.SavingColumns.Companion.TABLE_NAME

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbKalkulasiku"

        private const val DATABASE_VERSION = 1


        private val SQL_CREATE_TABLE_MYSAVING = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.SavingColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.SavingColumns.DATE} TEXT NOT NULL," +
                " ${DatabaseContract.SavingColumns.NAMA} TEXT NOT NULL," +
                " ${DatabaseContract.SavingColumns.SALDO} INTEGER," +
                " ${DatabaseContract.SavingColumns.TOTAL} INTEGER," +
                " ${DatabaseContract.SavingColumns.HARI} INTEGER)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MYSAVING)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}