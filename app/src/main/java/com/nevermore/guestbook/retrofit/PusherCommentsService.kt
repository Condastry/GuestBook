package com.nevermore.guestbook.retrofit

import com.nevermore.guestbook.data.*
import com.nevermore.guestbook.data.bodies.AnswerBody
import com.nevermore.guestbook.data.bodies.CommentBody
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PusherCommentsService {

    @GET("comment/")
    fun getComments(
        @Query("api_token") apiToken: String,
        @Query("page") page: Int
    ): Observable<Data>

    @GET("comment/{commentID}/answer/")
    fun getAnswersFor(
        @Path("commentID") commentID : Int,
        @Query("api_token") apiToken: String

    ): Observable<List<Answer>>


    @POST("comment/{commentID}/answer/")
    fun storeAnswer(
        @Path("commentID") commentID : Int,
        @Body answerBody: AnswerBody
    ): Observable<Response<ResponseBody>>

    @POST("comment/")
    fun storeCommnet(
        @Body commentBody: CommentBody
    ): Observable<Response<ResponseBody>>

}