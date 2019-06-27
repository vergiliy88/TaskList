package com.vladdolgusin.kotlintodo.data.configPref

import android.content.Context
import com.vladdolgusin.kotlintodo.domain.repository.SharedPrefRepository

class SharedPref (context: Context): SharedPrefRepository{
    private  val TASKS = "Tasks"
    private  val MODE = Context.MODE_PRIVATE
    private val SORT_TYPE = "sort_type"
    private val FIRST_ADD_TASK = "firat_run"

    private var preferences = context.getSharedPreferences(TASKS, MODE)

    override fun getSortType() = preferences.getInt(SORT_TYPE,0)

    override fun setSortType(type: Int) {
        preferences.edit().putInt(SORT_TYPE, type).apply()
    }

    override fun getConf() = preferences.getBoolean(FIRST_ADD_TASK,true)

    override fun setConf(conf: Boolean) {
        preferences.edit().putBoolean(FIRST_ADD_TASK, conf).apply()
    }
}