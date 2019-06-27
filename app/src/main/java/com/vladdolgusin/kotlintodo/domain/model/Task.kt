package com.vladdolgusin.kotlintodo.domain.model

data class Task(
        var id:Int,
        var textTask:String,
        var dateTask:Long,
        var priority:Int,
        var complited:Int,
        var orderField: Int,
        var savedTime: Int,
        var setAlarm: Int,
        var whenStartAlarm: Int
)