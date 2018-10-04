package com.example.enpit_p33.zetsushinleaning

import android.app.Application
import android.text.format.DateFormat
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ZetsushinApplication : Application(){
    private lateinit var realm: Realm
    val image = mapOf(1 to "紅舌", 2 to "淡紅舌", 3 to "紫舌", 4 to "紫舌", 5 to "淡紅舌",
            6 to "紫舌", 8 to "淡紅舌", 9 to "淡紅舌", 10 to "紅舌", 11 to "紫舌",
            12 to "紫舌", 13 to "淡紅舌", 14 to "淡白舌", 15 to "紫舌", 16 to "淡紅舌",
            17 to "紫舌", 18 to "紫舌", 19 to "紅舌", 20 to "淡紅舌")

    override fun onCreate(){
        super.onCreate()

        Realm.init(this)

        saveDate()

        Log.d("debug", "History: " + realm.where(History::class.java).findAll().size.toString())
        Log.d("debug", "Zetsushin: " + realm.where(Zetsushin::class.java).findAll().size.toString())
        Log.d("debug", "Question: " + realm.where(Question::class.java).findAll().size.toString())
        Log.d("debug", "QuestionList: " + realm.where(QuestionList::class.java).findAll().size.toString())
        Log.d("debug", "Result: " + realm.where(Result::class.java).findAll().size.toString())
        Log.d("debug", "ZetsuImage: " + realm.where(ZetsuImage::class.java).findAll().size.toString())
        Log.d("debug", "ZetsuImage_size: " + image.size.toString())
    }
    private fun saveDate(){
        val realmConfig = RealmConfiguration.Builder()
                .name("zetsushinleaning.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .build()
        realm = Realm.getInstance(realmConfig)

        realm.executeTransaction {
            realm.where(ZetsuImage::class.java).findAll().deleteAllFromRealm()
            //realm.where(Zetsushin::class.java).findAll().deleteAllFromRealm()
            realm.where(QuestionList::class.java).findAll().deleteAllFromRealm()
            realm.where(Question::class.java).findAll().deleteAllFromRealm()
            realm.where(Result::class.java).findAll().deleteAllFromRealm()
            realm.where(History::class.java).findAll().deleteAllFromRealm()
            for(j in Array(image.size+1, { i -> i })){
                if(image[j + 1] != null){
                    realm.createObject<ZetsuImage>(j + 1).apply {
                        zetsu_color = (image[j + 1] ?: "")
                    }
                }
            }
        }
    }
}