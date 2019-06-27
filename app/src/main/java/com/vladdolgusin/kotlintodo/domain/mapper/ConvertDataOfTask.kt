package com.vladdolgusin.kotlintodo.domain.mapper

import com.vladdolgusin.kotlintodo.data.entity.TaskDB
import com.vladdolgusin.kotlintodo.domain.model.Task

fun convertTasksFromDB(taskDB: List<TaskDB>): List<Task> {
    val domainTask = mutableListOf<Task>()
    taskDB.forEach {
        it.let { task ->
            domainTask.add(Task(
                    id = task.id,
                    textTask = task.textTask,
                    dateTask = task.dateTask,
                    priority = task.priority,
                    complited = task.complited,
                    orderField = task.orderField,
                    savedTime = task.savedTime,
                    setAlarm = task.setAlarm,
                    whenStartAlarm = task.whenStartAlarm
            ))
        }
    }
    return domainTask
}

fun convertTasksToDB(task: Task): TaskDB {
    return TaskDB(
            id = task.id,
            textTask = task.textTask,
            dateTask = task.dateTask,
            priority = task.priority,
            complited = task.complited,
            orderField = task.orderField,
            savedTime = task.savedTime,
            setAlarm = task.setAlarm,
            whenStartAlarm = task.whenStartAlarm
    )
}
