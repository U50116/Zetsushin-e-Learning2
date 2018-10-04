package com.example.enpit_p33.zetsushinleaning

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import java.util.*
import android.widget.RadioButton
import io.realm.RealmList

class BetaActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private var answerlist:MutableList<String> = mutableListOf("","","","","","","","","","") //ユーザーのリアルタイム解答
    private val colorlist:List<String> = listOf("紅舌","淡紅舌","淡白舌","紫舌") // 選択肢リスト
    private val WC = ViewGroup.LayoutParams.WRAP_CONTENT
    private val Q_SIZE = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //realm初期化
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

        createQuestion(relativeLayout)
    }

    @SuppressLint("ResourceType")
    fun createQuestion(relativeLayout: RelativeLayout) {
        var Id: Int = 0 // 使用するテスト問題のID
        var q_list: RealmList<QuestionList>?
        val mylist: List<Long> = listOf(21,16,19,3,1,5,14,7,9,6)
        val myans: List<String> = listOf("紫舌", "紅舌", "淡紅舌", "淡白舌",
                                         "淡白舌", "淡紅舌", "紅舌", "紫舌",
                                         "淡紅舌", "淡白舌", "紫舌", "紅舌",
                                         "紫舌", "淡白舌", "紅舌", "淡紅舌",
                                         "淡紅舌", "紅舌", "淡白舌", "紫舌",
                                         "淡白舌", "淡紅舌", "紅舌", "紫舌",
                                         "淡紅舌", "淡白舌", "紫舌", "紅舌",
                                         "紅舌", "淡白舌", "紫舌", "淡紅舌",
                                         "紫舌", "淡白舌", "紅舌", "淡紅舌",
                                         "淡紅舌", "紫舌", "淡白舌", "紅舌")

        // テスト問題の指定
        Id = myQuestion(mylist, myans)// randomQuestion(intent.getIntExtra("ALPHA", 0))
        q_list = realm.where(Question::class.java).equalTo("question_id", Id).findAll()[0]?.questions

        for (num in Array(Q_SIZE, { i -> i })) {
            //画像作成
            val r = resources.getIdentifier("q" + q_list!![num]?.image_number + "_image", "drawable", packageName) //drawableの画像指定
            val imageView = ImageView(this)
            imageView.setImageResource(r) //imageViewに画像設定
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) //エラー回避
            imageView.id = 1 + num * 3
            val param1 = RelativeLayout.LayoutParams(WC, WC)
            if (num == 0) {
                param1.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                param1.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                param1.setMargins(50, 350, 0, 0)
            } else {
                param1.addRule(RelativeLayout.BELOW, 1 + (num - 1) * 3)
                param1.addRule(RelativeLayout.ALIGN_LEFT, 1 + (num - 1) * 3)
                param1.setMargins(0, 250, 0, 0)
            }
            relativeLayout.addView(imageView, param1)


            // ラジオ作成
            val radioGroup = RadioGroup(this)
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioGroup.id = 2 + num * 3
            val radioButton1 = RadioButton(this)
            val radioButton2 = RadioButton(this)
            val radioButton3 = RadioButton(this)
            val radioButton4 = RadioButton(this)
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton1.id = 1000 + num * 4
            radioButton1.text = q_list[num]?.choice1
            radioButton1.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton2.id = 1001 + num * 4
            radioButton2.text = q_list[num]?.choice2
            radioButton2.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton3.id = 1002 + num * 4
            radioButton3.text = q_list[num]?.choice3
            radioButton3.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton4.id = 1003 + num * 4
            radioButton4.text = q_list[num]?.choice4
            radioButton4.textSize = 32.0f
            val param3 = RelativeLayout.LayoutParams(WC, WC)
            param3.setMargins(0, 50, 0, 0)
            radioGroup.addView(radioButton1, param3)
            radioGroup.addView(radioButton2, param3)
            radioGroup.addView(radioButton3, param3)
            radioGroup.addView(radioButton4, param3)
            val param2 = RelativeLayout.LayoutParams(WC, WC)
            param2.addRule(RelativeLayout.RIGHT_OF, 1 + num * 3)
            param2.addRule(RelativeLayout.ALIGN_TOP, 1 + num * 3)
            param2.setMargins(100, 50, 0, 0)
            relativeLayout.addView(radioGroup, param2)
            // ラジオボタンタップ時
            radioGroup.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    val radio = findViewById(checkedId) as RadioButton
                    if(radio.isChecked() == true){
                        answerlist[num] = radio.text.toString()
                        Log.d("debug", answerlist[num])
                    }
                }
            })
            val text = TextView(this)
            text.id = 3 + num * 3
            val param4 = RelativeLayout.LayoutParams(WC, WC)
            text.text = "問" + (num+1).toString()
            text.textSize = 32.0f
            param4.addRule(RelativeLayout.ABOVE, 1 + num * 3)
            param4.addRule(RelativeLayout.ALIGN_LEFT, 1 + num * 3)
            param4.setMargins(50, 0, 0, -250)
            relativeLayout.addView(text, param4)
        }

        val title = TextView(this)
        title.id = 10001
        title.text = "テスト問題2"
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
        title.textSize = 64.0f
        val param = RelativeLayout.LayoutParams(WC, WC)
        param.addRule(RelativeLayout.CENTER_HORIZONTAL)
        relativeLayout.addView(title, param)

        // ボタン作成
        val button = Button(this)
        button.id = 10000
        val param3 = RelativeLayout.LayoutParams(500, 200)
        button.text = "次へ"
        button.textSize = 32.0f
        param3.addRule(RelativeLayout.BELOW, Q_SIZE * 3 - 2)
        param3.setMargins(500, 300, 0, 100)
        relativeLayout.addView(button, param3)
        // ボタンタップ時
        button.setOnClickListener{onButtonTapped(Id)}
    }

    fun randomQuestion(Id: Int): Int {
        // テスト問題の一番大きいIDの次の数字を取得
        val maxId = realm.where(Question::class.java).max("question_id")
        val nextId = (maxId?.toInt() ?: 0) + 1

        //新しい問題を生成
        realm.executeTransaction {
            val tmp = realm.where(Question::class.java).equalTo("question_id", Id).findAll()[0]?.questions!!
            val n = Array(tmp.size, {i -> i}).toList()

            Collections.shuffle(n)
            realm.createObject<Question>(nextId).apply {
                for (num in Array(tmp.size, { i -> i })) {
                    Collections.shuffle(colorlist)
                    val q = realm.createObject<QuestionList>().apply {
                        question_number = (tmp[n[num]]?.question_number ?: 0)
                        image_number = (tmp[n[num]]?.image_number ?: 0)
                        choice1 = colorlist[0]
                        choice2 = colorlist[1]
                        choice3 = colorlist[2]
                        choice4 = colorlist[3]

                        Log.d("debug", "テスト問題" + question_number.toString() + ":" + image_number.toString())
                    }
                    questions.add(q)
                }
            }
        }
        return nextId
    }

    fun myQuestion(mylist: List<Long>, myans: List<String>): Int {
        // テスト問題の一番大きいIDの次の数字を取得
        val maxId = realm.where(Question::class.java).max("question_id")
        val nextId = (maxId?.toInt() ?: 0) + 1

        val number = listOf(10,5,6,3,1,2,8,7,4,9) // 緊急用　後で変更

        //新しい問題を生成
        realm.executeTransaction {
            realm.createObject<Question>(nextId).apply {
                for (num in Array(Q_SIZE, { i -> i })) {
                    val q = realm.createObject<QuestionList>().apply {
                        question_number = number[num] // 緊急用　後で変更
                        image_number = mylist[num]
                        choice1 = myans[0 + num * 4]
                        choice2 = myans[1 + num * 4]
                        choice3 = myans[2 + num * 4]
                        choice4 = myans[3 + num * 4]

                        Log.d("debug", "テスト問題" + question_number.toString() + ":" + image_number.toString())
                    }
                    questions.add(q)
                }
            }
        }
        return nextId
    }

    fun onButtonTapped(Id: Int) {
        val nextId = (realm.where(History::class.java).max("history_id")?.toLong() ?: 0L) + 1
        val user = intent.getStringExtra("ID")
        val alpha = intent.getIntExtra("ALPHA", 0)
        val intent = Intent(this, ReConfusionActivity::class.java)
        intent.putExtra("BETA", Id)
        intent.putExtra("ID", user)
        intent.putExtra("ALPHA", alpha)
        realm.executeTransaction {
            realm.createObject<History>(nextId).apply {
                user_id = intent.getStringExtra("ID")
                question_id = Id
                for (i in answerlist.indices) {
                    val a = realm.createObject<Result>().apply {
                        answer = answerlist[i]
                    }
                    result.add(a)
                }
                date = Date().toString()
            }
            Log.d("debug", realm.where(QuestionList::class.java).findAll().size.toString())
            Log.d("debug", "beta_id:" + intent.getIntExtra("BETA", 0).toString())
            Log.d("debug", "alpha_id:" + intent.getIntExtra("ALPHA", 0).toString())
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}