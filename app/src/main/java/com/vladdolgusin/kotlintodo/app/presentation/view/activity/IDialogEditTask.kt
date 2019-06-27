package com.vladdolgusin.kotlintodo.app.presentation.view.activity

import com.vladdolgusin.kotlintodo.domain.model.Task

interface IDialogEditTask {
    fun onDialogPositiveClick(task: Task, position: Int, edit: Boolean)
    fun onDialogNegativeClick()
    fun showSnapBar(text: String)
}