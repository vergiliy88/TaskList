package com.vladdolgusin.kotlintodo.app.DI

import android.app.Application
import com.vladdolgusin.kotlintodo.data.configPref.SharedPref
import com.vladdolgusin.kotlintodo.data.repository.TaskRepositoryImpl
import com.vladdolgusin.kotlintodo.data.repository.room.RoomSingleton
import com.vladdolgusin.kotlintodo.domain.iteractors.Impl.TaskInteractorImpl
import com.vladdolgusin.kotlintodo.domain.iteractors.TaskInteractor
import com.vladdolgusin.kotlintodo.domain.repository.SharedPrefRepository

object MainModule {
    private lateinit var confStore: SharedPrefRepository
    private lateinit var context: Application
    fun initialize(app: Application) {
        this.context = app
        confStore = SharedPref(app)
    }

    fun getTaskIneractorImpl(): TaskInteractor {
        return makeTaskInteractor(getConfigPref())
    }

    private fun makeTaskInteractor(sortType: Int) = TaskInteractorImpl(TaskRepositoryImpl(RoomSingleton.getInstance(context)))

    private fun getConfigPref() = confStore.getSortType()

    fun getConsPresSettings() = confStore
}