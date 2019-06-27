package com.vladdolgusin.kotlintodo.app.presentation.view.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.vladdolgusin.kotlintodo.domain.model.Task
import com.vladdolgusin.kotlintodo.app.presentation.utils.AdapterInterface
import com.vladdolgusin.kotlintodo.app.presentation.utils.ItemMoveCallback
import com.vladdolgusin.kotlintodo.app.presentation.utils.StartDragListener
import com.vladdolgusin.kotlintodo.R
import java.text.SimpleDateFormat
import java.util.*

class AdapterTask(private val taskList: MutableList<Task>, startDragListener: StartDragListener, contextActivity: AdapterInterface): RecyclerView.Adapter<AdapterTask.ViewHolder>(), ItemMoveCallback {

    val mStartDragListener: StartDragListener = startDragListener

    private val mListener: AdapterInterface = contextActivity

    override fun getItemCount(): Int = taskList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(taskList[holder.adapterPosition])

        holder.itemView.findViewById<LinearLayout>(R.id.containerTask).setOnLongClickListener {
            mListener.markTaskAsDone(holder.adapterPosition)
            true
        }

        holder.itemView.findViewById<LinearLayout>(R.id.containerTask).setOnClickListener {
            mListener.openDialogToEdit(holder.adapterPosition)
        }

        holder.itemView.findViewById<CheckBox>(R.id.checkBoxTask).setOnClickListener {
            mListener.markTaskAsDone(holder.adapterPosition)
        }

        holder.itemView.findViewById<ImageView>(R.id.iconMove).setOnTouchListener( object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if(p1!!.action == MotionEvent.ACTION_DOWN){
                    mStartDragListener.requestDrag(holder)
                    p0!!.performClick()
                }
                return  true
            }
        })

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(task: Task) {
            val dateFromDB: String
            val date = Date(task.dateTask)
            dateFromDB = if (task.dateTask > 0) {
                if (task.savedTime != 0) {
                    val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US)
                    format.format(date)
                }else{
                    val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                    format.format(date)
                }
            } else {
                itemView.context.getString(R.string.default_date_task)
            }

            itemView.findViewById<CheckBox>(R.id.checkBoxTask).isChecked = task.complited == 1
            itemView.findViewById<TextView>(R.id.contentTask).text = task.textTask
            itemView.findViewById<TextView>(R.id.dateTask).text =  dateFromDB
            itemView.findViewById<TextView>(R.id.priorityTask).text = task.priority.toString()
            itemView.findViewById<ImageView>(R.id.imageAlarm).visibility = if (task.setAlarm != 0) View.VISIBLE else View.GONE
            if(task.complited == 0){
                if ( Date().time > task.dateTask && task.dateTask != 0L) {
                    itemView.findViewById<LinearLayout>(R.id.itemList).setBackgroundColor(Color.parseColor("#f86c47"))
                } else{
                    itemView.findViewById<LinearLayout>(R.id.itemList).setBackgroundColor(Color.parseColor("#d9d9d9"))
                }
            }else{
                itemView.findViewById<LinearLayout>(R.id.itemList).setBackgroundColor(Color.parseColor("#53b45a"))
            }
        }
    }

    fun updateData() {
        notifyDataSetChanged()
    }

    fun addItem(position: Int){
        notifyItemInserted(position)
    }

    override fun removeTask(position: Int){
        mListener.deleteTaskFromDB(taskList[position])
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int){
        notifyItemChanged(position)
    }

    internal inner class StudentComparator : Comparator<Task> {
        override fun compare(contact1: Task, contact2: Task): Int {
            return if (contact1.complited > contact2.complited) {
                1
            } else {
                -1
            }
        }
    }

    override fun onRowMoved(from: Int, to: Int) {
        Collections.swap(taskList, from, to)
        notifyItemMoved(from, to)
    }

    override fun onRowSelected(mViewHolder: ViewHolder) {

    }

    override fun onRowClear(mViewHolder: ViewHolder) {
        mListener.updateOrderView(taskList)
    }
}