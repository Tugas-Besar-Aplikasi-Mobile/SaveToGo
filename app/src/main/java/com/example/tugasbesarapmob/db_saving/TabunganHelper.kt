package com.example.tugasbesarapmob.db_saving

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.tugasbesarapmob.db_saving.DatabaseContractS.TabunganColumns.Companion.TABLE_NAME
import com.example.tugasbesarapmob.db_saving.DatabaseContractS.TabunganColumns.Companion._ID
import kotlin.jvm.Throws

class TabunganHelper(context: Context){

    private var dataBaseHelperS: DatabaseHelperS = DatabaseHelperS(context)
    private lateinit var database: SQLiteDatabase


    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: TabunganHelper? = null

        fun getInstance(context: Context): TabunganHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: TabunganHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelperS.writableDatabase
    }

    fun close() {
        dataBaseHelperS.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAllS():Cursor{

        return database.rawQuery("SELECT * FROM mysavings", null)
    }


    fun getMasuk(): Int {

        var total_pemasukkan:Int = 0
        var masuk = 0
        val sql = database.rawQuery("SELECT * FROM mysavings", null)
        if (sql.count == 0) {
            total_pemasukkan = 0
        }
        else {
            while (sql.moveToNext()) {
                masuk = sql.getInt(3)
                total_pemasukkan += masuk
            }
        }
        return total_pemasukkan
    }


    fun isNotEmpty(): Cursor {

        return database.rawQuery("SELECT * FROM mysavings", null)
    }

    fun insert(masuk: Int, date: String, keterangan:String): Long {

        val value = ContentValues()

        value.put("pemasukkan", masuk)
        value.put("total", masuk)
        value.put("date", date)
        value.put("keterangan", keterangan)

        return  database.insert("mysavings", null, value)
    }

    fun insert_else(masuk: Int, date: String, keterangan:String): Long {

        var total : Int

        total = getMasuk().toInt() + masuk

        val value = ContentValues()

        value.put("pemasukkan", masuk)
        value.put("total", total)
        value.put("date", date)
        value.put("keterangan", keterangan)

        return  database.insert("mysavings", null, value)
    }

    fun update(id: String, masuk: Int, keterangan:String): Int {

        val detail = ContentValues()

        detail.put("pemasukkan", masuk)
        detail.put("keterangan", keterangan)


        return database.update("mysavings", detail, "$_ID = ?", arrayOf(id))

    }

    fun update_saldo(): Int {

        var total: Int

        val detail2 = ContentValues()

        total = getMasuk().toInt()

        val sql = database.rawQuery("SELECT * FROM mysavings", null)

        while (sql.moveToNext()) {
            detail2.put("total", total)
        }

        return database.update("mysavings", detail2, null,null )
    }

    fun deleteById(id: String): Int {

        var total: Int

        val detail2 = ContentValues()

        total = getMasuk().toInt()
        

        val sql = database.rawQuery("SELECT * FROM mysavings", null)

        while (sql.moveToNext()) {
            detail2.put("total", total)

        }

        database.update("mysavings", detail2, null, null)

        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}