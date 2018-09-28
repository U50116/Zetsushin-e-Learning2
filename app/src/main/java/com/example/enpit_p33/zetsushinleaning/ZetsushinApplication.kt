package com.example.enpit_p33.zetsushinleaning

import android.app.Application
import android.preference.PreferenceManager
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
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
    }
    private fun saveDate(){
        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        realm = Realm.getInstance(realmConfig)
        val x = Array(20, { i -> i })

        realm.executeTransaction {
            val delete = realm.where(ZetsuImage::class.java).findAll()
            delete.deleteAllFromRealm()
            for(i in x){
                if(image[i] != null){
                    //if(realm.where(ZetsuImage::class.java).equalTo("image_id", i).findAll() == null) {
                        realm.createObject<ZetsuImage>(i).apply {
                            zetsu_color = (image[i] ?: "")
                        }
                    //}else{}
                }else{}
            }
        }
    }
}