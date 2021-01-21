package com.example.tugasbesarapmob.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.adapter.TransactionAdapter
import com.example.tugasbesarapmob.db_transaction.DatabaseContractT
import com.example.tugasbesarapmob.db_transaction.TransactionHelper
import com.example.tugasbesarapmob.entity.Transaction
import com.example.tugasbesarapmob.helpers.MappingHelperTransaction
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_transaction_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionMain : AppCompatActivity() {

    private lateinit var adapter: TransactionAdapter
    private lateinit var noteHelper: TransactionHelper

    var send_jenis:String = ""

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_main)

        rv_transactions.layoutManager = LinearLayoutManager(this)
        rv_transactions.setHasFixedSize(true)
        adapter = TransactionAdapter(this)
        rv_transactions.adapter = adapter

        greeting.text = "Welcome Back ! Nice To See You Again"



        btnTambahMasuk_trans.setOnClickListener {

            send_jenis = "Pemasukkan"
            val intent = Intent(this@TransactionMain, TransactionAddUpdate::class.java)
            intent.putExtra("SEND_JENIS", send_jenis.trim { it <= ' ' })
            startActivityForResult(intent, TransactionAddUpdate.REQUEST_ADD)
        }
        btnTambahKeluar_trans.setOnClickListener {
            send_jenis = "Pengeluaran"
            val intent = Intent(this@TransactionMain, TransactionAddUpdate::class.java)
            intent.putExtra("SEND_JENIS", send_jenis.trim { it <= ' ' })
            startActivityForResult(intent, TransactionAddUpdate.REQUEST_ADD)
        }

        noteHelper = TransactionHelper.getInstance(applicationContext)
        noteHelper.open()

        if (savedInstanceState == null) {
            loadNotesAsync()
            initialCheck()
        }
        else {
            val list = savedInstanceState.getParcelableArrayList<Transaction>(EXTRA_STATE)
            initialCheck()
            if (list != null) {
                adapter.listNotes = list
            }
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar2.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAllT()
                MappingHelperTransaction.mapCursorToArrayList(cursor)
            }
            progressBar2.visibility = View.INVISIBLE

            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapter.listNotes = notes
            }
            else {
                adapter.listNotes = ArrayList()
                showSnackbarMessage("Tidak Ada Data Saat Ini")
            }
        }
    }

    private fun initialCheck() {

        var total_masuk = noteHelper.getMasuk()
        var total_keluar = noteHelper.getKeluar()
        var get_saldo = noteHelper.getSaldo()


        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        view_keluar_trans.text = format.format(total_keluar).toString()
        view_masuk_trans.text = format.format(total_masuk).toString()
        saldo_transaction.text = format.format(get_saldo).toString()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                TransactionAddUpdate.REQUEST_ADD -> if (resultCode == TransactionAddUpdate.RESULT_ADD) {
                    val note = data.getParcelableExtra<Transaction>(TransactionAddUpdate.EXTRA_NOTE)

                    if (note != null) {
                        adapter.addItem(note)
                    }
                    rv_transactions.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackbarMessage("Satu Data Berhasil Ditambahkan")
                }
                TransactionAddUpdate.REQUEST_UPDATE ->
                    when (resultCode) {
                        TransactionAddUpdate.RESULT_UPDATE -> {

                            val note = data.getParcelableExtra<Transaction>(TransactionAddUpdate.EXTRA_NOTE)
                            val position = data.getIntExtra(TransactionAddUpdate.EXTRA_POSITION, 0)

                            if (note != null) {
                                adapter.updateItem(position, note)
                            }
                            rv_transactions.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu Data Berhasil Diubah")
                        }

                        TransactionAddUpdate.RESULT_DELETE -> {
                            val position = data.getIntExtra(TransactionAddUpdate.EXTRA_POSITION, 0)

                            adapter.removeItem(position)

                            showSnackbarMessage("Satu Data Berhasil Dihapus")
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
        Snackbar.make(rv_transactions, message, Snackbar.LENGTH_SHORT).show()
    }
}