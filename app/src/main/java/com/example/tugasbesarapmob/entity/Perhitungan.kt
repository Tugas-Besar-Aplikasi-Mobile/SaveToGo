package com.example.tugasbesarapmob.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Perhitungan (
    var id: Int = 0, //0
    var date: String? = null, //1
    var nama: String? = null, //2
    var saldo: Int=0, //3
    var total: Int = 0, //4
    var hari: Int = 0 //5

): Parcelable