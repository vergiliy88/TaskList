package com.vladdolgusin.kotlintodo.app.presentation.view.activity

import com.vladdolgusin.kotlintodo.domain.model.Task

interface MainActivity {
    fun initView(tasks: MutableList<Task>)
    fun loadTasksView(tasks: MutableList<Task>)
    fun addTaskView(position: Int)
    fun editTaskView(position: Int)
    fun editTaskDialogFragment(task: Task, position: Int)
    fun setAlarm(task: Task)
}