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
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*

class CommentActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private val WC = ViewGroup.LayoutParams.WRAP_CONTENT
    val real = mapOf("紅舌" to "1_1","淡紅舌" to "2_1","淡白舌" to "3_1","紫舌" to "4_1")
    val color = listOf("紅舌", "淡紅舌", "淡白舌", "紫舌")

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
        val miss = intent.getStringArrayExtra("MISS")
        val miss_ans = intent.getStringArrayExtra("MISS_ANS")
        val param = LinearLayout.LayoutParams(WC, WC)

        val back = GradientDrawable()
        back.setStroke(3, Color.BLACK)

        val inlinearLayout_1 = LinearLayout(this)
        inlinearLayout_1.orientation = LinearLayout.VERTICAL

        val title = TextView(this)
        title.text = "2回とも同じ誤りへの対処"
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
        title.textSize = 64.0f
        if (title.parent != null){
            ((title.parent) as ViewGroup).removeView(title)
        }
        inlinearLayout_1.addView(title, param)

        val question_statement = TextView(this)
        question_statement.text = "2回とも同じ舌画像で同じ間違えをしたものをピックアップしました。\n間違えた舌画像と選択した舌画像で舌色がどのように異なるのか、それぞれの舌画像で特徴的な舌の色を見て確認しましょう。"
        question_statement.textSize = 32.0f
        inlinearLayout_1.addView(question_statement, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        val separate_1 = View(this)
        separate_1.background = back
        inlinearLayout_1.addView(separate_1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5))

        linearLayout.addView(inlinearLayout_1, param)

        for (num in Array(miss_ans.size, { i -> i })) {
            val inlinearLayout_2 = LinearLayout(this)
            inlinearLayout_2.orientation = LinearLayout.HORIZONTAL

            val inlinearLayout_2_1 = LinearLayout(this)
            inlinearLayout_2_1.orientation = LinearLayout.VERTICAL

            inlinearLayout_2.addView(inlinearLayout_2_1, param)

            val space_2_1 = Space(this)
            inlinearLayout_2_1.addView(space_2_1, LinearLayout.LayoutParams(100, 50))

            val inlinearLayout_3 = LinearLayout(this)
            inlinearLayout_3.orientation = LinearLayout.HORIZONTAL

            val inlinearLayout_3_1 = LinearLayout(this)
            inlinearLayout_3_1.orientation = LinearLayout.VERTICAL

            val state_1 = TextView(this)
            state_1.text = "問中の舌画像\n誤解答した画像" + "「" + color[miss[num].substring(0,1).toInt() - 1] + "」"
            state_1.textSize = 32.0f
            state_1.gravity = Gravity.CENTER
            inlinearLayout_3_1.addView(state_1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WC))

            val r_1 = resources.getIdentifier("q" + miss[num], "drawable", packageName) //drawableの画像指定
            val imageView_1 = ImageView(this)
            imageView_1.setImageResource(r_1)
            imageView_1.background
            inlinearLayout_3_1.addView(imageView_1,param)

            inlinearLayout_3.addView(inlinearLayout_3_1, param)

            val inlinearLayout_3_2 = LinearLayout(this)
            inlinearLayout_3_2.orientation = LinearLayout.VERTICAL

            val space_3_2_1 = Space(this)
            inlinearLayout_3_2.addView(space_3_2_1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100))

            val text_1 = TextView(this)
            text_1.text = "注目点"
            text_1.setTextColor(Color.BLUE)
            text_1.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
            text_1.textSize = 32.0f
            text_1.gravity = Gravity.CENTER
            inlinearLayout_3_2.addView(text_1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WC))

            val imageView_2 = ImageView(this)
            val r_2 = resources.getIdentifier("icon", "drawable", packageName)
            imageView_2.setImageResource(r_2) //imageViewに画像設定
            inlinearLayout_3_2.addView(imageView_2, LinearLayout.LayoutParams(150, WC))

            inlinearLayout_3.addView(inlinearLayout_3_2, param)

            val inlinearLayout_3_3 = LinearLayout(this)
            inlinearLayout_3_3.orientation = LinearLayout.VERTICAL

            val state_2 = TextView(this)
            state_2.text = "  \n特徴的な色を表示した舌"
            state_2.textSize = 32.0f
            state_2.gravity = Gravity.CENTER
            inlinearLayout_3_3.addView(state_2, LinearLayout.LayoutParams(WC, WC))

            val imageView_3 = ImageView(this)
            val r_3 = resources.getIdentifier("m" + miss[num] + "_" + real[miss_ans[num]] , "drawable", packageName) //drawableの画像指
            imageView_3.setImageResource(r_3) //imageViewに画像設定
            inlinearLayout_3_3.addView(imageView_3, param)

            inlinearLayout_3.addView(inlinearLayout_3_3, param)

            val space_3_1 = Space(this)
            inlinearLayout_3.addView(space_3_1, LinearLayout.LayoutParams(100, 50))

            val inlinearLayout_3_4 = LinearLayout(this)
            inlinearLayout_3_4.orientation = LinearLayout.VERTICAL

            val space_3_2 = Space(this)
            inlinearLayout_3_4.addView(space_3_2, LinearLayout.LayoutParams(100, 50))

            val r_4 = resources.getIdentifier("t" + real[color[miss[num].substring(0,1).toInt() - 1]] + "_" + real[miss_ans[num]], "drawable", packageName) //drawableの画像指定
            val imageView_4 = ImageView(this)
            imageView_4.setImageResource(r_4) //imageViewに画像設定
            inlinearLayout_3_4.addView(imageView_4, param)

            inlinearLayout_3.addView(inlinearLayout_3_4, param)



            val inlinearLayout_4 = LinearLayout(this)
            inlinearLayout_4.orientation = LinearLayout.HORIZONTAL

            val inlinearLayout_4_1 = LinearLayout(this)
            inlinearLayout_4_1.orientation = LinearLayout.VERTICAL

            val state_3 = TextView(this)
            Log.d("debug", miss_ans[num])
            state_3.text = "選択した舌の色の\n代表的な画像" + "「" + miss_ans[num] + "」"
            state_3.textSize = 32.0f
            state_3.gravity = Gravity.CENTER
            inlinearLayout_4_1.addView(state_3, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WC))

            val r_5 = resources.getIdentifier("q" + real[miss_ans[num]], "drawable", packageName) //drawableの画像指定
            val imageView_5 = ImageView(this)
            imageView_5.setImageResource(r_5) //imageViewに画像設定
            inlinearLayout_4_1.addView(imageView_5, param)

            inlinearLayout_4.addView(inlinearLayout_4_1, param)

            val inlinearLayout_4_2 = LinearLayout(this)
            inlinearLayout_4_2.orientation = LinearLayout.VERTICAL

            val space_4_2_1 = Space(this)
            inlinearLayout_4_2.addView(space_4_2_1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100))

            val text_2 = TextView(this)
            text_2.text = "注目点"
            text_2.setTextColor(Color.BLUE)
            text_2.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
            text_2.textSize = 32.0f
            text_2.gravity = Gravity.CENTER
            inlinearLayout_4_2.addView(text_2, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WC))

            val imageView_6 = ImageView(this)
            val r_6 = resources.getIdentifier("icon", "drawable", packageName)
            imageView_6.setImageResource(r_6) //imageViewに画像設定
            inlinearLayout_4_2.addView(imageView_6, LinearLayout.LayoutParams(150, WC))

            inlinearLayout_4.addView(inlinearLayout_4_2, param)

            val inlinearLayout_4_3 = LinearLayout(this)
            inlinearLayout_4_3.orientation = LinearLayout.VERTICAL

            val state_4 = TextView(this)
            state_4.text = "  \n特徴的な色を表示した舌"
            state_4.textSize = 32.0f
            state_4.gravity = Gravity.CENTER
            inlinearLayout_4_3.addView(state_4, LinearLayout.LayoutParams(WC, WC))

            val r_7 = resources.getIdentifier( "m" + real[miss_ans[num]]+ "_" + miss[num], "drawable", packageName) //drawableの画像指定
            val imageView_7 = ImageView(this)
            imageView_7.setImageResource(r_7) //imageViewに画像設定
            imageView_7.background = back
            inlinearLayout_4_3.addView(imageView_7, param)

            inlinearLayout_4.addView(inlinearLayout_4_3, param)

            val space_4_1 = Space(this)
            inlinearLayout_4.addView(space_4_1, LinearLayout.LayoutParams(100, 50))

            val inlinearLayout_4_4 = LinearLayout(this)
            inlinearLayout_4_4.orientation = LinearLayout.VERTICAL

            val space_4_2 = Space(this)
            inlinearLayout_4_4.addView(space_4_2, LinearLayout.LayoutParams(100, 50))

            val r_8 = resources.getIdentifier("t" + real[miss_ans[num]] + "_" + real[color[miss[num].substring(0,1).toInt() - 1]], "drawable", packageName) //drawableの画像指定
            val imageView_8 = ImageView(this)
            imageView_8.setImageResource(r_8) //imageViewに画像設定
            inlinearLayout_4_4.addView(imageView_8, param)

            inlinearLayout_4.addView(inlinearLayout_4_4, param)

            linearLayout.addView(inlinearLayout_2, param)
            linearLayout.addView(inlinearLayout_3, param)

            val space_1 = Space(this)
            linearLayout.addView(space_1, LinearLayout.LayoutParams(100, 50))

            linearLayout.addView(inlinearLayout_4, param)

            val separate_2 = View(this)
            separate_2.background = back
            linearLayout.addView(separate_2, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5))
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
