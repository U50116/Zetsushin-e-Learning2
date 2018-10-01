package com.example.enpit_p33.zetsushinleaning

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class History: RealmObject(){
    @PrimaryKey
    var history_id: Long = 0
    var user_id: String = ""
    var date: String = ""
}

open class Question: RealmObject(){
    var question_id: Int = 0
    var history_id: Long = 0
    var question: Long = 0
    var answer: String = ""
}