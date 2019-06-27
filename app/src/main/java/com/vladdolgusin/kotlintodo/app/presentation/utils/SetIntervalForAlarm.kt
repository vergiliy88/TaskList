package com.vladdolgusin.kotlintodo.app.presentation.utils

import com.vladdolgusin.kotlintodo.domain.model.Task
import java.util.*

class SetIntervalForAlarm{

    fun setInterval(dateTask: Long, whenStartAlarm: Int): Long {
        return when (whenStartAlarm) {
            0 -> {dateTask}
            1 -> {
                if (dateTask - 900000 <= Date().time ) {
                    dateTask
                }else{
                    dateTask - 900000
                }
            }
            2 -> {
                if (dateTask - 1800000 <= Date().time ) {
                    dateTask
                }else{
                    dateTask - 1800000
                }
            }
            3 -> {
                if (dateTask - 3600000 <= Date().time ) {
                    dateTask
                }else{
                    dateTask - 3600000
                }}
            else -> dateTask
        }
    }
}