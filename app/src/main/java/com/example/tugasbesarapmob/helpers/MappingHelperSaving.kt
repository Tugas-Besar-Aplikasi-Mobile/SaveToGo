package com.example.tugasbesarapmob.helpers

import android.database.Cursor
import com.example.tugasbesarapmob.db_saving.DatabaseContractS
import com.example.tugasbesarapmob.entity.Saving

object MappingHelperSaving {

    fun mapCursorToArrayList(transCursor: Cursor?): ArrayList<Saving> {
    val transList = ArrayList<Saving>()
    transCursor?.apply {
        while (moveToNext() ) {

                val id = getInt(getColumnIndexOrThrow(DatabaseContractS.TabunganColumns._ID))
                val date = getString(getColumnIndexOrThrow(DatabaseContractS.TabunganColumns.DATE))
                val keterangan = getString(getColumnIndexOrThrow(DatabaseContractS.TabunganColumns.KETERANGAN))
                val pemasukkan = getInt(getColumnIndexOrThrow(DatabaseContractS.TabunganColumns.PEMASUKKAN))
                val total = getInt(getColumnIndexOrThrow(DatabaseContractS.TabunganColumns.TOTAL))

                transList.add(Saving(id, date, keterangan, pemasukkan, total))

        }
    }
        return transList
    }
}