package com.nevermore.guestbook.mvvm.repositories

import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.navigation.MainScreens
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import ru.terrakok.cicerone.Router

abstract class BaseRepository(
    protected val router : Router,
    protected val prefs : AppPreferences
){

    protected val subscriptions = CompositeDisposable()

    protected fun executeDefaultRequest(
        request: Observable<Response<ResponseBody>>, onResponse : () -> Unit = {}
    ) {
        subscriptions.add(
            request
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                   onResponse.invoke()
                }, {
                    showMessage("Reguest error!")
                })
        )
    }

    protected open fun showMessage(message : String){
        router.showSystemMessage(message)
    }

    protected open fun checkIsEmpty(value : String) : Boolean{
        if(value.isEmpty()){
            showMessage("Please, input all values")
            return true
        }
        return false
    }

    fun logout(){
        prefs.clearUserCredetials()
        router.newRootScreen(MainScreens.AUTH_SCREEN)
    }

    open fun clear(){
        subscriptions.clear()
    }
}