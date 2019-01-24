package com.nevermore.guestbook.mvvm.repositories

import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.navigation.MainScreens
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class MainRepository(
    val cicerone: Cicerone<Router>,
    prefs: AppPreferences
) : BaseRepository(cicerone.router, prefs) {

    init {
        if (prefs.userCredentials.apiToken.isEmpty()) {
            router.newRootScreen(MainScreens.AUTH_SCREEN)
        } else {
            router.newRootScreen(MainScreens.POSTS_SCREEN)
        }
    }
}