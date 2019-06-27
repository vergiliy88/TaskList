package com.vladdolgusin.kotlintodo.app.DI

import android.app.Application

object DI {
    fun initialize(app: Application){
        MainModule.initialize(app)
    }
}