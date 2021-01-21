package com.example.tugasbesarapmob.UI

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.db_saving.TabunganHelper
import com.example.tugasbesarapmob.entity.Saving
import kotlinx.android.synthetic.main.activity_saving_add_update.*
import kotlinx.android.synthetic.main.activity_transaction_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class SavingAddUpdate : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var saving: Saving? = null

    private var position: Int = 0
    private lateinit var savingHelper: TabunganHelper

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
        setContentView(R.layout.activity_saving_add_update)

        savingHelper = TabunganHelper.getInstance(applicationContext)
        savingHelper.open()

        saving = intent.getParcelableExtra(EXTRA_NOTE)

        if (saving != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        }
        else {
            saving = Saving()
        }

        val actionBarTitle: String

        if (isEdit) {
            actionBarTitle = "Edit Data Tabungan"
            judul_saving.setText("Edit Data Tabungan")
            btnSimpan_save.setText("Update")

            saving?.let {
                etNama_saving.setText(it.keterangan)
                etSaldo_saving.setText(it.pemasukkan.toString())

            }

        }
        else {
            actionBarTitle = "Tambah Data Tabungan"
            judul_saving.setText("Tambah Data Tabungan")
            btnSimpan_save.setText("Save")
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnSimpan_save.setOnClickListener(this)
        btnBatal_save.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        if (view.id == R.id.btnSimpan_save) {

            val title = etNama_saving.text.toString().trim()
            val get_saldo = etSaldo_saving.text.toString().trim()

            if (title.isEmpty()) {
                input_layout_nama_save.error = "Kolom Tidak Boleh Kosong"
                return
            }
            if(get_saldo.isEmpty()){
                input_layout_saldo_save.error = "Kolom Tidak Boleh Kosong"
                return
            }

            var money = get_saldo.toInt()

            var total_masuk = savingHelper.getMasuk()

            saving?.keterangan = title
            saving?.pemasukkan = money.toInt()
            saving?.date = getCurrentDate()
            saving?.total = total_masuk

            val intent = Intent()

            intent.putExtra(EXTRA_NOTE, saving)
            intent.putExtra(EXTRA_POSITION, position)

            if (isEdit) {

                val result = savingHelper.update(saving?.id.toString(), money, title).toLong()
                val result2 = savingHelper.update_saldo().toLong()
                if ((result > 0) && (result2 > 0)  ) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                }
                else {
                    Toast.makeText(this@SavingAddUpdate, "Gagal Mengupdate Data", Toast.LENGTH_SHORT).show()
                }
            }
            else {

                val check = savingHelper.isNotEmpty()

                if (check.count == 0) {

                    val result = savingHelper.insert(money, getCurrentDate(), title)

                    if (result > 0) {
                        saving?.id = result.toInt()
                        setResult(RESULT_ADD, intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@SavingAddUpdate, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    val result = savingHelper.insert_else(money, getCurrentDate(), title)
                    if (result > 0) {
                        saving?.id = result.toInt()
                        saving?.total = result.toInt()
                        setResult(RESULT_ADD, intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@SavingAddUpdate, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
        else if (view.id == R.id.btnBatal_save){
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
            dialogMessage = "Apakah Anda Yakin Ingin Menghapus Data Tabungan Ini?"
            dialogTitle = "Hapus Data Tabungan"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder.setMessage(dialogMessage).setCancelable(false).setPositiveButton("Ya") {
                dialog, id ->
            if (isDialogClose) {
                finish()
            }
            else {

                val result = savingHelper.deleteById(saving?.id.toString()).toLong()

                if (result > 0){
                    val intent = Intent()
                    intent.putExtra(EXTRA_POSITION, position)
                    setResult(RESULT_DELETE, intent)
                        finish()
                }
                else {
                    Toast.makeText(this@SavingAddUpdate, "Gagal Menghapus Data", Toast.LENGTH_SHORT).show()
                }
            }
        }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}