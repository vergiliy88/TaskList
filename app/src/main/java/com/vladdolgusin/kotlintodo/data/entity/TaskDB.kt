package com.vladdolgusin.kotlintodo.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "task")
data class TaskDB(
        @PrimaryKey (autoGenerate = true)
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