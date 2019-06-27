package com.vladdolgusin.kotlintodo.app.presentation.presenter

import com.vladdolgusin.kotlintodo.domain.model.Task

interface InteractorContract {
    fun onSavedData()
    fun onLoadData(tasks: List<Task>)
}