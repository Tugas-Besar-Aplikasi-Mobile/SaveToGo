package com.example.tugasbesarapmob.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Saving (
        var id: Int = 0, //0
        var date: String? = null, //1
        var keterangan: String? = null, //2
        var pemasukkan: Int=0, //3
        var total: Int = 0 //4
): Parcelable