package com.example.enpit_p33.TongueDiagnosis

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
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
import android.view.View
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

// テスト問題1を表示する
class AlphaActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private val answerlist:MutableList<String> = mutableListOf("","","","","","","","","","") //ユーザーのリアルタイム解答
    val WC = ViewGroup.LayoutParams.WRAP_CONTENT // viewのサイズを適当にする
    val Q_SIZE = 10 // 問題の数

    private val mylist= listOf("1_1", "2_2", "4_1", "2_4", "2_6", "1_3", "4_3", "3_1", "3_3", "2_8") // 指定した問題
    private val myans: List<String> = listOf("紅舌", "紫舌", "淡白舌", "淡紅舌",
                                            "淡紅舌", "淡白舌", "紫舌", "紅舌",
                                            "淡白舌", "淡紅舌", "紅舌", "紫舌",
                                            "淡白舌", "淡紅舌", "紫舌", "紅舌",
                                            "紫舌", "紅舌", "淡白舌", "淡紅舌",
                                            "淡紅舌", "淡白舌", "紅舌", "紫舌",
                                            "淡紅舌", "紅舌", "紫舌", "淡白舌",
                                            "紅舌", "淡紅舌", "淡白舌", "紫舌",
                                            "淡紅舌", "淡白舌", "紫舌", "紅舌",
                                            "紅舌", "淡紅舌", "淡白舌", "紫舌") // 指定した選択肢

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val realmConfig = RealmConfiguration.Builder()
                .name("zetsushinleaning.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .build()
        realm = Realm.getInstance(realmConfig) // realmのインスタンス作成 // データベース変更時、データ破棄

        var new_flag = false // 新しく問題を作成するかどうかのフラグ
        val history = (realm.where(History::class.java)
                        .equalTo("user_id", intent.getStringExtra("ID"))
                        .max("history_id")?.toInt() ?: 0) // ユーザーの解いた最後の履歴のデータベースの番号
        val existence = realm.where(History::class.java)
                                        .equalTo("history_id", history)
                                        .findFirst() // 存在する履歴

        //レイアウト設定
        val scrollView = ScrollView(this) // スクロールビューのインスタンス
        setContentView(scrollView) // スクロールビューをセット
        val linearLayout = LinearLayout(this) // リニアレイアウトのインスタンス
        linearLayout.orientation = LinearLayout.VERTICAL // ビューを縦に並べる
        scrollView.addView(linearLayout) // スクロールビューにリニアレイアウトをセット

        // 履歴がなかった時
        if(existence == null) {
            new_flag = true
        }
        createQuestion(linearLayout, new_flag, existence) // 問題の表示
    }

    @SuppressLint("ResourceType")
    fun createQuestion(linearLayout: LinearLayout, new_flag: Boolean, existence: History?) {
        var Id: Int // 使用するテスト問題のID
        var q_list: RealmList<QuestionList>? // 表示する問題のデータベース

        val param = LinearLayout.LayoutParams(WC, WC) // ビューの適当なサイズを指定
        val back = GradientDrawable() // バックグラウンドの指定
        back.setStroke(3, Color.BLACK) // 枠線

        // テスト問題の指定
        if (new_flag) {
            Id = myQuestion(mylist, myans) // 指定した問題と選択肢のリストからデータベースを作成しそのidを保存
            // IDから見つけた問題を保存
            q_list = realm.where(Question::class.java).equalTo("question_id", Id).findAll()[0]?.questions
        } else {
            Id = existence!!.question_id // 最後に解いた問題のID
            // IDから見つけた問題を保存
            q_list = realm.where(Question::class.java).equalTo("question_id", Id).findAll()[0]?.questions
        }

        // -----ここからレイアウトの設定------------------------------------------------------------
        // ----ここからレイアウト1の設定--------------------------------------
        val inlinearLayout_1 = LinearLayout(this)
        inlinearLayout_1.orientation = LinearLayout.VERTICAL

        // ---ここからタイトルの生成--------------------------------
        val title = TextView(this)
        title.text = "テスト問題1"
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC)) // 太文字斜め
        title.textSize = 64.0f
        // エラー回避
        if (title.parent != null) {
            ((title.parent) as ViewGroup).removeView(title)
        }
        inlinearLayout_1.addView(title, param)
        // ---ここまでタイトルの生成--------------------------------

        // ---ここから説明文の生成----------------------------------
        val question_statement = TextView(this)
        question_statement.text = "問いに示された舌画像を見て、4つの選択肢の中から正しいと思われる選択肢を1つ選んでください。"
        question_statement.textSize = 32.0f
        inlinearLayout_1.addView(question_statement, param)
        // ---ここまで説明文の生成----------------------------------

        // ----ここまでレイアウト1の設定--------------------------------------
        linearLayout.addView(inlinearLayout_1, param) // レイアウト1のビューを表示

        // ---ここから仕切り線の生成--------------------------------
        val separate_1 = View(this)
        separate_1.background = back
        linearLayout.addView(separate_1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5))
        // ---ここまで仕切り線の生成--------------------------------


        for (num in Array(Q_SIZE, { i -> i })) {
            // ----ここからレイアウト2の設定----------------------------------
            val inlinearLayout_2 = LinearLayout(this)
            inlinearLayout_2.orientation = LinearLayout.HORIZONTAL

            // ---ここから問番号の生成------------------------------
            val text_2_1 = TextView(this)
            text_2_1.text = "問" + (num+1).toString()
            text_2_1.textSize = 32.0f
            inlinearLayout_2.addView(text_2_1, param)
            // ---ここから問番号の生成------------------------------
            // ----ここまでレイアウト2の設定----------------------------------

            // ----ここからレイアウト3の設定----------------------------------
            val inlinearLayout_3 = LinearLayout(this)
            inlinearLayout_3.orientation = LinearLayout.HORIZONTAL

            // ---ここから問題画像の生成----------------------------
            // drawableから画像を指定
            val r_3_1 = resources.getIdentifier("q" + q_list!![num]?.image_number, "drawable", packageName)
            val imageView_3_1 = ImageView(this)
            imageView_3_1.setImageResource(r_3_1) //imageViewに画像設定
            imageView_3_1.setPadding(0,50,200,50)
            inlinearLayout_3.addView(imageView_3_1, param)
            // ---ここまで問題画像の生成----------------------------

            // ---ここから選択肢の生成------------------------------
            // ラジオグループの中にラジオボタンを保存
            val radioGroup_3_1 = RadioGroup(this)
            val radioButton_3_1 = RadioButton(this)
            val radioButton_3_2 = RadioButton(this)
            val radioButton_3_3 = RadioButton(this)
            val radioButton_3_4 = RadioButton(this)
            radioButton_3_1.text = q_list[num]?.choice1
            radioButton_3_1.textSize = 32.0f
            radioButton_3_2.text = q_list[num]?.choice2
            radioButton_3_2.textSize = 32.0f
            radioButton_3_3.text = q_list[num]?.choice3
            radioButton_3_3.textSize = 32.0f
            radioButton_3_4.text = q_list[num]?.choice4
            radioButton_3_4.textSize = 32.0f
            radioGroup_3_1.addView(radioButton_3_1, param)
            radioGroup_3_1.addView(radioButton_3_2, param)
            radioGroup_3_1.addView(radioButton_3_3, param)
            radioGroup_3_1.addView(radioButton_3_4, param)
            radioGroup_3_1.setPadding(0,50,0,50)
            inlinearLayout_3.addView(radioGroup_3_1, param)

            // ラジオボタンタップ時
            radioGroup_3_1.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    val radio = findViewById(checkedId) as RadioButton
                    if (radio.isChecked() == true)
                        answerlist[num] = radio.text.toString()
                    Log.d("debug", answerlist[num])
                }
            })
            // ---ここまで選択肢の生成------------------------------
            // ----ここまでレイアウト3の設定----------------------------------

            linearLayout.addView(inlinearLayout_2, param) // レイアウト2のビューを表示
            linearLayout.addView(inlinearLayout_3, param) // レイアウト3のビューを表示

            // ---ここから仕切り線の生成----------------------------
            val separate_2 = View(this)
            separate_2.background = back
            linearLayout.addView(separate_2, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5))
            // ---ここまで仕切り線の生成----------------------------
        }

        // ----ここからレイアウト4の設定--------------------------------------
        val inlinearLayout_4 = LinearLayout(this)
        inlinearLayout_4.orientation = LinearLayout.HORIZONTAL

        // ---ここから空白の生成------------------------------------
        val space_4_1 = Space(this)
        inlinearLayout_4.addView(space_4_1, LinearLayout.LayoutParams(500, 100))
        // ---ここまで空白の生成------------------------------------

        // ---ここから遷移するボタンの生成--------------------------
        val button = Button(this)
        button.text = "次へ"
        button.textSize = 32.0f
        button.gravity = Gravity.CENTER
        inlinearLayout_4.addView(button, LinearLayout.LayoutParams(300, 100))

        button.setOnClickListener { onButtonTapped(Id) } // "次へ"のボタンが押されたとき
        // ---ここまで遷移するボタンの生成--------------------------
        // ----ここまでレイアウト4の設定--------------------------------------

        // ----ここからレイアウト5の設定--------------------------------------
        val inlinearLayout_5 = LinearLayout(this)
        inlinearLayout_5.orientation = LinearLayout.HORIZONTAL

        // ---ここから空白の生成------------------------------------
        val space_5_1 = Space(this)
        inlinearLayout_5.addView(space_5_1, LinearLayout.LayoutParams(100, 100))
        // ---ここまで空白の生成------------------------------------
        // ----ここまでレイアウト5の設定--------------------------------------

        linearLayout.addView(inlinearLayout_4, param) // レイアウト4のビューを表示
        linearLayout.addView(inlinearLayout_5, param) // レイアウト5のビューを表示
        // -----ここまでレイアウトの設定------------------------------------------------------------
    }
/*
    fun newQuestion(): Int {
        // テスト問題の一番大きいIDの次の数字を取得
        val maxId = realm.where(Question::class.java).max("question_id")
        val nextId = (maxId?.toInt() ?: 0) + 1

        //新しい問題を生成
        realm.executeTransaction {
            realm.createObject<Question>(nextId).apply {
                for (num in Array(Q_SIZE, { i -> i })) {
                    val tmp = realm.where(ZetsuImage::class.java).equalTo("zetsu_color", colorlist[Random().nextInt(4)]).findAll()
                    val image_n = (tmp[Random().nextInt(tmp.size)]?.image_id ?: 0)
                    Collections.shuffle(colorlist)
                    val q = realm.createObject<QuestionList>().apply {
                        question_number = num + 1
                        image_number = image_n
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
*/
    fun myQuestion(mylist: List<String>, myans: List<String>): Int {
        // テスト問題の一番大きいIDの次の数字を取得
        val maxId = realm.where(Question::class.java).max("question_id")
        val nextId = (maxId?.toInt() ?: 0) + 1

        //新しい問題を生成
        realm.executeTransaction {
            realm.createObject<Question>(nextId).apply {
                for (num in Array(Q_SIZE, { i -> i })) {
                    val q = realm.createObject<QuestionList>().apply {
                        question_number = num + 1
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

    fun onButtonTapped(Id: Int){
        val nextId = (realm.where(History::class.java).max("history_id")?.toLong() ?: 0L) + 1
        val user = intent.getStringExtra("ID")
        val intent = Intent(this, ConfusionActivity::class.java)
        intent.putExtra("ALPHA", Id)
        intent.putExtra("ID", user)
        for(i in answerlist.indices) {
            if(answerlist[i].equals("")){
                alert("まだすべて解答していません。") { yesButton {} }.show()
                return
            }
        }
        realm.executeTransaction{
            realm.createObject<History>(nextId).apply {
                user_id = intent.getStringExtra("ID")
                question_id = Id
                for(i in answerlist.indices){
                    val a = realm.createObject<Result>().apply {
                        answer = answerlist[i]
                        Log.d("debug", "解答" + answerlist[i])
                    }
                    result.add(a)
                }
                date = Date().toString()
            }
        }
        Log.d("debug", realm.where(QuestionList::class.java).findAll().size.toString())
        startActivity(intent)
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