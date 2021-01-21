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
import com.example.tugasbesarapmob.db_transaction.TransactionHelper
import com.example.tugasbesarapmob.entity.Transaction
import kotlinx.android.synthetic.main.activity_saving_add_update.*
import kotlinx.android.synthetic.main.activity_transaction_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionAddUpdate : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var note: Transaction? = null
    private var position: Int = 0
    private lateinit var noteHelper: TransactionHelper

    var get_jenis:String = ""

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
        setContentView(R.layout.activity_transaction_add_update)

        noteHelper = TransactionHelper.getInstance(applicationContext)
        noteHelper.open()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Transaction()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Edit Data Transaksi"
            judul_trans.setText("Edit Data Transaksi")
            btnSimpan_trans.setText("Update Data")

            note?.let {
                if(it.jenis == "Pemasukkan") {
                    etNama_trans.setText(it.keterangan)
                    etSaldo_trans.setText(it.pemasukkan.toString())
                    get_jenis = "Pemasukkan"

                }
                else{
                    etNama_trans.setText(it.keterangan)
                    etSaldo_trans.setText(it.pengeluaran.toString())
                    get_jenis = "Pengeluaran"
                }
            }

        }
        else {
            actionBarTitle = "Tambah Data Transaksi"
            judul_trans.setText("Tambah Data Transaksi")
            btnSimpan_trans.setText("Save")
        }


        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnSimpan_trans.setOnClickListener(this)
        btnBatal_trans.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        if (view.id == R.id.btnSimpan_trans) {


            val title = etNama_trans.text.toString().trim()
            val get_saldo = etSaldo_trans.text.toString().trim()


            if (title.isEmpty()) {
                input_layout_nama_trans.error = "Kolom Tidak Boleh Kosong"
                return
            }
            if(get_saldo.isEmpty()){
                input_layout_saldo_trans.error = "Kolom Tidak Boleh Kosong"
                return
            }

            var money = get_saldo.toInt()


            var jenis_get = intent.getStringExtra("SEND_JENIS")
            var input = jenis_get.toString().trim()

            val intent = Intent()

            if(input=="Pemasukkan"){
                note?.pemasukkan = money
            }
            else{
                note?.pengeluaran = money
            }
            note?.pemasukkan = money
            note?.pengeluaran = money

            note?.keterangan = title
            note?.date = getCurrentDate()
            note?.jenis = input

            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)


            if (isEdit) {

                val result = noteHelper.update(note?.id.toString(), money, get_jenis, title).toLong()
                val result2 = noteHelper.update_saldo().toLong()
                if ((result > 0) && (result2 > 0)  ) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                }
                else {
                    Toast.makeText(this@TransactionAddUpdate, "Gagal Mengupdate Data", Toast.LENGTH_SHORT).show()
                }
            }
            else {

                val check = noteHelper.isNotEmpty()

                if (check.count == 0) {
                    val result = noteHelper.insert( money, getCurrentDate(), input, title)

                    if (result > 0) {
                        note?.id = result.toInt()
                        setResult(RESULT_ADD, intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@TransactionAddUpdate, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    val result = noteHelper.insert_else(money, getCurrentDate(), input, title)
                    if (result > 0) {
                        note?.id = result.toInt()
                        setResult(RESULT_ADD, intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@TransactionAddUpdate, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
        else if (view.id == R.id.btnBatal_trans){
            showAlertDialog(ALERT_DIALOG_CLOSE)
        }
    }


    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm")
        return sdf.format(Date())

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
            dialogMessage = "Apakah Anda Yakin Ingin Membatalkan Perubahan ?"
        } else {
            dialogMessage = "Apakah Anda Yakin Ingin Menghapus Data Transaksi Ini?"
            dialogTitle = "Hapus Data Transaksi"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder.setMessage(dialogMessage).setCancelable(false).setPositiveButton("Ya") {
            dialog, id ->
                    if (isDialogClose) {
                        finish()
                    }
                    else {

                        val result = noteHelper.deleteById(note?.id.toString()).toLong()

                        if (result > 0){

                                val intent = Intent()
                                intent.putExtra(EXTRA_POSITION, position)
                                setResult(RESULT_DELETE, intent)
                                finish()

                        }
                        else {
                            Toast.makeText(this@TransactionAddUpdate, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}
