package com.vladdolgusin.kotlintodo.app.presentation.view.activity.Impl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.vladdolgusin.kotlintodo.App
import com.vladdolgusin.kotlintodo.domain.model.Task
import com.vladdolgusin.kotlintodo.R
import com.vladdolgusin.kotlintodo.app.DI.MainModule
import com.vladdolgusin.kotlintodo.app.presentation.presenter.Impl.MainPresenterImpl
import com.vladdolgusin.kotlintodo.app.presentation.view.adapters.AdapterTask
import com.vladdolgusin.kotlintodo.app.presentation.view.fragments.Impl.DetailedFragment
import com.vladdolgusin.kotlintodo.app.presentation.view.fragments.Impl.MenuFragment
import com.vladdolgusin.kotlintodo.app.presentation.view.activity.IDialogEditTask
import com.vladdolgusin.kotlintodo.app.presentation.view.activity.MainActivity
import com.vladdolgusin.kotlintodo.app.presentation.utils.AdapterInterface
import com.vladdolgusin.kotlintodo.app.presentation.utils.MoveItemCallback
import com.vladdolgusin.kotlintodo.app.presentation.utils.SetIntervalForAlarm
import com.vladdolgusin.kotlintodo.app.presentation.utils.StartDragListener
import com.vladdolgusin.kotlintodo.app.service.AlarmReceiver
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainActivity, IDialogEditTask,
        StartDragListener, AdapterInterface, MenuFragment.OnFragmentInteractionListener {


    private lateinit var adapterTask: AdapterTask
    private var presenter: MainPresenterImpl? = null
    private lateinit var app: App
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var mArrayTypeSort: Array<String>
    private val confPref = MainModule.getConsPresSettings()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mActionBarToolbar = findViewById<Toolbar>(R.id.tollbar)
        setSupportActionBar(mActionBarToolbar)

        mArrayTypeSort = resources.getStringArray(R.array.type_sort)

        typeSortLabel.text = resources.getString(R.string.type_sort_label, mArrayTypeSort[confPref.getSortType()].toLowerCase())

        mActionBarToolbar.title = application.getString(R.string.app_name)

        val llm = LinearLayoutManager(this)
        listTasks.layoutManager = llm

        app = App.getInstance()

        if(app.checkRelation(this.localClassName)){
            this.presenter = app.getPresenter(this.localClassName) as MainPresenterImpl
        }else{
            this.presenter = MainPresenterImpl()
            app.setPresenterToActivity(this.localClassName, this.presenter!!)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val dialogShowPass = DetailedFragment()
            val fm = this.supportFragmentManager
            dialogShowPass.show(fm,"editTask")
        }

        listTasks.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0 && !fab.isShown)
                    fab.show()
                else if (dy > 0 && fab.isShown)
                    fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort -> {
                val dialogShow = MenuFragment()
                val fm = this.supportFragmentManager
                dialogShow.show(fm,"menuFrag")
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        this.presenter!!.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        this.presenter!!.detachView()
    }

    // from Presenter
    override fun initView(tasks: MutableList<Task>) {
        adapterTask = AdapterTask(tasks, this, this)

        val dragHelper = MoveItemCallback(adapterTask, this)
        touchHelper = ItemTouchHelper(dragHelper)
        touchHelper.attachToRecyclerView(listTasks)

        listTasks.adapter = adapterTask

        this.presenter!!.firstLoadData(confPref.getSortType(), false)
    }

    override fun loadTasksView(tasks: MutableList<Task>) {
        adapterTask.updateData()
    }

    override fun addTaskView(position: Int) {
        adapterTask.addItem(position)
        if ( confPref.getConf()) {
            val mSnackbar = Snackbar.make(coordLayout, R.string.text_first_add_task, Snackbar.LENGTH_LONG)
                    .setAction("ОК", null)

            mSnackbar.show()
            confPref.setConf(false)
        }
    }

    override fun editTaskView(position: Int) {
        adapterTask.editItem(position)
    }

    override fun editTaskDialogFragment(task: Task, position: Int){
        val dialogShowPass = DetailedFragment()

        dialogShowPass.sendToEdit = true
        dialogShowPass.position = position

        dialogShowPass.task = task

        val fm = this.supportFragmentManager
        dialogShowPass.show(fm, "editTask")
    }

    override fun setAlarm(task: Task) {
        if(task.setAlarm != 0 && task.savedTime != 0){
            val timeBeforeAlarm = SetIntervalForAlarm().setInterval(task.dateTask, task.whenStartAlarm)
            val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            val broadcastIntent = Intent(this, AlarmReceiver::class.java)
            broadcastIntent.putExtra("id", task.id) //data to pass
            val pendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, 0)
            alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, timeBeforeAlarm, pendingIntent)
        }
    }
    ////////////

    // from Adapter
    override fun deleteTaskFromDB(task: Task) {
        this.presenter!!.deleteTask(task)
    }

    override fun markTaskAsDone(position: Int) {
        this.presenter!!.changeStatusTask(position)
    }

    override fun openDialogToEdit(position: Int) {
        this.presenter!!.getDataToDialog(position)
    }

    override fun updateOrderView(listTask: MutableList<Task>) {
        this.presenter!!.updateOrderPresenter(listTask)
    }
    //////////////


    // from DialogFragment TaskDB
    override fun onDialogPositiveClick(task: Task, position: Int, edit: Boolean) {
        this.presenter!!.resultDialogFragment(task, position, edit)
    }

    override fun onDialogNegativeClick() {}

    override fun showSnapBar(text: String){
        val mSnackbar = Snackbar.make(coordLayout, text, Snackbar.LENGTH_LONG).setAction("ОК", null)
        mSnackbar.show()
    }
    ///////////

    /// from Dialog Fragment sort
    override fun onFragmentInteraction(typeSort: Int) {
        confPref.setSortType(typeSort)
        this.presenter!!.firstLoadData(typeSort, true)
        typeSortLabel.text = resources.getString(R.string.type_sort_label, mArrayTypeSort[typeSort].toLowerCase())
    }

    /////////
    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }
}