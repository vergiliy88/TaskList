package com.vladdolgusin.kotlintodo.app.presentation.presenter

import com.vladdolgusin.kotlintodo.app.presentation.view.activity.MainActivity


interface MainPresenter {
    fun attachView(view: MainActivity)
    fun detachView()
}