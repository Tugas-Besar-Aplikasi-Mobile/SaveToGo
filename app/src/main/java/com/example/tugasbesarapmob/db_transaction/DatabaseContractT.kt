package com.example.tugasbesarapmob.db_transaction

import android.provider.BaseColumns

internal class DatabaseContractT {

    internal class TransactionColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "mytransaksi"
            const val _ID = "_id"
            const val DATE = "date"
            const val KETERANGAN = "keterangan"
            const val JENIS = "jenis"
            const val SALDO = "saldo"
            const val PEMASUKKAN = "pemasukkan"
            const val PENGELUARAN = "pengeluaran"
            const val TOTAL_PEMASUKKAN = "total_pemasukkan"
            const val TOTAL_PENGELUARAN = "total_pengeluaran"

        }
    }
}