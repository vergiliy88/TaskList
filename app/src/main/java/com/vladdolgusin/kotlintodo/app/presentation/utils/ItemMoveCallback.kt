package com.vladdolgusin.kotlintodo.app.presentation.utils

import com.vladdolgusin.kotlintodo.app.presentation.view.adapters.AdapterTask

interface ItemMoveCallback {
    fun onRowMoved(from: Int, to: Int)
    fun onRowSelected(mViewHolder: AdapterTask.ViewHolder)
    fun onRowClear(mViewHolder: AdapterTask.ViewHolder)
    fun removeTask(position: Int)
}