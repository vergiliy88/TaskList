package com.vladdolgusin.kotlintodo.app.presentation.utils

import android.support.v7.widget.RecyclerView

interface StartDragListener {
    fun requestDrag(viewHolder: RecyclerView.ViewHolder)
}