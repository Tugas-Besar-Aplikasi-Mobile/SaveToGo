package com.example.tugasbesarapmob.db_saving

import android.provider.BaseColumns

internal class  DatabaseContractS {

    internal class TabunganColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "mysavings"
            const val _ID = "_id"
            const val DATE = "date"
            const val KETERANGAN = "keterangan"
            const val PEMASUKKAN = "pemasukkan"
            const val TOTAL = "total"

        }
    }
}