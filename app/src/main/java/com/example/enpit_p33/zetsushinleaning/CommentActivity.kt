package com.example.enpit_p33.zetsushinleaning

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Typeface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import io.realm.Realm
import io.realm.RealmConfiguration

class CommentActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private val WC = ViewGroup.LayoutParams.WRAP_CONTENT
    val real = mapOf("紅舌" to 1,"淡紅舌" to 2,"淡白舌" to 14,"紫舌" to 3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

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

        comment(relativeLayout)
    }

    @SuppressLint("ResourceType")
    fun comment(relativeLayout: RelativeLayout) {
        val miss = intent.getLongArrayExtra("MISS")
        val miss_ans = intent.getStringArrayExtra("MISS_ANS")

        for (num in Array(miss_ans.size, { i -> i })) {
            val r = resources.getIdentifier("q" + miss[num] + "_image", "drawable", packageName) //drawableの画像指定
            Log.d("debug", r.toString())
            val imageView = ImageView(this)
            imageView.setImageResource(r) //imageViewに画像設定
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) //エラー回避
            imageView.id = 1 + num * 4
            val param1 = RelativeLayout.LayoutParams(WC, WC)
            if (num == 0) {
                param1.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                param1.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                param1.setMargins(50, 150, 0, 0)
            } else {
                param1.addRule(RelativeLayout.BELOW, 3 + (num - 1) * 4)
                param1.addRule(RelativeLayout.ALIGN_LEFT, 3 + (num - 1) * 4)
                param1.setMargins(0, 200, 0, 0)
            }
            relativeLayout.addView(imageView, param1)

            val r1 = resources.getIdentifier("q" + 1 + "_image", "drawable", packageName) //drawableの画像指定
            Log.d("debug", r1.toString())
            val imageView1 = ImageView(this)
            imageView1.setImageResource(r1) //imageViewに画像設定
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) //エラー回避
            imageView1.id = 2 + num * 4
            val param2 = RelativeLayout.LayoutParams(WC, WC)
            if (num == 0) {
                param2.addRule(RelativeLayout.CENTER_HORIZONTAL)
                param2.setMargins(0, 150, 0, 0)
            } else {
                param2.addRule(RelativeLayout.BELOW, 4 + (num - 1) * 4)
                param2.addRule(RelativeLayout.ALIGN_LEFT, 4 + (num - 1) * 4)
                param2.setMargins(0, 200, 0, 0)
            }
            relativeLayout.addView(imageView1, param2)

            val r2 = resources.getIdentifier("q" + real[miss_ans[num]] + "_image", "drawable", packageName) //drawableの画像指定
            Log.d("debug", r2.toString())
            val imageView2 = ImageView(this)
            imageView2.setImageResource(r2) //imageViewに画像設定
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) //エラー回避
            imageView2.id = 3 + num * 4
            val param3 = RelativeLayout.LayoutParams(WC, WC)
            param3.addRule(RelativeLayout.BELOW, 1 + num * 4)
            param3.addRule(RelativeLayout.ALIGN_LEFT, 1 + num * 4)
            param3.setMargins(0, 0, 0, 0)
            relativeLayout.addView(imageView2, param3)

            val r3 = resources.getIdentifier("q" + 1 + "_image", "drawable", packageName) //drawableの画像指定
            Log.d("debug", r3.toString())
            val imageView3 = ImageView(this)
            imageView3.setImageResource(r3) //imageViewに画像設定
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) //エラー回避
            imageView3.id = 4 + num * 4
            val param4 = RelativeLayout.LayoutParams(WC, WC)
            param4.addRule(RelativeLayout.ALIGN_TOP, 3 + num * 4)
            param4.addRule(RelativeLayout.ALIGN_LEFT, 2 + num * 4)
            param4.setMargins(0, 0, 0, 0)
            relativeLayout.addView(imageView3, param4)
        }
        val title = TextView(this)
        title.id = 100
        title.text = "同一比較"
        title.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC))
        title.textSize = 64.0f
        val param = RelativeLayout.LayoutParams(WC, WC)
        param.addRule(RelativeLayout.CENTER_HORIZONTAL)
        relativeLayout.addView(title, param)
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
