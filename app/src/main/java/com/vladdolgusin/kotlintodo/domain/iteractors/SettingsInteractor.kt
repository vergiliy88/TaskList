package com.vladdolgusin.kotlintodo.domain.iteractors

interface SettingsInteractor {

    fun getSettingsBootFromPref(): Boolean
    fun setSettingsBootFromPref(value: Boolean)

    fun getTypeSortFromPref(): Int
    fun setTypeSortFromPref(type: Int)
}