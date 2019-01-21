package com.nevermore.guestbook.mvvm.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.github.nkzawa.socketio.client.Manager
import com.nevermore.guestbook.R
import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.mvvm.repositories.MainRepository
import com.nevermore.guestbook.mvvm.viewmodels.MainViewModel
import com.nevermore.guestbook.navigation.MainNavigator
import com.nevermore.guestbook.tools.SocketIOUtils.HOST
import com.nevermore.guestbook.tools.SocketIOUtils.PRIVATE_USER
import com.nevermore.guestbook.tools.SocketIOUtils.PUBLIC_PUSH
import ru.terrakok.cicerone.Cicerone
import java.net.URI


class MainActivity : AppCompatActivity() {

    private val navigator = MainNavigator(this, R.id.container)
    private lateinit var vm: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this).apply { id = R.id.container })
        vm = ViewModelProviders.of(this).get(MainViewModel::class.java)
        if (savedInstanceState == null) {
            val prefs = AppPreferences(application)

            vm.initRepository(
                MainRepository(
                    Cicerone.create(),
                    prefs
                )
            )
            vm.prefs = prefs

            val manager = Manager(URI(HOST))
            vm.publicSocet = manager.socket(PUBLIC_PUSH)
            vm.privateSocket = manager.socket(PRIVATE_USER + prefs.userCredentials.id)
        }
    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        vm.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        vm.navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        vm.router.exit()
    }
}
