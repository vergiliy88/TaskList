package com.vladdolgusin.kotlintodo

import android.app.Application
import com.vladdolgusin.kotlintodo.app.DI.DI


class App: Application() {
    var relations: MutableMap<String, Any> = mutableMapOf()
    companion object {
        private var instance: App? = null
        fun getInstance(): App{
            if (instance == null) {
                instance = App()
            }
            return instance as App
        }
    }

    override fun onCreate() {
        super.onCreate()
        DI.initialize(this)
    }

    fun getPresenter(nameActivity:String): Any? {
        return relations.get(nameActivity)
    }

    fun setPresenterToActivity(nameActivity:String, presenter: Any){
        relations.put(nameActivity, presenter)
    }

    fun checkRelation(nameActivity:String): Boolean{
        return relations.containsKey(nameActivity)
    }

    fun deleteRalation(nameActivity:String){
        relations.remove(nameActivity)
    }
}