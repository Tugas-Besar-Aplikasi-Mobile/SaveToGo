package com.example.tugasbesarapmob.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction (
        var id: Int = 0, //0
        var date: String? = null, //1
        var keterangan: String? = null, //2
        var jenis: String? = null, //3
        var saldo: Int=0, //4
        var pemasukkan: Int=0, //5
        var pengeluaran: Int=0,//6
        var total_pemasukkan: Int = 0, //7
        var total_pengeluaran: Int = 0 //8
): Parcelable