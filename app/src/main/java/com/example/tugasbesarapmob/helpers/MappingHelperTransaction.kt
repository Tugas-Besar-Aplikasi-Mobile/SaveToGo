package com.example.tugasbesarapmob.helpers

import android.database.Cursor
import com.example.tugasbesarapmob.db_transaction.DatabaseContractT
import com.example.tugasbesarapmob.entity.Transaction

object MappingHelperTransaction {

    fun mapCursorToArrayList(transCursor: Cursor?): ArrayList<Transaction> {
        val transList = ArrayList<Transaction>()
        transCursor?.apply {
            while (moveToNext() ) {

                    val id = getInt(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns._ID))
                    val date = getString(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns.DATE))
                    val keterangan = getString(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns.KETERANGAN))
                    val jenis = getString(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns.JENIS))
                    val pemasukkan = getInt(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns.PEMASUKKAN))
                    val pengeluaran = getInt(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns.PENGELUARAN))
                    val total_pemasukkan = getInt(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns.TOTAL_PEMASUKKAN))
                    val total_pengeluaran = getInt(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns.TOTAL_PENGELUARAN))
                    val saldo = getInt(getColumnIndexOrThrow(DatabaseContractT.TransactionColumns.SALDO))

                    transList.add(Transaction(id, date, keterangan, jenis, saldo, pemasukkan, pengeluaran, total_pemasukkan, total_pengeluaran))
                }
            }
        return transList
    }
}