package com.nevermore.guestbook.app

import android.app.Application
import android.content.Context
import com.nevermore.guestbook.data.User

class AppPreferences(private val app: Application) {
    private val PREFS_NAME = "GUEST_BOOK_PREFS"

    private val USER_ID = "USER_ID"
    private val USER_NAME = "USER_NAME"
    private val USER_EMAIL = "USER_EMAIL"
    private val USER_IS_ADMIN = "USER_IS_ADMIN"
    private val USER_TOKEN = "USER_TOKEN"


    var userCredentials: User
        get() {
            return User(
                getInt(USER_ID),
                getString(USER_NAME),
                getString(USER_EMAIL),
                getInt(USER_IS_ADMIN),
                getString(USER_TOKEN)
            )
        }
        set(user) {
            putInt(USER_ID, user.id)
            putString(USER_NAME, user.name)
            putString(USER_EMAIL, user.email)
            putInt(USER_IS_ADMIN, user.isAdmin)
            putString(USER_TOKEN, user.apiToken)
        }


    fun clearUserCredetials() {
        userCredentials = User()
    }

    private fun getString(name: String): String {
        return app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(name, "")!!
    }

    private fun putString(name: String, value: String) {
        app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(name, value).apply()
    }

    private fun getInt(name: String): Int {
        return app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(name, 0)
    }

    private fun putInt(name: String, value: Int) {
        app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putInt(name, value).apply()
    }
}