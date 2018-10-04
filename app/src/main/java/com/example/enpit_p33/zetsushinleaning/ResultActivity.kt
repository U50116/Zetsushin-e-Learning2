package com.example.enpit_p33.zetsushinleaning

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
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
        val relativeLayout = RelativeLayout(this)
        scrollView.addView(relativeLayout)

        resultQuestion(relativeLayout)
    }

    @SuppressLint("ResourceType")
    fun resultQuestion(relativeLayout: RelativeLayout) {
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


        for(num in Array(Q_SIZE, { i -> i })){
            val im_n_a = realm.where(Question::class.java).equalTo("question_id", a).findAll()[0]?.questions!![num]?.image_number
            val im_n_b = realm.where(Question::class.java).equalTo("question_id", b).findAll()[0]?.questions!![num]?.image_number
            val f_a = !realm.where(ZetsuImage::class.java).equalTo("image_id", im_n_a).findAll()[0]?.zetsu_color.equals(realm.where(History::class.java).equalTo("question_id", a).findAll()[0]?.result!![num]?.answer)
            val f_b = !realm.where(ZetsuImage::class.java).equalTo("image_id", im_n_b).findAll()[0]?.zetsu_color.equals(realm.where(History::class.java).equalTo("question_id", b).findAll()[0]!!.result[num]?.answer)

            // 画像作成
            val r = resources.getIdentifier("q" + q_list!![num]?.image_number + "_image", "drawable", packageName) //drawableの画像指定
            Log.d("debug", r.toString())
            val imageView = ImageView(this)
            imageView.setImageResource(r) //imageViewに画像設定
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) //エラー回避
            imageView.id = 1 + num
            val param1 = RelativeLayout.LayoutParams(WC, WC)
            if (num == 0) {
                param1.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                param1.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                param1.setMargins(50, 350, 0, 0)
            } else {
                param1.addRule(RelativeLayout.BELOW, 1 + (num - 1))
                param1.addRule(RelativeLayout.ALIGN_LEFT, 1 + (num - 1))
                param1.setMargins(0, 250, 0, 0)
            }
            relativeLayout.addView(imageView, param1)

            val text = TextView(this)
            text.id = 100
            val param = RelativeLayout.LayoutParams(WC, WC)
            text.text = "問" + (num+1).toString()
            text.textSize = 32.0f
            param.addRule(RelativeLayout.ABOVE, 1 + num)
            param.addRule(RelativeLayout.ALIGN_LEFT, 1 + num)
            param.setMargins(100, 0, 0, -250)
            relativeLayout.addView(text, param)

            // テキスト
            val text1 = TextView(this)
            text1.id = 1000 + num * 3
            text1.text = "テスト問題1\n" + realm.where(History::class.java).equalTo("question_id", a).findAll()[0]?.result!![num]?.answer
            text1.textSize = 32.0f
            if(f_a){
                text1.setTextColor(Color.RED)
                miss_a.add((im_n_a ?: 0))
                miss_ans_a.add((realm.where(History::class.java).equalTo("question_id", a).findAll()[0]?.result!![num]?.answer ?: ""))
            }else{
            }
            val param2 = RelativeLayout.LayoutParams(WC, WC)
            param2.addRule(RelativeLayout.RIGHT_OF, 1 + num)
            param2.addRule(RelativeLayout.ALIGN_TOP, 1 + num)
            param2.setMargins(100, 50, 0, 0)
            relativeLayout.addView(text1, param2)

            val text2 = TextView(this)
            text2.id = 1001 + num * 3
            val q = realm.where(Question::class.java).equalTo("question_id", b).findAll()[0]!!.questions
            for (i in Array(q.size, { j -> j })) {
                Log.d("debug", q[i]!!.question_number.toString())
                if (q[i]!!.question_number == q_list[num]?.question_number) {
                    Log.d("debug", q[i]!!.question_number.toString())
                    text2.text = "テスト問題2\n" + realm.where(History::class.java).equalTo("question_id", b).findAll()[0]!!.result[i]?.answer
                    if(f_b){
                        text2.setTextColor(Color.RED)
                        miss_b.add((im_n_b ?: 0))
                        miss_ans_b.add((realm.where(History::class.java).equalTo("question_id", b).findAll()[0]!!.result[i]?.answer ?: ""))
                    }else{
                    }
                }
            }
            text2.textSize = 32.0f

            val param3 = RelativeLayout.LayoutParams(WC, WC)
            param3.addRule(RelativeLayout.BELOW, 1000 + num * 3)
            param3.addRule(RelativeLayout.ALIGN_LEFT, 1000 + num * 3)
            param3.setMargins(0, 50, 0, 0)
            relativeLayout.addView(text2, param3)

            val text3 = TextView(this)
            text3.id = 1002 + num * 3
            text3.text = "テスト問題正解\n" + realm.where(ZetsuImage::class.java).equalTo("image_id", im_n_a).findAll()[0]?.zetsu_color
            text3.textSize = 32.0f
            Log.d("debug", realm.where(ZetsuImage::class.java).equalTo("image_id", im_n_a).findAll()[0]?.zetsu_color)
            val param4 = RelativeLayout.LayoutParams(WC, WC)
            param4.addRule(RelativeLayout.BELOW, 1001 + num * 3)
            param4.addRule(RelativeLayout.ALIGN_LEFT, 1000 + num * 3)
            param4.setMargins(0, 50, 0, 0)
            relativeLayout.addView(text3, param4)
        }
        val title = TextView(this)
        title.id = 10001
        title.text = "確信的誤り指摘"
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
        title.textSize = 64.0f
        val param = RelativeLayout.LayoutParams(WC, WC)
        param.addRule(RelativeLayout.CENTER_HORIZONTAL)
        relativeLayout.addView(title, param)

        // ボタン作成
        val button = Button(this)
        button.id = 10000
        val param5 = RelativeLayout.LayoutParams(500, 200)
        button.text = "次へ"
        button.textSize = 32.0f
        param5.addRule(RelativeLayout.BELOW, Q_SIZE)
        param5.setMargins(500, 300, 0, 100)
        relativeLayout.addView(button, param5)

        if(miss_a.size or miss_b.size != 0) {
            for (i in Array(miss_b.size, { i -> i })) {
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

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}