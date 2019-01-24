package com.nevermore.guestbook.mvvm.viewmodels

import android.arch.lifecycle.ViewModel
import com.nevermore.guestbook.mvvm.repositories.BaseRepository
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<BR : BaseRepository> : ViewModel() {
    private var isRepositoryInited = false
    protected var repository: BR? = null
    val eventEmmiter = PublishSubject.create<String>()

    fun initRepository(repo: BR) {
        if (!isRepositoryInited) {
            repository = repo
            isRepositoryInited = true
            onRepositoryInited()
        }
    }

    protected open fun onRepositoryInited() {}

    fun logout() {
        repository!!.logout()
    }

    protected fun sendEvent(eventStr: String) {
        eventEmmiter.onNext(eventStr)
    }

    override fun onCleared() {
        super.onCleared()
        repository?.clear()
        repository = null
    }
}