package com.vladdolgusin.kotlintodo.app.presentation.utils

import com.vladdolgusin.kotlintodo.domain.model.Task

interface AdapterInterface {
    fun markTaskAsDone(position: Int)
    fun openDialogToEdit(position: Int)
    fun deleteTaskFromDB(task: Task)
    fun updateOrderView(listTask: MutableList<Task>)
}