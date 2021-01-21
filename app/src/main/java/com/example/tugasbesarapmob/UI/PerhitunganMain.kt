package com.example.tugasbesarapmob.UI

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.adapter.PerhitunganAdapter
import com.example.tugasbesarapmob.db_perhitungan.PerhitunganHelper
import com.example.tugasbesarapmob.entity.Perhitungan
import com.example.tugasbesarapmob.helpers.MappingHelperPerhitungan
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_perhitungan_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PerhitunganMain : AppCompatActivity() {

    private lateinit var adapter: PerhitunganAdapter
    private lateinit var noteHelper: PerhitunganHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perhitungan_main)


        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)
        adapter = PerhitunganAdapter(this)
        rv_notes.adapter = adapter

        btnTambahMasuk.setOnClickListener {
            val intent = Intent(this@PerhitunganMain, PerhitunganAddUpdate::class.java)
            startActivityForResult(intent, PerhitunganAddUpdate.REQUEST_ADD)
        }

        noteHelper = PerhitunganHelper.getInstance(applicationContext)
        noteHelper.open()

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Perhitungan>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar2.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelperPerhitungan.mapCursorToArrayList(cursor)
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
                PerhitunganAddUpdate.REQUEST_ADD -> if (resultCode == PerhitunganAddUpdate.RESULT_ADD) {
                    val note = data.getParcelableExtra<Perhitungan>(PerhitunganAddUpdate.EXTRA_NOTE)

                    if (note != null) {
                        adapter.addItem(note)
                    }
                    rv_notes.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                PerhitunganAddUpdate.REQUEST_UPDATE ->
                    when (resultCode) {
                        PerhitunganAddUpdate.RESULT_UPDATE -> {

                            val note = data.getParcelableExtra<Perhitungan>(PerhitunganAddUpdate.EXTRA_NOTE)
                            val position = data.getIntExtra(PerhitunganAddUpdate.EXTRA_POSITION, 0)

                            if (note != null) {
                                adapter.updateItem(position, note)
                            }
                            rv_notes.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu item berhasil diubah")
                        }

                        PerhitunganAddUpdate.RESULT_DELETE -> {
                            val position = data.getIntExtra(PerhitunganAddUpdate.EXTRA_POSITION, 0)

                            adapter.removeItem(position)

                            showSnackbarMessage("Satu item berhasil dihapus")
                        }
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        noteHelper.close()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_notes, message, Snackbar.LENGTH_SHORT).show()
    }
}