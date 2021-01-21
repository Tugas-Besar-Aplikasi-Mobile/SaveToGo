package com.example.tugasbesarapmob.db_transaction

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.tugasbesarapmob.db_transaction.DatabaseContractT.TransactionColumns.Companion.TABLE_NAME
import com.example.tugasbesarapmob.db_transaction.DatabaseContractT.TransactionColumns.Companion._ID
import kotlin.jvm.Throws

class TransactionHelper(context: Context) {

    private var dataBaseHelperT: DatabaseHelperT = DatabaseHelperT(context)
    private lateinit var databaseT: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: TransactionHelper? = null

        fun getInstance(context: Context): TransactionHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: TransactionHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open() {
        databaseT = dataBaseHelperT.writableDatabase
    }

    fun close() {
        dataBaseHelperT.close()

        if (databaseT.isOpen)
            databaseT.close()
    }

    fun queryAllT(): Cursor {

        return databaseT.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$_ID ASC",
                null)
    }

    fun getSaldo(): Int {
        var saldo: Int
        var masuk = 0
        var keluar = 0
        databaseT = dataBaseHelperT.writableDatabase
        val sql = databaseT.rawQuery("SELECT * FROM mytransaksi", null)
        if (sql.count == 0) {
            saldo = 0
        }
        else {
            while (sql.moveToNext()) {
                masuk = sql.getInt(7)
                keluar = sql.getInt(8)
            }
            saldo = masuk - keluar
        }
        return saldo

    }

    fun updateSaldo(): Int {

        var saldo: Int
        var masuk = 0
        var keluar = 0
        var getmasuk = 0
        var getkeluar = 0
        databaseT = dataBaseHelperT.writableDatabase
        val sql = databaseT.rawQuery("SELECT * FROM mytransaksi", null)
        if (sql.count == 0) {
            saldo = 0
        }
        else {
            while (sql.moveToNext()) {
                masuk = sql.getInt(5)
                keluar = sql.getInt(6)
                getmasuk += masuk
                getkeluar += keluar
            }
            saldo = getmasuk - getkeluar
        }
        return saldo

    }

    fun getMasuk(): Int {

        var total_pemasukkan:Int = 0
        var masuk = 0
        val sql = databaseT.rawQuery("SELECT * FROM mytransaksi", null)
        if (sql.count == 0) {
            total_pemasukkan = 0
        }
        else {
            while (sql.moveToNext()) {
                masuk = sql.getInt(7)

            }
            total_pemasukkan = masuk
        }
        return total_pemasukkan

    }

    fun updateMasuk(): Int {

        var total_pemasukkan:Int = 0
        var masuk = 0
        val sql = databaseT.rawQuery("SELECT * FROM mytransaksi", null)
        if (sql.count == 0) {
            total_pemasukkan = 0
        }
        else {
            while (sql.moveToNext()) {
                masuk = sql.getInt(5)
                total_pemasukkan += masuk
            }
        }
        return total_pemasukkan

    }

    fun getKeluar(): Int {

        var total_pengeluaran:Int = 0
        var keluar = 0

        val sql = databaseT.rawQuery("SELECT * FROM mytransaksi", null)
        if (sql.count == 0) {
            total_pengeluaran = 0
        }
        else {
            while (sql.moveToNext()) {
                keluar = sql.getInt(8)

            }
            total_pengeluaran = keluar
        }
        return total_pengeluaran

    }

    fun updateKeluar(): Int {

        var total_pengeluaran:Int = 0
        var keluar = 0

        val sql = databaseT.rawQuery("SELECT * FROM mytransaksi", null)
        if (sql.count == 0) {
            total_pengeluaran = 0
        }
        else {
            while (sql.moveToNext()) {
                keluar = sql.getInt(6)
                total_pengeluaran += keluar

            }

        }
        return total_pengeluaran

    }

    fun isNotEmpty(): Cursor {
        return databaseT.rawQuery("SELECT * FROM mytransaksi", null)
    }

    fun insert(saldo: Int, date: String, jenis: String, keterangan:String): Long {
        var total_masuk : Int = 0
        var total_keluar : Int = 0

        val value = ContentValues()
        when (jenis) {
            "Pemasukkan" -> {
                value.put("pemasukkan", saldo)
                value.put("pengeluaran", 0)
                total_masuk = saldo
                total_keluar = 0
            }
            "Pengeluaran" -> {
                value.put("pengeluaran", saldo)
                value.put("pemasukkan", 0)
                total_masuk = 0
                total_keluar = saldo
            }
        }

        var totalSaldo : Int = 0

        totalSaldo = total_masuk - total_keluar

        value.put("date", date)
        value.put("jenis", jenis)
        value.put("saldo", totalSaldo)
        value.put("keterangan", keterangan)
        value.put("total_pemasukkan", total_masuk)
        value.put("total_pengeluaran", total_keluar)
        return  databaseT.insert("mytransaksi", null, value)
    }

    fun getUser():Cursor{
        return databaseT.rawQuery("SELECT * FROM mytransaksi", null)
    }


    fun insert_else(saldo: Int, date: String, jenis: String, keterangan:String): Long {
        var total_masuk : Int = 0
        var total_keluar : Int = 0
        var db_add = ""
        val value = ContentValues()
        when (jenis) {
            "Pemasukkan" -> {
                value.put("pemasukkan", saldo)
                value.put("pengeluaran", 0)
                total_masuk = getMasuk().toInt() + saldo
                total_keluar = getKeluar().toInt()
            }
            "Pengeluaran" -> {
                value.put("pengeluaran", saldo)
                value.put("pemasukkan", 0)
                total_masuk = getMasuk().toInt()
                total_keluar = getKeluar().toInt() + saldo
            }
        }

        var totalSaldo : Int = 0

        totalSaldo = total_masuk - total_keluar

        value.put("date", date)
        value.put("jenis", jenis)
        value.put("saldo", totalSaldo)
        value.put("keterangan", keterangan)
        value.put("total_pemasukkan", total_masuk)
        value.put("total_pengeluaran", total_keluar)
        return  databaseT.insert("mytransaksi", null, value)
    }

    fun update(id: String, saldo: Int, jenis: String, keterangan:String): Int {

        val detail = ContentValues()

        when (jenis) {
            "Pemasukkan" -> {
                detail.put("pemasukkan", saldo)
                detail.put("pengeluaran", 0)
            }
            "Pengeluaran" -> {
                detail.put("pengeluaran", saldo)
                detail.put("pemasukkan", 0)

            }
        }
        detail.put("jenis", jenis)
        detail.put("keterangan", keterangan)

        return databaseT.update("mytransaksi", detail, "$_ID = ?", arrayOf(id))

    }

    fun update_saldo(): Int {

        var total_masuk : Int
        var total_keluar : Int
        val detail2 = ContentValues()


        total_masuk = updateMasuk().toInt()
        total_keluar = updateKeluar().toInt()


        val sql = databaseT.rawQuery("SELECT * FROM mytransaksi", null)

        while (sql.moveToNext()) {
            detail2.put("saldo", updateSaldo())
            detail2.put("total_pemasukkan", total_masuk)
            detail2.put("total_pengeluaran", total_keluar)

        }

        return databaseT.update("mytransaksi", detail2, null, null)
    }

    fun deleteById(id: String): Int {

        var total_masuk : Int
        var total_keluar : Int
        val detail2 = ContentValues()


        total_masuk = updateMasuk().toInt()
        total_keluar = updateKeluar().toInt()


        val sql = databaseT.rawQuery("SELECT * FROM mytransaksi", null)

        while (sql.moveToNext()) {
            detail2.put("saldo", updateSaldo())
            detail2.put("total_pemasukkan", total_masuk)
            detail2.put("total_pengeluaran", total_keluar)

        }

        databaseT.update("mytransaksi", detail2, null, null)

        return databaseT.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}
