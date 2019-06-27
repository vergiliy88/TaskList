package com.vladdolgusin.kotlintodo.domain.repository

interface SharedPrefRepository {
    fun getSortType (): Int
    fun setSortType (type :Int)
    fun setConf (conf: Boolean)
    fun getConf (): Boolean
}