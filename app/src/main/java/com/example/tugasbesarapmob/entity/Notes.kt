package com.example.tugasbesarapmob.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notes (

        var id: Int = 0, //0
        var date: String? = null, //1
        var judul: String? = null, //2
        var content: String? = null

): Parcelable