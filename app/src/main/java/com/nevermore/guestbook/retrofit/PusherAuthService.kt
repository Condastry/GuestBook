package com.nevermore.guestbook.retrofit

import com.nevermore.guestbook.data.bodies.LoginBody
import com.nevermore.guestbook.data.User
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface PusherAuthService {

    @Multipart
    @POST("auth/register")
    fun register(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part
    ): Observable<User>

    @POST("auth/login")
    fun login(
        @Body loginBody: LoginBody
    ): Observable<User>

}