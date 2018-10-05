package com.example.enpit_p33.zetsushinleaning

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
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
import kotlinx.android.synthetic.main.activity_main.*

class CommentActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private val WC = ViewGroup.LayoutParams.WRAP_CONTENT
    val real = mapOf("紅舌" to 1,"淡紅舌" to 2,"淡白舌" to 14,"紫舌" to 3)

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
        //val relativeLayout = RelativeLayout(this)
        //scrollView.addView(relativeLayout)
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        scrollView.addView(linearLayout)

        comment(linearLayout)
    }

    @SuppressLint("ResourceType")
    fun comment(linearLayout: LinearLayout) {
        val miss = intent.getLongArrayExtra("MISS")
        val miss_ans = intent.getStringArrayExtra("MISS_ANS")
        val param = LinearLayout.LayoutParams(WC, WC)

        val inlinearLayout_1 = LinearLayout(this)
        inlinearLayout_1.orientation = LinearLayout.HORIZONTAL

        val title = TextView(this)
        title.text = "同一比較"
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
        title.textSize = 64.0f
        val back = GradientDrawable()
        back.setStroke(3, Color.BLACK)
        if (title.parent != null){
            ((title.parent) as ViewGroup).removeView(title)
        }
        inlinearLayout_1.addView(title, param)
        linearLayout.addView(inlinearLayout_1, param)

        for (num in Array(miss_ans.size, { i -> i })) {
            val inlinearLayout_2 = LinearLayout(this)
            inlinearLayout_2.orientation = LinearLayout.HORIZONTAL

            val inlinearLayout_2_1 = LinearLayout(this)
            inlinearLayout_2_1.orientation = LinearLayout.VERTICAL

            val space_1 = Space(this)
            inlinearLayout_2_1.addView(space_1, LinearLayout.LayoutParams(100, 200))

            inlinearLayout_2.addView(inlinearLayout_2_1, param)

            val q_title = TextView(this)
            q_title.text = "問中の舌画像"
            q_title.textSize = 32.0f
            q_title.gravity = Gravity.CENTER
            inlinearLayout_2_1.addView(q_title, param)

            val inlinearLayout_3 = LinearLayout(this)
            inlinearLayout_3.orientation = LinearLayout.HORIZONTAL

            val inlinearLayout_3_1 = LinearLayout(this)
            inlinearLayout_3_1.orientation = LinearLayout.VERTICAL

            val state_1 = TextView(this)
            state_1.text = "誤解答した画像" + "「" + miss_ans[num] + "」"
            state_1.textSize = 32.0f
            state_1.gravity = Gravity.CENTER
            inlinearLayout_3_1.addView(state_1, param)

            val r_1 = resources.getIdentifier("q" + miss[num] + "_image", "drawable", packageName) //drawableの画像指定
            val imageView_1 = ImageView(this)
            imageView_1.setImageResource(r_1) //imageViewに画像設定
            inlinearLayout_3_1.addView(imageView_1, param)

            inlinearLayout_3.addView(inlinearLayout_3_1, param)

            val inlinearLayout_3_2 = LinearLayout(this)
            inlinearLayout_3_2.orientation = LinearLayout.VERTICAL

            val text_1 = TextView(this)
            text_1.text = "注目点"
            text_1.textSize = 32.0f
            text_1.gravity = Gravity.CENTER
            inlinearLayout_3_2.addView(text_1, LinearLayout.LayoutParams(100, WC))

            val imageView_2 = ImageView(this)
            val r_2 = resources.getIdentifier("icon", "drawable", packageName)
            imageView_2.setImageResource(r_2) //imageViewに画像設定
            inlinearLayout_3_2.addView(imageView_2, LinearLayout.LayoutParams(100, WC))

            inlinearLayout_3.addView(inlinearLayout_3_2, param)

            val inlinearLayout_3_3 = LinearLayout(this)
            inlinearLayout_3_3.orientation = LinearLayout.VERTICAL

            val state_2 = TextView(this)
            state_2.text = "マーキング画像"
            state_2.textSize = 32.0f
            state_2.gravity = Gravity.CENTER
            inlinearLayout_3_3.addView(state_2, LinearLayout.LayoutParams(200, WC))

            val imageView_3 = ImageView(this)
            val r_3 = resources.getIdentifier("m" + miss[num] + "_" + real[miss_ans[num]] , "drawable", packageName) //drawableの画像指
            imageView_3.setImageResource(r_3) //imageViewに画像設定
            inlinearLayout_3_3.addView(imageView_3, param)

            inlinearLayout_3.addView(inlinearLayout_3_3, param)

            val inlinearLayout_3_4 = LinearLayout(this)
            inlinearLayout_3_4.orientation = LinearLayout.VERTICAL

            val r_4 = resources.getIdentifier("t" + (num+1) + "_1", "drawable", packageName) //drawableの画像指定
            val imageView_4 = ImageView(this)
            imageView_4.setImageResource(r_4) //imageViewに画像設定
            inlinearLayout_3_4.addView(imageView_4, param)

            inlinearLayout_3.addView(inlinearLayout_3_4, param)

            val inlinearLayout_4 = LinearLayout(this)
            inlinearLayout_4.orientation = LinearLayout.HORIZONTAL

            val inlinearLayout_4_1 = LinearLayout(this)
            inlinearLayout_4_1.orientation = LinearLayout.VERTICAL

            val state_3 = TextView(this)
            state_3.text = "選択した舌の色の\n代表的な画像" + "「" + miss_ans[num] + "」"
            state_3.textSize = 32.0f
            state_3.gravity = Gravity.CENTER
            inlinearLayout_4_1.addView(state_3, param)

            val r_5 = resources.getIdentifier("q" + real[miss_ans[num]] + "_image", "drawable", packageName) //drawableの画像指定
            val imageView_5 = ImageView(this)
            imageView_5.setImageResource(r_5) //imageViewに画像設定
            inlinearLayout_4_1.addView(imageView_5, param)

            inlinearLayout_4.addView(inlinearLayout_4_1, param)

            val inlinearLayout_4_2 = LinearLayout(this)
            inlinearLayout_4_2.orientation = LinearLayout.VERTICAL

            val text_2 = TextView(this)
            text_2.text = "注目点"
            text_2.textSize = 32.0f
            text_2.gravity = Gravity.CENTER
            inlinearLayout_4_2.addView(text_2, LinearLayout.LayoutParams(100, WC))

            val imageView_6 = ImageView(this)
            val r_6 = resources.getIdentifier("icon", "drawable", packageName)
            imageView_6.setImageResource(r_6) //imageViewに画像設定
            inlinearLayout_4_2.addView(imageView_6, LinearLayout.LayoutParams(100, WC))

            inlinearLayout_4.addView(inlinearLayout_4_2, param)

            val inlinearLayout_4_3 = LinearLayout(this)
            inlinearLayout_4_3.orientation = LinearLayout.VERTICAL

            val state_4 = TextView(this)
            state_4.text = "マーキング画像"
            state_4.textSize = 32.0f
            state_4.gravity = Gravity.CENTER
            inlinearLayout_4_3.addView(state_4, LinearLayout.LayoutParams(200, WC))

            val r_7 = resources.getIdentifier( "m" + real[miss_ans[num]]+ "_" + miss[num], "drawable", packageName) //drawableの画像指定
            val imageView_7 = ImageView(this)
            imageView_7.setImageResource(r_7) //imageViewに画像設定
            inlinearLayout_4_3.addView(imageView_7, param)

            inlinearLayout_4.addView(inlinearLayout_4_3, param)

            val inlinearLayout_4_4 = LinearLayout(this)
            inlinearLayout_4_4.orientation = LinearLayout.VERTICAL

            val r_8 = resources.getIdentifier("t" + (num+1) + "_2", "drawable", packageName) //drawableの画像指定
            val imageView_8 = ImageView(this)
            imageView_8.setImageResource(r_8) //imageViewに画像設定
            inlinearLayout_4_4.addView(imageView_8, param)

            inlinearLayout_4.addView(inlinearLayout_4_4, param)

            linearLayout.addView(inlinearLayout_2, param)
            linearLayout.addView(inlinearLayout_3, param)
            linearLayout.addView(inlinearLayout_4, param)
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
