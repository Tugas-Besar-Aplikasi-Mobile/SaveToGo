package com.example.tugasbesarapmob.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.tugasbesarapmob.R

class Dashboard : AppCompatActivity() {

    var names : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)



        var button_transaksi : ImageView = findViewById<ImageView>(R.id.button_transactions)
        var button_saving : ImageView = findViewById<ImageView>(R.id.button_saving)
        var button_help : ImageView = findViewById<ImageView>(R.id.button_notes)
        var button_kalkulasi : ImageView = findViewById<ImageView>(R.id.btn_kalkulasi)


        button_saving.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Anda Akan Dialihkan Ke Menu Tabungan", Toast.LENGTH_SHORT).show()
            var intent_transaksi1 = Intent(this, SavingMain::class.java)
            startActivity(intent_transaksi1)

        })

        button_transaksi.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Anda Akan Dialihkan Ke Menu Transaksi", Toast.LENGTH_SHORT).show()
            var intent_transaksi = Intent(this, TransactionMain::class.java)
            startActivity(intent_transaksi)

        })

        button_kalkulasi.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Anda Akan Dialihkan Ke Menu Kalkulasi Perhitungan Tabungan", Toast.LENGTH_SHORT).show()
            var intent_transaksi3 = Intent(this, PerhitunganMain::class.java)
            startActivity(intent_transaksi3)

        })

        button_help.setOnClickListener(View.OnClickListener {

            Toast.makeText(this, "Anda Akan Dialihkan Ke Menu Catatan", Toast.LENGTH_SHORT).show()
            var intent_transaksi2 = Intent(this, NoteActivity::class.java)
            startActivity(intent_transaksi2)

        })

    }

}