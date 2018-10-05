package com.example.enpit_p33.zetsushinleaning

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
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

        val scrollView = ScrollView(this)
        setContentView(scrollView)
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        scrollView.addView(linearLayout)

        createQuestion(linearLayout)
    }

    @SuppressLint("ResourceType")
    fun createQuestion(linearLayout: LinearLayout) {
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

        val param = LinearLayout.LayoutParams(WC, WC)
        val back = GradientDrawable()
        back.setStroke(3, Color.BLACK)

        val inlinearLayout_1 = LinearLayout(this)
        inlinearLayout_1.orientation = LinearLayout.HORIZONTAL

        val title = TextView(this)
        title.text = "テスト問題2"
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
        title.textSize = 64.0f
        if (title.parent != null) {
            ((title.parent) as ViewGroup).removeView(title)
        }
        inlinearLayout_1.addView(title, param)
        linearLayout.addView(inlinearLayout_1, param)

        for (num in Array(Q_SIZE, { i -> i })) {

            val inlinearLayout_2 = LinearLayout(this)
            inlinearLayout_2.orientation = LinearLayout.HORIZONTAL

            val text = TextView(this)
            text.text = "問" + (num+1).toString()
            text.textSize = 32.0f
            inlinearLayout_2.addView(text, param)

            val inlinearLayout_3 = LinearLayout(this)
            inlinearLayout_3.orientation = LinearLayout.HORIZONTAL

            val r_1 = resources.getIdentifier("q" + q_list!![num]?.image_number + "_image", "drawable", packageName) //drawableの画像指定
            val imageView_1 = ImageView(this)
            imageView_1.setImageResource(r_1) //imageViewに画像設定
            imageView_1.setPadding(0,50,200,50)
            inlinearLayout_3.addView(imageView_1, param)

            val radioGroup = RadioGroup(this)
            val radioButton1 = RadioButton(this)
            val radioButton2 = RadioButton(this)
            val radioButton3 = RadioButton(this)
            val radioButton4 = RadioButton(this)
            radioButton1.text = q_list[num]?.choice1
            radioButton1.textSize = 32.0f
            radioButton2.text = q_list[num]?.choice2
            radioButton2.textSize = 32.0f
            radioButton3.text = q_list[num]?.choice3
            radioButton3.textSize = 32.0f
            radioButton4.text = q_list[num]?.choice4
            radioButton4.textSize = 32.0f
            radioGroup.addView(radioButton1, param)
            radioGroup.addView(radioButton2, param)
            radioGroup.addView(radioButton3, param)
            radioGroup.addView(radioButton4, param)
            radioGroup.setPadding(0,50,0,50)
            inlinearLayout_3.addView(radioGroup, param)
            // ラジオボタンタップ時
            radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    val radio = findViewById(checkedId) as RadioButton
                    if (radio.isChecked() == true)
                        answerlist[num] = radio.text.toString()
                    Log.d("debug", answerlist[num])
                }
            })

            linearLayout.addView(inlinearLayout_2, param)
            linearLayout.addView(inlinearLayout_3, param)
        }

        val inlinearLayout_4 = LinearLayout(this)
        inlinearLayout_4.orientation = LinearLayout.HORIZONTAL

        val space_1 = Space(this)
        inlinearLayout_4.addView(space_1, LinearLayout.LayoutParams(500, 100))

        val button = Button(this)
        button.text = "次へ"
        button.textSize = 32.0f
        button.gravity = Gravity.CENTER
        inlinearLayout_4.addView(button, LinearLayout.LayoutParams(300, 100))


        val inlinearLayout_5 = LinearLayout(this)
        inlinearLayout_5.orientation = LinearLayout.HORIZONTAL

        val space_2 = Space(this)
        inlinearLayout_5.addView(space_2, LinearLayout.LayoutParams(100, 200))

        // ボタンタップ時
        button.setOnClickListener { onButtonTapped(Id) }
        linearLayout.addView(inlinearLayout_4, param)
        linearLayout.addView(inlinearLayout_5, param)
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

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.getAction() === KeyEvent.ACTION_DOWN) {
            when (event.getKeyCode()) {
                KeyEvent.KEYCODE_BACK ->
                    return true
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}