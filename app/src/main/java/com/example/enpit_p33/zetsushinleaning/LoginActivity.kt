package com.example.enpit_p33.zetsushinleaning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class LoginActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        realm = Realm.getDefaultInstance()
        button.setOnClickListener{ onNextButtonTapped()}
        new_button.setOnClickListener{ onCreateUserID()}
    }

    fun onNextButtonTapped(){
        val intent = Intent(this, AlphaActivity::class.java)

        val id = user_id.text.toString()
        val pass = user_password.text.toString()

        val user_account = realm.where(Zetsushin::class.java).equalTo("user_id", id).findFirst() //idが一致したインデックスを取得

        try{
            if (user_account != null) {
                realm.executeTransaction{
                    if (pass.equals(user_account.password)) {
                        intent.putExtra("ID", id)
                        intent.putExtra("PASSWORD", pass)

                        startActivity(intent)
                    } else {
                        alert("IDかパスワードが違います") {yesButton {  }}.show()
                    }}
            } else {
                alert("IDかパスワードが違います") {yesButton {  }}.show()
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
