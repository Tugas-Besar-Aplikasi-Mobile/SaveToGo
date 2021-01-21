package com.example.tugasbesarapmob.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasbesarapmob.R
import com.example.tugasbesarapmob.adapter.TabunganAdapter
import com.example.tugasbesarapmob.db_saving.TabunganHelper
import com.example.tugasbesarapmob.entity.Saving
import com.example.tugasbesarapmob.helpers.MappingHelperSaving
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_saving_main.*
import kotlinx.android.synthetic.main.activity_saving_main.progressBar2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class SavingMain : AppCompatActivity() {

    private lateinit var adapter: TabunganAdapter
    private lateinit var savingHelper: TabunganHelper

    var total_masuk:Int = 0

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saving_main)

        rvTabungan.layoutManager = LinearLayoutManager(this)
        rvTabungan.setHasFixedSize(true)
        adapter = TabunganAdapter(this)
        rvTabungan.adapter = adapter

        btnTambahMasuk.setOnClickListener {
            val intent = Intent(this@SavingMain, SavingAddUpdate::class.java)
            startActivityForResult(intent, SavingAddUpdate.REQUEST_ADD)
        }

        savingHelper = TabunganHelper.getInstance(applicationContext)
        savingHelper.open()

        if (savedInstanceState == null) {
            loadNotesAsync()
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            total_masuk = savingHelper.getMasuk()
            saldo_tabungan.text = format.format(total_masuk)

        } else {
            val list = savedInstanceState.getParcelableArrayList<Saving>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar2.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {

                val cursor = savingHelper.queryAllS()

                MappingHelperSaving.mapCursorToArrayList(cursor)

            }
            progressBar2.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
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
                SavingAddUpdate.REQUEST_ADD -> if (resultCode == SavingAddUpdate.RESULT_ADD) {
                    val saving = data.getParcelableExtra<Saving>(SavingAddUpdate.EXTRA_NOTE)

                    if (saving != null) {

                        adapter.addItem(saving)
                    }
                    rvTabungan.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackbarMessage("Satu Item Berhasil Ditambahkan")
                }
                SavingAddUpdate.REQUEST_UPDATE ->
                    when (resultCode) {
                        SavingAddUpdate.RESULT_UPDATE -> {

                            val note = data.getParcelableExtra<Saving>(SavingAddUpdate.EXTRA_NOTE)
                            val position = data.getIntExtra(SavingAddUpdate.EXTRA_POSITION, 0)

                            if (note != null) {
                                adapter.updateItem(position, note)
                            }
                            rvTabungan.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu item berhasil diubah")
                        }

                        SavingAddUpdate.RESULT_DELETE -> {
                            val position = data.getIntExtra(SavingAddUpdate.EXTRA_POSITION, 0)

                            adapter.removeItem(position)

                            showSnackbarMessage("Satu Item Berhasil Dihapus")
                        }
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        savingHelper.close()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rvTabungan, message, Snackbar.LENGTH_SHORT).show()
    }
}