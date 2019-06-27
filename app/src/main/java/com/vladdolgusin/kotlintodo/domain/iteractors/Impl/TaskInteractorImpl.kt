package com.vladdolgusin.kotlintodo.domain.iteractors.Impl

import com.vladdolgusin.kotlintodo.domain.iteractors.TaskInteractor
import com.vladdolgusin.kotlintodo.domain.mapper.convertTasksFromDB
import com.vladdolgusin.kotlintodo.domain.mapper.convertTasksToDB
import com.vladdolgusin.kotlintodo.domain.model.Task
import com.vladdolgusin.kotlintodo.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskInteractorImpl(var repository: TaskRepository): TaskInteractor {

    override fun getTasksDataFromDB(sortType: Int, onCompletedLoadData: TaskInteractor.CallbackLoadData) {
        GlobalScope.launch(Dispatchers.Main){
            val data = withContext(Dispatchers.IO) {convertTasksFromDB(repository.getTasksDataFromDB(sortType))}
            onCompletedLoadData.onCompletedLoadData(data)
        }
    }

    override  fun saveTask(task: Task, onCompletedSaveData: TaskInteractor.CallbackSaveData) {
        var listNewTasks: List<Long>? = null
        var maxOrederIndex = 0
        GlobalScope.launch(Dispatchers.Main){
            withContext(Dispatchers.IO) {
                listNewTasks = repository.saveTask(convertTasksToDB(task))
                maxOrederIndex = repository.getMaxOrderValue()
            }
            onCompletedSaveData.onCompletedSaveData(listNewTasks, maxOrederIndex, task)
        }
    }

    override fun updateTask(task: Task) {
        GlobalScope.launch(Dispatchers.IO){
            repository.updateTask(convertTasksToDB(task))
        }
    }

    override fun deleteTask(task: Task) {
        GlobalScope.launch(Dispatchers.IO){
            repository.deleteTask(convertTasksToDB(task))
        }
    }
}