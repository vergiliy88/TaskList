package com.vladdolgusin.kotlintodo.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("TASK11", "start AlarmReceiver")
        if (intent != null) {
            val service = Intent(context, AlertService::class.java)
            service.putExtra("id", intent.getIntExtra("id", 0))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context!!.startForegroundService(service)
            }else{
                context!!.startService(service)
            }
        }
    }
}