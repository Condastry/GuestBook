package com.nevermore.guestbook.mvvm.repositories

import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.data.bodies.LoginBody
import com.nevermore.guestbook.data.User
import com.nevermore.guestbook.navigation.MainScreens
import com.nevermore.guestbook.retrofit.PusherAuthService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.terrakok.cicerone.Router
import java.io.File


class AuthRepository(
    private val authService: PusherAuthService,
    router: Router,
    prefs: AppPreferences
) : BaseRepository(router, prefs) {

    fun login(email: String, password: String) {
        if(checkIsEmpty(email)) return
        if(checkIsEmpty(password)) return

        authRequest(authService.login(LoginBody(email, password)))
    }

    fun register(name: String, email: String, password: String, confirmPassword: String, image: File) {
        if (password != confirmPassword) {
            showMessage("Invalid password!")
            return
        }
       if(checkIsEmpty(name)) return
       if(checkIsEmpty(email)) return
       if(checkIsEmpty(password)) return

        val imageBody = MultipartBody.Part.createFormData(
            "avatar", image.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), image)
        )

       authRequest(authService.register(toBody(name), toBody(email), toBody(password), imageBody))
    }

    private fun authRequest(request: Observable<User>) {
        subscriptions.add(
            request
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    prefs.userCredentials = it
                    router.newRootScreen(MainScreens.POSTS_SCREEN)
                }, {
                    showMessage("Registration error!")
                })
        )
    }

    private fun toBody(str: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), str)
    }
}