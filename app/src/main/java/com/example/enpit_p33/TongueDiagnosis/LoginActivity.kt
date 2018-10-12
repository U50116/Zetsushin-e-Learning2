package com.example.enpit_p33.TongueDiagnosis

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class LoginActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val realmConfig = RealmConfiguration.Builder()
                .name("zetsushinleaning.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .build()
        realm = Realm.getInstance(realmConfig)
        button.setOnClickListener{ onNextButtonTapped()}
        new_button.setOnClickListener{ onCreateUserID()}
    }

    fun onNextButtonTapped(){
        val intent = Intent(this, MainActivity::class.java)

        val id = user_id.text.toString()
        val pass = user_password.text.toString()

        val user_account = realm.where(Zetsushin::class.java).equalTo("user_id", id).findFirst() //idが一致したインデックスを取得

        try{
            if (user_account != null) {
                realm.executeTransaction{
                    if (pass.equals(user_account.password)) {
                        intent.putExtra("ID", id)

                        startActivity(intent)
                    } else {
                        alert("パスワードが違います") {yesButton {  }}.show()
                    }}
            } else {
                alert("IDが違います") {yesButton {  }}.show()
            }
        }catch (e: IllegalStateException){
            alert("エラーが起きました"){yesButton {  }}.show()
        }finally{

        }
    }

    fun onCreateUserID(){
        val intent = Intent(this, CreateActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy(){
        super.onDestroy()
        realm.close()
    }
}
