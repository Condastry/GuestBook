package com.nevermore.guestbook.mvvm.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.nevermore.guestbook.R
import com.nevermore.guestbook.mvvm.repositories.AuthRepository
import com.nevermore.guestbook.mvvm.viewmodels.AuthViewModel
import com.nevermore.guestbook.retrofit.PusherAuthService
import com.nevermore.guestbook.retrofit.PusherServiceProvider
import com.nevermore.guestbook.tools.isGone
import kotlinx.android.synthetic.main.fragment_auth.*
import java.io.File

class AuthFragment : BaseFragment() {
    override val contentLayoutID = R.layout.fragment_auth
    override val isToolbarVisible = false
    private lateinit var vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        vm.initRepository(
            AuthRepository(
                PusherServiceProvider.createService(PusherAuthService::class.java),
                mainVM.router,
                mainVM.prefs!!
            )
        )
        vm.imageFile = createImageFile()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupModeObserver()
        setupClickListeners()
    }

    private fun createImageFile(): File {
        return File(
            context!!.externalCacheDir!!.absolutePath + File.separator + "image.png"
        ).apply { createNewFile() }
    }

    private fun setupModeObserver() {
        vm.isLogin.observe(this, Observer { isLogin ->
            tvTitle.text = if (isLogin!!) getString(R.string.welcome) else getString(R.string.create_acc)
            tvSignIn.text = if (isLogin) getString(R.string.register) else getString(R.string.sign_in)
            inputConfirmPass.isGone(isLogin)
            inputName.isGone(isLogin)
            btnLogin.text = if (isLogin) getString(R.string.sign_in) else getString(R.string.register)
            tvHaveAcc.text = if (isLogin) getString(R.string.no_acc) else getString(R.string.have_acc)
        })
    }

    private fun setupClickListeners() {
        tvSignIn.setOnClickListener {
            vm.isLogin.value = !vm.isLogin.value!!
        }
        btnLogin.setOnClickListener {
            if (vm.isLogin.value!!) {
                vm.login(inputEmail.text, inputPassword.text)
            } else {
                vm.register(inputName.text, inputEmail.text, inputPassword.text, inputConfirmPass.text)
            }
        }
    }
}