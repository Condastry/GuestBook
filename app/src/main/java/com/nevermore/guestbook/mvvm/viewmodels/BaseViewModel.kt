package com.nevermore.guestbook.mvvm.viewmodels

import android.arch.lifecycle.ViewModel
import com.nevermore.guestbook.mvvm.repositories.BaseRepository

abstract class BaseViewModel<BR : BaseRepository> : ViewModel(){
    private var isRepositoryInited = false
    protected var repository: BR? = null

    fun initRepository(repo : BR){
        if(!isRepositoryInited){
            repository = repo
            isRepositoryInited = true
            onRepositoryInited()
        }
    }

    protected open fun onRepositoryInited(){}

    fun logout(){
        repository!!.logout()
    }

    override fun onCleared() {
        super.onCleared()
        repository?.clear()
        repository = null
    }
}