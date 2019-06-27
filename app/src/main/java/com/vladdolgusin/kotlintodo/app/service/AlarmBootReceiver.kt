package com.vladdolgusin.kotlintodo.app.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.vladdolgusin.kotlintodo.data.repository.room.RoomSingleton
import com.vladdolgusin.kotlintodo.app.presentation.utils.SetIntervalForAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AlarmBootReceiver : BroadcastReceiver() {
    private lateinit var mDataBase: RoomSingleton
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            mDataBase = RoomSingleton.getInstance(context)
            GlobalScope.launch(Dispatchers.IO) {
                val arrayTasks = mDataBase.taskDao().allTaskWithAlarm
                arrayTasks.forEach {
                    if (SetIntervalForAlarm().setInterval(it.dateTask, it.whenStartAlarm) > Date().time) {
                        Log.i("TASK11", "REBOOT id - ${it.id}, name - ${it.textTask}, setAlarm - ${it.setAlarm}, time - ${it.savedTime}, date - ${it.dateTask}, whenStartAlarm - ${it.whenStartAlarm}")
                        val timeBeforeAlarm = SetIntervalForAlarm().setInterval(it.dateTask, it.whenStartAlarm)
                        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
                        val broadcastIntent = Intent(context, AlarmReceiver::class.java)
                        broadcastIntent.putExtra("id", it.id) //data to pass
                        val pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, 0)
                        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, timeBeforeAlarm, pendingIntent)
                    }
                }
            }
        }
    }
}