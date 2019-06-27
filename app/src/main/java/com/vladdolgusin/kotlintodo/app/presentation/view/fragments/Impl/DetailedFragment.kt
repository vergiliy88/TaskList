package com.vladdolgusin.kotlintodo.app.presentation.view.fragments.Impl

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.vladdolgusin.kotlintodo.domain.model.Task
import com.vladdolgusin.kotlintodo.R
import com.vladdolgusin.kotlintodo.app.presentation.view.activity.IDialogEditTask
import com.vladdolgusin.kotlintodo.app.presentation.utils.SetIntervalForAlarm
import java.text.SimpleDateFormat
import java.util.*

class DetailedFragment: DialogFragment(){

    private lateinit var taskText: EditText
    private lateinit var taskDate: TextView
    private lateinit var taskTime: TextView
    private lateinit var errorTextView: TextView
    private lateinit var taskPrior: Spinner
    private lateinit var checkBoxSetAlarm: CheckBox
    private lateinit var spinnerAlarmOn: Spinner

    var position: Int = 0
    var sendToEdit: Boolean = false

    var task: Task = Task(0, "", 0, 0, 0, 0, 0, 0, 0)

    private var mTitleAlert: String = ""
    private var mListener: IDialogEditTask? = null

    private var listOfItems = arrayOf(1, 2, 3, 4, 5)
    private var cal = Calendar.getInstance()

    override fun onAttach(context:Context ) {
        super.onAttach(context)
        if (mListener == null) {
            mListener = activity as IDialogEditTask
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val detailedForm = activity!!.layoutInflater.inflate(R.layout.alert_view, null)

        taskText = detailedForm.findViewById(R.id.taskText) as EditText
        taskDate = detailedForm.findViewById(R.id.taskDate) as TextView
        taskTime = detailedForm.findViewById(R.id.timePicker) as TextView
        errorTextView = detailedForm.findViewById(R.id.errorTextView) as TextView
        checkBoxSetAlarm = detailedForm.findViewById(R.id.checkBoxSetAlarm) as CheckBox
        taskPrior = detailedForm.findViewById(R.id.spinnerPtiority) as Spinner
        spinnerAlarmOn = detailedForm.findViewById(R.id.spinnerAlarmOn) as Spinner

        errorTextView.visibility = View.GONE

        spinnerAlarmOn.isEnabled = false

        val alarmBefore = getResources().getStringArray(R.array.alarm_before)

        val adapterPrior = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, listOfItems)
        adapterPrior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskPrior.adapter = adapterPrior

        val adapterAlarmBefore = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, alarmBefore)
        adapterAlarmBefore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAlarmOn.adapter = adapterAlarmBefore

        checkBoxSetAlarm.isEnabled = false

        if (sendToEdit) {
            taskText.setText(task.textTask)
            taskDate.text = convertLongToDate(task.dateTask, true)
            taskTime.text = convertLongToDate(task.dateTask, false)
            taskPrior.setSelection(listOfItems.indexOf(task.priority))
            spinnerAlarmOn.setSelection(task.whenStartAlarm)
            mTitleAlert = activity!!.getString(R.string.title_alert_edit_task)
            if (task.savedTime != 0) {
                checkBoxSetAlarm.isEnabled = true
            }
            if (task.setAlarm != 0) {
                checkBoxSetAlarm.isChecked = true
                spinnerAlarmOn.isEnabled = true
            }
        }else{
            mTitleAlert = activity!!.getString(R.string.title_alert_new_task)
        }

        checkBoxSetAlarm.setOnCheckedChangeListener { _, isChecked ->
            run {
                spinnerAlarmOn.isEnabled = isChecked
                task.setAlarm = if (isChecked) 1 else 0
                if (SetIntervalForAlarm().setInterval(task.dateTask, task.whenStartAlarm) < Date().time && task.dateTask != 0L) {
                    errorTextView.visibility = View.VISIBLE
                }else{
                    errorTextView.visibility = View.GONE
                }
            }
        }

        taskPrior.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                task.priority = listOfItems[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        spinnerAlarmOn.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                task.whenStartAlarm = position
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                task.whenStartAlarm = 0
            }
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        taskDate.setOnClickListener {
            if (task.dateTask == 0L) {
                DatePickerDialog(context!!,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }else{
                val customDate = DatePickerDialog(context!!,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH))

                var arrayOfDate = taskDate.text.split(".")

                customDate.show()
                customDate.updateDate(arrayOfDate[2].toInt(), arrayOfDate[1].toInt() - 1, arrayOfDate[0].toInt())
            }
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            run {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                task.savedTime = 1
                updateDateInView()
            }
        }

        taskTime.setOnClickListener {
            TimePickerDialog(context!!,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), true)
            .show()
        }

        val  builder = AlertDialog.Builder(activity!!)
                .setTitle(mTitleAlert)
                .setView(detailedForm)
                .setPositiveButton(R.string.ok_fragment, null)
                .setNegativeButton(R.string.cansek_fragment, null)
                .setCancelable(false)
                .create()

        builder.setOnShowListener(fun(_: DialogInterface?) {
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if(taskText.text.isEmpty()){
                    taskText.setHint(R.string.hint_task_fragment)
                    taskText.setHintTextColor(Color.RED)
                }else{
                    if (SetIntervalForAlarm().setInterval(task.dateTask, task.whenStartAlarm) < Date().time && task.dateTask != 0L && task.savedTime == 1) {
                        errorTextView.visibility = View.VISIBLE
                    }else{
                        task.textTask = taskText.text.toString().trim()
                        mListener!!.onDialogPositiveClick(task, position, sendToEdit)
                        dialog.dismiss()
                    }
                }
            }
        })
        return builder
    }

    private fun updateDateInView() {
        errorTextView.visibility = View.GONE

        task.dateTask = cal.time.time

        if (task.savedTime != 0) {
            val formatTimeTask = SimpleDateFormat("HH:mm", Locale.US)
            taskTime.text = formatTimeTask.format(cal.time)
            checkBoxSetAlarm.isEnabled = cal.time.time > Date().time
        }

        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        val today = formatter.format(cal.time)
        taskDate.text = today
    }

    private fun convertLongToDate(date: Long, dateView: Boolean): String {
        var dateToField = ""
        var dateNow: Date
        if (date > 0) {
            if (dateView) {
                val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                dateNow = Date(date)
                dateToField = format.format(dateNow)
            } else {
                if (task.savedTime != 0) {
                    val format = SimpleDateFormat("HH:mm", Locale.US)
                    dateNow = Date(date)
                    dateToField = format.format(dateNow)
                }
            }
        }else{
            dateToField = ""
        }
        return dateToField
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroyView()
    }
}

