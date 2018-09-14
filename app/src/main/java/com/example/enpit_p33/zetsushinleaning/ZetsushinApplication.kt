package com.example.enpit_p33.zetsushinleaning

import android.app.Application
import io.realm.Realm

class ZetsushinApplication : Application(){
    override fun onCreate(){
        super.onCreate()

        Realm.init(this)
    }
}