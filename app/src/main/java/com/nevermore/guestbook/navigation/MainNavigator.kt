package com.nevermore.guestbook.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nevermore.guestbook.R
import com.nevermore.guestbook.mvvm.ui.AuthFragment
import com.nevermore.guestbook.mvvm.ui.MakeCommentFragment
import com.nevermore.guestbook.mvvm.ui.PostsFragment
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import ru.terrakok.cicerone.commands.Command

class MainNavigator(private val activity: AppCompatActivity, containerID : Int) :
    SupportFragmentNavigator(activity.supportFragmentManager, containerID) {

    override fun exit() {
        activity.finish()
    }

    override fun showSystemMessage(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun createFragment(screenKey: String?, data: Any?): Fragment {
        return when (screenKey) {
            MainScreens.AUTH_SCREEN -> AuthFragment()
            MainScreens.POSTS_SCREEN -> PostsFragment()
            MainScreens.MAKE_COMMENT_SCREEN -> MakeCommentFragment()
            else -> Fragment()
        }
    }

    override fun setupFragmentTransactionAnimation(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment,
        fragmentTransaction: FragmentTransaction
    ) {
        currentFragment?.let {
            fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }

    }
}
