package com.example.tugasbesarapmob.db_perhitungan

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class SavingColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "calculate"
            const val _ID = "_id"
            const val DATE = "date"
            const val NAMA = "nama"
            const val SALDO = "saldo"
            const val TOTAL = "total"
            const val HARI = "hari"


        }
    }
}