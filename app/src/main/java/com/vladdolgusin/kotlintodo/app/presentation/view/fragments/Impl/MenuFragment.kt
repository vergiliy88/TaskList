package com.vladdolgusin.kotlintodo.app.presentation.view.fragments.Impl

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vladdolgusin.kotlintodo.R
import com.vladdolgusin.kotlintodo.app.DI.MainModule

class MenuFragment : DialogFragment() {

    private var listener: OnFragmentInteractionListener? = null
    private var selectedFilterType: Int = 0
    private val confPref = MainModule.getConsPresSettings()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val catNamesArray = getResources().getStringArray(R.array.type_sort)

        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.sort_fragment_title)
                .setSingleChoiceItems(catNamesArray, confPref.getSortType()
                ) { _, item ->
                    selectedFilterType = item
                }
                .setPositiveButton(R.string.ok_fragment) { _, _ ->
                    listener!!.onFragmentInteraction(selectedFilterType)
                }
                .setNegativeButton(R.string.cansek_fragment) { _, _ ->

                }

        return builder.create()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(typeSort:Int)
    }
}
