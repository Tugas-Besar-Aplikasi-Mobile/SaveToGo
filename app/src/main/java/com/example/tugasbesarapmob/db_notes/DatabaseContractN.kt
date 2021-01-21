package com.example.tugasbesarapmob.db_notes

import android.provider.BaseColumns

internal class DatabaseContractN {

    internal class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "notes"
            const val _ID = "_id"
            const val DATE = "date"
            const val TITTLE = "judul"
            const val CONTENT = "content"


        }
    }
}