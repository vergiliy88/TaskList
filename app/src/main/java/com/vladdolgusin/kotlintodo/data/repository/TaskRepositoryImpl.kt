package com.vladdolgusin.kotlintodo.data.repository

import android.util.Log
import com.vladdolgusin.kotlintodo.data.entity.TaskDB
import com.vladdolgusin.kotlintodo.data.repository.room.RoomSingleton
import com.vladdolgusin.kotlintodo.domain.repository.TaskRepository

class TaskRepositoryImpl(var roomSingleton: RoomSingleton): TaskRepository{

    override fun getTasksDataFromDB(sortType: Int): List<TaskDB> {
        Log.i("TASK" , "${sortType}")
        return when (sortType) {
            0 -> roomSingleton.taskDao().allTask
            1 -> roomSingleton.taskDao().allTaskByDate
            2 -> roomSingleton.taskDao().allTaskByCompl
            3 -> roomSingleton.taskDao().allTaskByPrior
            else -> roomSingleton.taskDao().allTask
        }
    }

    override fun saveTask(taskDB: TaskDB): List<Long>? {
        return roomSingleton.taskDao().insertAll(taskDB)
    }

    override fun getMaxOrderValue(): Int {
         return roomSingleton.taskDao().maxOrederIndex
    }

    override fun updateTask(taskDB: TaskDB) {
        roomSingleton.taskDao().updateTask(taskDB)
    }

    override fun deleteTask(taskDB: TaskDB) {
        roomSingleton.taskDao().delete(taskDB)
    }
}