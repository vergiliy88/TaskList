package com.vladdolgusin.kotlintodo.data.repository.room

import android.arch.persistence.room.*
import com.vladdolgusin.kotlintodo.data.entity.TaskDB
import com.vladdolgusin.kotlintodo.domain.model.Task


@Dao
interface TaskDAO {
    @get:Query("SELECT MAX(orderField) FROM task")
    val maxOrederIndex: Int

    @get:Query("SELECT * FROM task ORDER BY orderField DESC")
    val allTask: List<TaskDB>

    @get:Query("SELECT * FROM task ORDER BY dateTask")
    val allTaskByDate: List<TaskDB>

    @get:Query("SELECT * FROM task ORDER BY complited")
    val allTaskByCompl: List<TaskDB>

    @get:Query("SELECT * FROM task ORDER BY priority")
    val allTaskByPrior: List<TaskDB>

    @get:Query("SELECT * FROM task WHERE setAlarm = 1 AND complited = 0 ")
    val allTaskWithAlarm: List<TaskDB>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getSingleTask(id: Int): TaskDB

    @Query("SELECT * FROM task WHERE complited = :complited")
    fun getAllComolitedTask(complited: Int): List<TaskDB>

    @Insert
    fun insertAll(vararg task: TaskDB): List<Long>?

    @Delete
    fun delete(vararg task: TaskDB)

    @Update
    fun updateTask(vararg task: TaskDB)
}