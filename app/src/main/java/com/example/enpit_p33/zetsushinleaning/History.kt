package com.example.enpit_p33.zetsushinleaning

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class History: RealmObject(){
    @PrimaryKey
    var history_id: Long = 0
    var date: String = ""
    var question_id: Long = 0
    var result: RealmList<Result> = RealmList()
}

open class Result: RealmObject(){
    var answer: String = ""
}

open class Question: RealmObject(){
    @PrimaryKey
    var question_id: Long = 0
    var questions: RealmList<QuestionList> = RealmList()
}

open class QuestionList: RealmObject(){
    var question_number: Int = 0
    var image_number: Long = 0
    var choice1: String = ""
    var choice2: String = ""
    var choice3: String = ""
    var choice4: String = ""
}