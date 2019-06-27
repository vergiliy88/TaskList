package com.vladdolgusin.kotlintodo.app.presentation.presenter.Impl

import com.vladdolgusin.kotlintodo.domain.model.Task
import com.vladdolgusin.kotlintodo.app.presentation.view.activity.MainActivity
import android.util.Log
import com.vladdolgusin.kotlintodo.app.DI.MainModule
import com.vladdolgusin.kotlintodo.domain.iteractors.TaskInteractor
import com.vladdolgusin.kotlintodo.app.presentation.presenter.MainPresenter
import kotlin.collections.ArrayList

class MainPresenterImpl: MainPresenter,
        TaskInteractor.CallbackLoadData,
        TaskInteractor.CallbackSaveData {

    private val confPref = MainModule.getConsPresSettings()
    private var taskInteractorImpl: TaskInteractor = MainModule.getTaskIneractorImpl()
    private var mView: MainActivity? = null
    private val mTasks: MutableList<Task> = ArrayList()
    private var mSelectedTask: Int = 0

    fun firstLoadData(sortType: Int, reload: Boolean) {
        if (reload) {
            taskInteractorImpl.getTasksDataFromDB(sortType, this)
        }else{
            if(mTasks.size != 0 ){
                mView?.let { it.loadTasksView(mTasks) }
            }else{
                taskInteractorImpl.getTasksDataFromDB(sortType, this)
            }
        }
    }

    fun resultDialogFragment(task: Task, position: Int, edit: Boolean){
        val positionAdd: Int
        if(!edit){
            positionAdd = addTaskToRecycler(task, confPref.getSortType())
            addTask(task, positionAdd)
        }else{
            mTasks.set(mSelectedTask, task)
            editTask(task, position)
        }
    }

    private fun addTask(task: Task, position: Int){
        taskInteractorImpl.saveTask(task, this)
        mView?.let { it.addTaskView(position) }
    }

    fun getDataToDialog(position: Int){
        mView?.let { it.editTaskDialogFragment(mTasks[position], position) }
        mSelectedTask = position
    }

    private fun editTask(task: Task, position: Int){
        editDataInDB(task)
        mView?.let { it.editTaskView(position) }
    }

    private fun updateOrderTask(task: Task) {
        editDataInDB(task)
    }

    fun changeStatusTask(position: Int){
        val copmliteTemp: Int = if (mTasks[position].complited == 0) 1 else 0
        mTasks.set(position, Task(mTasks[position].id, mTasks[position].textTask,
                mTasks[position].dateTask, mTasks[position].priority, copmliteTemp,
                0, mTasks[position].savedTime, mTasks[position].setAlarm, mTasks[position].whenStartAlarm))
        editTask(mTasks[position], position)
    }

    fun deleteTask(task: Task){
        taskInteractorImpl.deleteTask(task)
    }

    private fun editDataInDB(task: Task){
        taskInteractorImpl.updateTask(task)
        if (task.complited == 0) setAlarm(task)
    }

    private fun addTaskToRecycler(task: Task, type: Int): Int {
        var positionAdd = 0
        when(type){
            0 -> mTasks.add(0, task)
            1 -> {
                var haveMoreLaterTsk = true
                for((index,taskItem) in mTasks.withIndex()){
                    if (taskItem.dateTask >= task.dateTask) {
                        mTasks.add(index, task)
                        positionAdd = index
                        haveMoreLaterTsk = false
                        break
                    }
                }
                if (haveMoreLaterTsk) {
                    mTasks.add(mTasks.size, task)
                    positionAdd = mTasks.size
                }
            }
            3 -> {
                var haveMoreLaterTsk = true
                for((index,taskItem) in mTasks.withIndex()){
                    if (taskItem.priority >= task.priority) {
                        mTasks.add(index, task)
                        positionAdd = index
                        haveMoreLaterTsk = false
                        break
                    }
                }
                if (haveMoreLaterTsk) {
                    mTasks.add(mTasks.size, task)
                    positionAdd = mTasks.size
                }
            }
            else -> mTasks.add(0, task)
        }
        return positionAdd
    }

    fun updateOrderPresenter(listTask: MutableList<Task>){
        var iterator = listTask.size - 1
        listTask.forEach {
            it.orderField = iterator
            updateOrderTask(it)
            iterator --
        }
    }

    private fun setAlarm(task: Task) {
        Log.i("TASK", "SETALARM id - ${task.id}, name - ${task.textTask}, setAlarm - ${task.setAlarm}, time - ${task.savedTime}, date - ${task.dateTask}, whenStartAlarm - ${task.whenStartAlarm}")
        mView?.let { it.setAlarm(task) }
    }

    override suspend fun onCompletedSaveData(idOfTasks: List<Long>?, maxOrderIndex: Int, task: Task) {
        idOfTasks?.let {
            if (idOfTasks[0] > 0) {
                task.id = idOfTasks[0].toInt()
                setAlarm(task)
            }
        }
    }

    override suspend fun onCompletedLoadData(tasks: List<Task>) {
        mTasks.clear()
        mTasks.addAll(tasks)
        mView?.let { it.loadTasksView(mTasks) }
    }

    override fun attachView(view: MainActivity) {
        this.mView = view
        this.mView!!.initView(mTasks)
    }

    override fun detachView() {
        this.mView = null
    }
}