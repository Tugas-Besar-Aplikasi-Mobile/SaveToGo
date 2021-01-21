package com.example.tugasbesarapmob.UI

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.db_perhitungan.DatabaseContract
import com.example.tugasbesarapmob.db_perhitungan.DatabaseContract.SavingColumns.Companion.DATE
import com.example.tugasbesarapmob.db_perhitungan.PerhitunganHelper
import com.example.tugasbesarapmob.entity.Perhitungan
import kotlinx.android.synthetic.main.activity_perhitungan_add_update.*
import kotlinx.android.synthetic.main.activity_saving_add_update.*
import kotlinx.android.synthetic.main.perhitungan_adapter.*
import java.text.SimpleDateFormat
import java.util.*

class PerhitunganAddUpdate : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var note: Perhitungan? = null
    private var position: Int = 0
    private lateinit var noteHelper: PerhitunganHelper

    var username_get:String = ""

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
        setContentView(R.layout.activity_perhitungan_add_update)

        noteHelper = PerhitunganHelper.getInstance(applicationContext)
        noteHelper.open()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Perhitungan()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Edit Data Kalkulasi"
            judul_calc.setText("Edit Data Kalkulasi")
            btnSimpan_calc.setText("Update")

            note?.let {
                et_judul.setText(it.nama)
                et_Saldo.setText(it.saldo.toString())
                et_hari.setText(it.hari.toString())
            }

        } else {
            actionBarTitle = "Tambah Data Kalkulasi"
            judul_calc.setText("Tambah Data Kalkulasi")
            btnSimpan_calc.setText("Save")
        }


        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnSimpan_calc.setOnClickListener(this)
        btnBatal_calc.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btnSimpan_calc) {


            val title = et_judul.text.toString().trim()
            val saldo = et_Saldo.text.toString().trim().toInt()
            val hari = et_hari.text.toString().trim().toInt()

            if (title.isEmpty()) {
                txtNamaTransaksi.error = "Field can not be blank"
                return
            }

            //val usernames = et_username.text.toString().trim()


            var total = saldo * hari

            note?.nama = title
            note?.saldo = saldo.toInt()
            note?.hari = hari.toInt()

            note?.total = total.toInt()

            val intent = Intent()

            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)


            val values = ContentValues()
            values.put(DatabaseContract.SavingColumns.NAMA, title)
            values.put(DatabaseContract.SavingColumns.SALDO, saldo.toInt())
            values.put(DatabaseContract.SavingColumns.HARI, hari.toInt())
            values.put(DatabaseContract.SavingColumns.TOTAL, total)

            if (isEdit) {

                val result = noteHelper.update(note?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@PerhitunganAddUpdate, "Gagal Mengupdate Data", Toast.LENGTH_SHORT).show()
                }
            } else {
                note?.date = getCurrentDate()
                values.put(DATE, getCurrentDate())

                val result = noteHelper.insert(values)

                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@PerhitunganAddUpdate, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (view.id == R.id.btnBatal_calc){
            showAlertDialog(ALERT_DIALOG_CLOSE)
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
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
            dialogMessage = "Apakah Anda Yakin Ingin Menghapus Kalkulasi Ini?"
            dialogTitle = "Hapus Data Kalkulasi Tabungan"
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
                    val result = noteHelper.deleteById(note?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@PerhitunganAddUpdate, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}