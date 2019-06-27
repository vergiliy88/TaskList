package com.vladdolgusin.kotlintodo.app.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.vladdolgusin.kotlintodo.data.repository.room.RoomSingleton

import com.vladdolgusin.kotlintodo.R
import com.vladdolgusin.kotlintodo.app.presentation.view.activity.Impl.MainActivity
import com.vladdolgusin.kotlintodo.data.entity.TaskDB

class AlertService: IntentService("NotificationService") {
    private val CHANNEL_ID = "default"
    private var mContext: Context? = null
    private lateinit var mDataBase: RoomSingleton

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mContext = applicationContext
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        mDataBase = RoomSingleton.getInstance(mContext!!)
        val task: TaskDB = mDataBase.taskDao().getSingleTask(intent!!.getIntExtra("id", 0))
        if (task.setAlarm == 1) {
            showNotification(task)
        }
    }

    private fun showNotification(task: TaskDB) {

        //Log.i("TASK", "NOTOFI id - ${task.id}, name - ${task.textTask}, setAlarm - ${task.setAlarm}, time - ${task.savedTime}, date - ${task.dateTask}, whenStartAlarm - ${task.whenStartAlarm}")
        val intent = Intent(mContext!!, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(mContext!!, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val pattern = longArrayOf(500, 500, 500, 500, 500)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = mContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        var channelId = ""
        val channel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(CHANNEL_ID, "ToDoList",
                    NotificationManager.IMPORTANCE_HIGH)
            channel.description = mContext!!.getString(R.string.notif_chanel)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            notificationManager?.createNotificationChannel(channel)
            channelId = channel.id
        }

        val notificationBuilder = NotificationCompat.Builder(mContext!!, channelId)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(task.textTask)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.WHITE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager?.notify(0 /* ID of notification */, notificationBuilder)

        startForeground(1, notificationBuilder)
    }
}
