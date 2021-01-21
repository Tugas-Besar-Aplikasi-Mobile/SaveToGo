package com.example.tugasbesarapmob.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.adapter.NoteAdapter
import com.example.tugasbesarapmob.db_notes.NoteHelper
import com.example.tugasbesarapmob.entity.Notes
import com.example.tugasbesarapmob.helpers.MappingHelperNote
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.activity_note.progressBar2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class NoteActivity : AppCompatActivity(){

    private lateinit var adapter: NoteAdapter
    private lateinit var helper: NoteHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)


        rv_catatan.layoutManager = LinearLayoutManager(this)
        rv_catatan.setHasFixedSize(true)
        adapter = NoteAdapter(this)
        rv_catatan.adapter = adapter

        btnTambahMasuk.setOnClickListener {
            val intent = Intent(this@NoteActivity, NoteAddUpdate::class.java)
            startActivityForResult(intent, NoteAddUpdate.REQUEST_ADD)
        }

        helper = NoteHelper.getInstance(applicationContext)
        helper.open()

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Notes>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar2.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = helper.queryAll()
                MappingHelperNote.mapCursorToArrayList(cursor)
            }
            progressBar2.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
                showSnackbarMessage("Tidak Ada Data Saat Ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                NoteAddUpdate.REQUEST_ADD -> if (resultCode == NoteAddUpdate.RESULT_ADD) {
                    val note = data.getParcelableExtra<Notes>(NoteAddUpdate.EXTRA_NOTE)

                    if (note != null) {
                        adapter.addItem(note)
                    }
                    rv_catatan.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                PerhitunganAddUpdate.REQUEST_UPDATE ->
                    when (resultCode) {
                        PerhitunganAddUpdate.RESULT_UPDATE -> {

                            val note = data.getParcelableExtra<Notes>(NoteAddUpdate.EXTRA_NOTE)
                            val position = data.getIntExtra(NoteAddUpdate.EXTRA_POSITION, 0)

                            if (note != null) {
                                adapter.updateItem(position, note)
                            }
                            rv_catatan.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu item berhasil diubah")
                        }

                        PerhitunganAddUpdate.RESULT_DELETE -> {
                            val position = data.getIntExtra(NoteAddUpdate.EXTRA_POSITION, 0)

                            adapter.removeItem(position)

                            showSnackbarMessage("Satu item berhasil dihapus")
                        }
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_catatan, message, Snackbar.LENGTH_SHORT).show()
    }
}