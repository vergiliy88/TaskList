package com.vladdolgusin.kotlintodo.domain.repository

import com.vladdolgusin.kotlintodo.data.entity.TaskDB


interface TaskRepository {
    fun getTasksDataFromDB(sortType: Int): List<TaskDB>
    fun saveTask(taskDB: TaskDB): List<Long>?
    fun updateTask(taskDB: TaskDB)
    fun deleteTask(taskDB: TaskDB)
    fun getMaxOrderValue(): Int
}