package com.example.tugasbesarapmob.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tugasbesarapmob.MainActivity
import com.example.tugasbesarapmob.R
import kotlinx.android.synthetic.main.creator.*

class CreatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creator)

        btnback.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "You Will Back To Main Menu", Toast.LENGTH_LONG).show()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
    }
}