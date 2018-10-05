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
import kotlinx.android.synthetic.main.activity_result.*
import kotlin.jvm.java

class ResultActivity : AppCompatActivity() {
    private lateinit var realm: Realm
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
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        scrollView.addView(linearLayout)

        resultQuestion(linearLayout)
    }

    @SuppressLint("ResourceType")
    fun resultQuestion(linearLayout: LinearLayout) {
        var Id: Int // 使用するテスト問題のID
        var q_list: RealmList<QuestionList>?

        var flag:MutableList<Int> = mutableListOf()
        var miss_a:MutableList<Long> = mutableListOf()
        var miss_b:MutableList<Long> = mutableListOf()
        var miss: MutableList<Long> = mutableListOf()
        var miss_ans_a: MutableList<String> = mutableListOf()
        var miss_ans_b: MutableList<String> = mutableListOf()
        var miss_ans: MutableList<String> = mutableListOf()

        val a = intent.getIntExtra("ALPHA", 0)
        val b = intent.getIntExtra("BETA", 0)

        Log.d("debug", a.toString() +"and"+b.toString())

        // テスト問題の指定
        Id = intent.getIntExtra("ALPHA", 0)
        q_list = realm.where(Question::class.java).equalTo("question_id", Id).findAll()[0]?.questions

        val param = LinearLayout.LayoutParams(WC, WC)
        val back = GradientDrawable()
        back.setStroke(3, Color.BLACK)

        val inlinearLayout_1 = LinearLayout(this)
        inlinearLayout_1.orientation = LinearLayout.HORIZONTAL

        val title = TextView(this)
        title.text = "テスト結果"
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
        title.textSize = 64.0f
        if (title.parent != null) {
            ((title.parent) as ViewGroup).removeView(title)
        }
        inlinearLayout_1.addView(title, param)
        linearLayout.addView(inlinearLayout_1, param)


        for(num in Array(Q_SIZE, { i -> i })){
            val im_n_a = realm.where(Question::class.java).equalTo("question_id", a).findAll()[0]?.questions!![num]?.image_number
            val im_n_b = realm.where(Question::class.java).equalTo("question_id", b).findAll()[0]?.questions!![num]?.image_number
            val f_a = !realm.where(ZetsuImage::class.java).equalTo("image_id", im_n_a).findAll()[0]?.zetsu_color.equals(realm.where(History::class.java).equalTo("question_id", a).findAll()[0]?.result!![num]?.answer)

            val inlinearLayout_2 = LinearLayout(this)
            inlinearLayout_2.orientation = LinearLayout.HORIZONTAL

            val q_number = TextView(this)
            q_number.text = "問" + (num+1).toString()
            q_number.textSize = 32.0f
            inlinearLayout_2.addView(q_number, param)

            val inlinearLayout_3 = LinearLayout(this)
            inlinearLayout_3.orientation = LinearLayout.HORIZONTAL

            val r_1 = resources.getIdentifier("q" + q_list!![num]?.image_number + "_image", "drawable", packageName) //drawableの画像指定
            val imageView_1 = ImageView(this)
            imageView_1.setImageResource(r_1) //imageViewに画像設定
            imageView_1.setPadding(0,50,200,50)
            inlinearLayout_3.addView(imageView_1, param)

            val inlinearLayout_4 = LinearLayout(this)
            inlinearLayout_4.orientation = LinearLayout.VERTICAL

            // テキスト
            val text_1 = TextView(this)
            text_1.text = "テスト問題1の選択\n" + realm.where(History::class.java).equalTo("question_id", a).findAll()[0]?.result!![num]?.answer
            text_1.textSize = 32.0f
            if(f_a){
                text_1.setTextColor(Color.RED)
                miss_a.add((im_n_a ?: 0))
                miss_ans_a.add((realm.where(History::class.java).equalTo("question_id", a).findAll()[0]?.result!![num]?.answer ?: ""))
            }
            inlinearLayout_4.addView(text_1, param)

            val text_2 = TextView(this)
            text_2.textSize = 32.0f
            val q = realm.where(Question::class.java).equalTo("question_id", b).findAll()[0]!!.questions
            for (i in Array(q.size, { j -> j })) {
                Log.d("debug", q[i]!!.question_number.toString())
                if (q[i]!!.question_number == q_list[num]?.question_number) {
                    Log.d("debug", q[i]!!.question_number.toString()) // テスト問題1の番号
                    Log.d("debug", "2の問題:" + realm.where(History::class.java).equalTo("question_id", b).findAll()[0]?.result!![i]?.answer) // テスト問題1の番号
                    text_2.text = "テスト問題2の選択\n" + realm.where(History::class.java).equalTo("question_id", b).findAll()[0]!!.result[i]?.answer
                    val f_b = !realm.where(ZetsuImage::class.java).equalTo("image_id", im_n_a).findAll()[0]?.zetsu_color.equals(realm.where(History::class.java).equalTo("question_id", b).findAll()[0]?.result!![i]?.answer)
                    if(f_b){
                        text_2.setTextColor(Color.RED)
                        miss_b.add((im_n_a ?: 0))
                        miss_ans_b.add((realm.where(History::class.java).equalTo("question_id", b).findAll()[0]!!.result[i]?.answer ?: ""))
                    }else{
                    }
                }
            }
            inlinearLayout_4.addView(text_2, param)

            val text_3 = TextView(this)
            text_3.text = "この舌画像の正解\n" + realm.where(ZetsuImage::class.java).equalTo("image_id", im_n_a).findAll()[0]?.zetsu_color
            text_3.textSize = 32.0f
            inlinearLayout_4.addView(text_3)

            inlinearLayout_3.addView(inlinearLayout_4, param)

            linearLayout.addView(inlinearLayout_2, param)
            linearLayout.addView(inlinearLayout_3, param)
        }

        val inlinearLayout_5 = LinearLayout(this)
        inlinearLayout_5.orientation = LinearLayout.HORIZONTAL

        val space_1 = Space(this)
        inlinearLayout_5.addView(space_1, LinearLayout.LayoutParams(500, 100))

        val button = Button(this)
        button.text = "次へ"
        button.textSize = 32.0f
        button.gravity = Gravity.CENTER
        inlinearLayout_5.addView(button, LinearLayout.LayoutParams(300, 100))

        val inlinearLayout_6 = LinearLayout(this)
        inlinearLayout_6.orientation = LinearLayout.HORIZONTAL

        val space_2 = Space(this)
        inlinearLayout_6.addView(space_2, LinearLayout.LayoutParams(100, 200))

        linearLayout.addView(inlinearLayout_5, param)
        linearLayout.addView(inlinearLayout_6, param)

        Log.d("debug", "ミスサイズ:" + miss_a.toString() + "  " + miss_b.toString())
        if(miss_a.size or miss_b.size != 0) {
            for (i in Array(miss_a.size, { i -> i })) {
                for (j in Array(miss_b.size, { i -> i })) {
                    if (miss_a[i] == miss_b[j]) {
                        if(miss_ans_a[i] == miss_ans_b[j]){
                            miss_ans.add(miss_ans_a[i])
                            miss.add(miss_a[i])
                        }
                    }
                }
            }
        }else{
            miss.add(0)
            miss_ans.add("")
        }
        // ボタンタップ時
        button.setOnClickListener { onButtonTapped(miss, miss_ans) }

    }

    fun onButtonTapped(miss:MutableList<Long>, miss_ans:MutableList<String>){
        val intent = Intent(this, CommentActivity::class.java)
        Log.d("debug", "ミス:" + miss.toString() + "ミス:" + miss_ans.toString() )
        intent.putExtra("MISS", miss.toLongArray())
        intent.putExtra("MISS_ANS", miss_ans.toTypedArray())
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