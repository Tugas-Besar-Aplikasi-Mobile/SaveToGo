package com.example.tugasbesarapmob.helpers

import android.database.Cursor
import com.example.tugasbesarapmob.db_perhitungan.DatabaseContract
import com.example.tugasbesarapmob.entity.Perhitungan


object MappingHelperPerhitungan {

    fun mapCursorToArrayList(transCursor: Cursor?): ArrayList<Perhitungan> {
        val transList = ArrayList<Perhitungan>()
        transCursor?.apply {
            while (moveToNext() ) {

                    val id = getInt(getColumnIndexOrThrow(DatabaseContract.SavingColumns._ID))
                    val date = getString(getColumnIndexOrThrow(DatabaseContract.SavingColumns.DATE))
                    val nama = getString(getColumnIndexOrThrow(DatabaseContract.SavingColumns.NAMA))
                    val saldo = getInt(getColumnIndexOrThrow(DatabaseContract.SavingColumns.SALDO))
                    val total = getInt(getColumnIndexOrThrow(DatabaseContract.SavingColumns.TOTAL))
                    val hari = getInt(getColumnIndexOrThrow(DatabaseContract.SavingColumns.HARI))

                    transList.add(Perhitungan(id, date, nama, saldo, total, hari))
                }
            }

        return transList
    }
}

