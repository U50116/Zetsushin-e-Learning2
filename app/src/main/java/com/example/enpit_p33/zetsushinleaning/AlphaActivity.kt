package com.example.enpit_p33.zetsushinleaning

import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import java.util.*

class AlphaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //レイアウト設定
        val scrollView = ScrollView(this)
        setContentView(scrollView)
        val relativeLayout = RelativeLayout(this)
        scrollView.addView(relativeLayout)
        question(relativeLayout)
    }

    fun question( relativeLayout: RelativeLayout){
        val WC = ViewGroup.LayoutParams.WRAP_CONTENT
        val x = Array(20,{i->i}).toList()
        Collections.shuffle(x)
        val y = x.toIntArray()
        for(num in y.indices) {
            //画像レイアウト
            val r = resources.getIdentifier("q" + (y[num]+1) + "_image", "drawable", packageName) //drawableの画像指定
            val imageView = ImageView(this)
            imageView.setImageResource(r) //imageViewに画像設定
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) //エラー回避
            imageView.id = 1 + num * 2
            val param1 = RelativeLayout.LayoutParams(740, 700)
            if(num==0){
                param1.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                param1.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                param1.setMargins(-50, 150, 0, 0)
            }else {
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
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton1.id = 1000 + num * 4
            radioButton1.text = "    1    "
            radioButton1.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton2.id = 1001 + num * 4
            radioButton2.text = "    2    "
            radioButton2.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton3.id = 1002 + num * 4
            radioButton3.text = "    3    "
            radioButton3.textSize = 32.0f
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            radioButton4.id = 1003 + num * 4
            radioButton4.text = "    4    "
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
            when(radioGroup.checkedRadioButtonId){
                radioButton1.id->{}
                radioButton2.id->{}
                radioButton3.id->{}
                radioButton4.id->{}
            }
        }
    }

}