package com.vladdolgusin.kotlintodo.domain.iteractors

import com.vladdolgusin.kotlintodo.domain.model.Task

interface TaskInteractor {
    interface CallbackLoadData {
        suspend fun onCompletedLoadData(tasks: List<Task>)
    }
    interface CallbackSaveData {
        suspend fun onCompletedSaveData (idOfTasks: List<Long>?, maxOrderIndex: Int, task: Task)
    }

    fun getTasksDataFromDB(sortType: Int, onCompletedLoadData: CallbackLoadData)
    fun saveTask(task: Task, onCompletedSaveData: CallbackSaveData)
    fun updateTask(task: Task)
    fun deleteTask(task: Task)
}