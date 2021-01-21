package com.example.tugasbesarapmob.db_notes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tugasbesarapmob.db_notes.DatabaseContractN.NoteColumns.Companion.TABLE_NAME

class DatabaseHelperN(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbNotes"

        private const val DATABASE_VERSION = 1


        private val SQL_CREATE_TABLE_MYNOTES = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContractN.NoteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContractN.NoteColumns.DATE} TEXT NOT NULL," +
                " ${DatabaseContractN.NoteColumns.TITTLE} TEXT NOT NULL," +
                " ${DatabaseContractN.NoteColumns.CONTENT} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MYNOTES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}