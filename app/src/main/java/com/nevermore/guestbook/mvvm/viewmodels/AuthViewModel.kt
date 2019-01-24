package com.nevermore.guestbook.mvvm.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.nevermore.guestbook.mvvm.repositories.AuthRepository
import java.io.File

class AuthViewModel : BaseViewModel<AuthRepository>() {
    val isLogin = MutableLiveData<Boolean>().apply { value = true }
    var imageFile: File? = null

    fun login(email: String, password: String) {
        repository!!.login(email, password)
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        repository!!.register(name, email, password, confirmPassword, imageFile!!)
    }
}