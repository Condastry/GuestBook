package com.nevermore.guestbook.mvvm.viewmodels

import com.github.nkzawa.socketio.client.Socket
import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.mvvm.repositories.MainRepository
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

class MainViewModel : BaseViewModel<MainRepository>(){
    val router : Router
        get() = repository!!.cicerone.router
    val navigatorHolder : NavigatorHolder
        get() = repository!!.cicerone.navigatorHolder

    var prefs : AppPreferences? = null
    var publicSocet : Socket? = null
    var privateSocket : Socket? = null
}