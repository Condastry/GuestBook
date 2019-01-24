package com.nevermore.guestbook.mvvm.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.nevermore.guestbook.R
import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.mvvm.repositories.MainRepository
import com.nevermore.guestbook.mvvm.viewmodels.MainViewModel
import com.nevermore.guestbook.navigation.MainNavigator
import ru.terrakok.cicerone.Cicerone


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
                MainRepository(Cicerone.create(), prefs)
            )
            vm.prefs = prefs
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
