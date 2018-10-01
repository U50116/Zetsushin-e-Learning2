package com.example.enpit_p33.zetsushinleaning

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.realm.OrderedCollectionChangeSet
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*
import android.widget.RadioButton
import java.util.concurrent.CopyOnWriteArrayList


class BetaActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val realmConfig = RealmConfiguration.Builder()
                .name("zetsushinleaning.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .build()
        realm = Realm.getInstance(realmConfig)
        //レイアウト設定
        val scrollView = ScrollView(this)
        setContentView(scrollView)
        val relativeLayout = RelativeLayout(this)
        scrollView.addView(relativeLayout)
        question(relativeLayout)
    }


    @SuppressLint("ResourceType")
    fun question(relativeLayout: RelativeLayout) {
        val ans:MutableList<String> = mutableListOf("","","","","","","","","","","","","","","","","","","","")
        val q:MutableList<Long> = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        val color:List<String> = listOf("紅舌","淡紅舌","淡白舌","紫舌")

        val WC = ViewGroup.LayoutParams.WRAP_CONTENT
        val Id = (realm.where(Question::class.java).max("question_id")?.toInt() ?: 0)
        val n = realm.where(Question::class.java).equalTo("question_id", Id).findAll()[0]?.questions!!
        val x = Array(n.size, { i -> i }).toList()
        Collections.shuffle(x)

        for (num in x.indices) {
            //画像レイアウト
            val image_n = (n[x[num]]?.image_number ?: 0)
            q[num] = image_n
            val r = resources.getIdentifier("q" + image_n + "_image", "drawable", packageName) //drawableの画像指定
            val imageView = ImageView(this)
            imageView.setImageResource(r) //imageViewに画像設定
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) //エラー回避
            imageView.id = 1 + num * 2
            val param1 = RelativeLayout.LayoutParams(740, 700)
            if (num == 0) {
                param1.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                param1.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                param1.setMargins(-50, 150, 0, 0)
            } else {
                param1.addRule(RelativeLayout.BELOW, 1 + (num - 1) * 2)
                param1.addRule(RelativeLayout.ALIGN_LEFT, 1 + (num - 1) * 2)
                param1.setMargins(0, 150, 0, 0)
            }

            relativeLayout.addView(imageView, param1)

            val radioGroup = RadioGroup(this)
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioGroup.id = 2 + num * 2

            val radioButton1 = RadioButton(this)
            val radioButton2 = RadioButton(this)
            val radioButton3 = RadioButton(this)
            val radioButton4 = RadioButton(this)
            Collections.shuffle(color)

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton1.id = 1000 + num * 4
            radioButton1.text = color[0]
            radioButton1.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton2.id = 1001 + num * 4
            radioButton2.text = color[1]
            radioButton2.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton3.id = 1002 + num * 4
            radioButton3.text = color[2]
            radioButton3.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton4.id = 1003 + num * 4
            radioButton4.text = color[3]
            radioButton4.textSize = 32.0f
            radioGroup.addView(radioButton1)
            radioGroup.addView(radioButton2)
            radioGroup.addView(radioButton3)
            radioGroup.addView(radioButton4)
            val param2 = RelativeLayout.LayoutParams(WC, WC)
            param2.addRule(RelativeLayout.RIGHT_OF, 1 + num * 2)
            param2.addRule(RelativeLayout.ALIGN_TOP, 1 + num * 2)
            param2.setMargins(0, 50, 0, 0)
            relativeLayout.addView(radioGroup, param2)

            radioGroup.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    val radio = findViewById(checkedId) as RadioButton
                    if(radio.isChecked() == true){
                        ans[num] = radio.text.toString()
                        Log.d("debug", ans[num])
                    }else{}
                }
            })
        }

        val button = Button(this)
        button.id = 10000
        val param = RelativeLayout.LayoutParams(500, 200)
        button.text = "次へ"
        button.textSize = 32.0f
        param.addRule(RelativeLayout.BELOW, 39)
        param.setMargins(500, 300, 0, 100)
        relativeLayout.addView(button, param)

        button.setOnClickListener{onButtonTapped(ans, q)}
    }

    fun onButtonTapped(ans:MutableList<String>, q:MutableList<Long>){
        realm.executeTransaction{
            val maxId = realm.where(History::class.java).max("history_id")
            val nextId = (maxId?.toLong() ?: 0L) + 1
            realm.createObject<History>(nextId).apply {
                val maxId1 = realm.where(Question::class.java).max("question_id") // 後で
                question_id = (maxId1?.toLong() ?: 0L)
                for(i in ans.indices){
                    val a = realm.createObject<Result>().apply {
                        answer = ans[i]
                    }
                    result.add(a)
                }
                date = "10/1" // 後で
            }
            Log.d("debug", realm.where(QuestionList::class.java).findAll().size.toString())
        }

        val intent = Intent(this, ConfusionActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}