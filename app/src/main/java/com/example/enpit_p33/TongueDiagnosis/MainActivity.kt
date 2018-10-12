package com.example.enpit_p33.TongueDiagnosis

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val set_id = intent.getStringExtra("ID")

        Text.text = set_id
        startButton.setOnClickListener{onStartButtonTapped(set_id)}
    }

    fun onStartButtonTapped(set_id: String){
        val intent = Intent(this, AlphaActivity::class.java)
        intent.putExtra("ID", set_id)
        startActivity(intent)
    }
}
