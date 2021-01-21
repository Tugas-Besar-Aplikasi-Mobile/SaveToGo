package com.example.tugasbesarapmob.db_transaction

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tugasbesarapmob.db_transaction.DatabaseContractT.TransactionColumns.Companion.TABLE_NAME

class DatabaseHelperT(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbTransaksiku"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_TRANSACTIONSKU = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContractT.TransactionColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContractT.TransactionColumns.DATE} TEXT NOT NULL," +
                " ${DatabaseContractT.TransactionColumns.KETERANGAN} TEXT NOT NULL," +
                " ${DatabaseContractT.TransactionColumns.JENIS} TEXT NOT NULL," +
                " ${DatabaseContractT.TransactionColumns.SALDO} INTEGER," +
                " ${DatabaseContractT.TransactionColumns.PEMASUKKAN} INTEGER," +
                " ${DatabaseContractT.TransactionColumns.PENGELUARAN} INTEGER," +
                " ${DatabaseContractT.TransactionColumns.TOTAL_PEMASUKKAN} INTEGER," +
                " ${DatabaseContractT.TransactionColumns.TOTAL_PENGELUARAN} INTEGER)"
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(SQL_CREATE_TABLE_TRANSACTIONSKU)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }


}