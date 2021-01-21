package com.example.tugasbesarapmob.UI

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.db_notes.DatabaseContractN
import com.example.tugasbesarapmob.db_notes.NoteHelper
import com.example.tugasbesarapmob.entity.Notes
import kotlinx.android.synthetic.main.activity_note_add_update.*
import kotlinx.android.synthetic.main.activity_saving_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdate : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var note: Notes? = null
    private var position: Int = 0
    private lateinit var helper: NoteHelper

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        helper = NoteHelper.getInstance(applicationContext)
        helper.open()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Notes()
        }

        val actionBarTitle: String

        if (isEdit) {
            actionBarTitle = "Edit Data Catatan"
            notes_ket.setText("Edit Data Catatan")
            btnSimpan_notes.setText("Update")

            note?.let {
                txtjudul.setText(it.judul)
                txtcontent.setText(it.content)
            }

        }
        else {
            actionBarTitle = "Tambah Data Catatan"
            notes_ket.setText("Tambah Data Catatan")
            btnSimpan_notes.setText("Save")
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnSimpan_notes.setOnClickListener(this)
        btnBatal_notes.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id. btnSimpan_notes) {


            val title = txtjudul.text.toString().trim()
            val content = txtcontent.text.toString().trim()

            if (title.isEmpty()) {
                input_layout_judul.error = "Field can not be blank"
                return
            }
            if (content.isEmpty()) {
                input_layout_content.error = "Field can not be blank"
                return
            }

            note?.judul = title
            note?.content = content

            val intent = Intent()

            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)


            val values = ContentValues()
            values.put(DatabaseContractN.NoteColumns.TITTLE, title)
            values.put(DatabaseContractN.NoteColumns.CONTENT, content)

            if (isEdit) {

                val result = helper.update(note?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdate, "Gagal Mengupdate Data", Toast.LENGTH_SHORT).show()
                }
            } else {
                note?.date = getCurrentDate()
                values.put(DatabaseContractN.NoteColumns.DATE, getCurrentDate())

                val result = helper.insert(values)

                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(PerhitunganAddUpdate.RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdate, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (view.id == R.id.btnBatal_notes){
            showAlertDialog(ALERT_DIALOG_CLOSE)
        }
    }


    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah Anda Ingin Membatalkan Perubahan ?"
        } else {
            dialogMessage = "Apakah Anda Yakin Ingin Menghapus Catatan Ini?"
            dialogTitle = "Hapus Data Catatan"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = helper.deleteById(note?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@NoteAddUpdate, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}