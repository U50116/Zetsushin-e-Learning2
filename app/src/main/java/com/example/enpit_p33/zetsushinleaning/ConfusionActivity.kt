package com.example.enpit_p33.zetsushinleaning

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import io.realm.Realm
import io.realm.RealmConfiguration

class ConfusionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confusion)
    }

    fun onButtonTapped(view: View?) {
        val intent = Intent(this, ReConfusionActivity::class.java)
        startActivity(intent)
    }
}
