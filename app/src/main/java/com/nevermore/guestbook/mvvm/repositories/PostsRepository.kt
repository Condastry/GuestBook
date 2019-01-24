package com.nevermore.guestbook.mvvm.repositories

import android.arch.lifecycle.MutableLiveData
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.data.Answer
import com.nevermore.guestbook.data.Comment
import com.nevermore.guestbook.data.bodies.AnswerRequestBody
import com.nevermore.guestbook.navigation.MainScreens
import com.nevermore.guestbook.retrofit.PusherCommentsService
import com.nevermore.guestbook.tools.Page
import com.nevermore.guestbook.tools.SocketIOUtils
import com.nevermore.guestbook.tools.SocketIOUtils.EVENT_PUBLIC_PUSH
import com.nevermore.guestbook.tools.SocketIOUtils.EVENT_USER_PUSH
import com.nevermore.guestbook.tools.SocketIOUtils.PRIVATE_USER
import com.nevermore.guestbook.tools.SocketIOUtils.PUBLIC_PUSH
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ru.terrakok.cicerone.Router

class PostsRepository(
    private val commentsService: PusherCommentsService,
    private val socket: Socket,
    router: Router,
    prefs: AppPreferences
) : BaseRepository(router, prefs) {
    private val credentials = prefs.userCredentials
    val isAdmin = credentials.isAdmin != 0
    private val token = credentials.apiToken
    private var currentPage = 1
    private var isPageLoading = false

    val loadedPage = MutableLiveData<Page<Comment>>()
    val pushedComment = MutableLiveData<Comment>()
    val pushedAnswer = MutableLiveData<Answer>()

    private val publicPushListener = Emitter.Listener {
        val commentJson = (it[1] as JSONObject).getJSONObject("comment")
        pushedComment.postValue(
            Gson().fromJson(commentJson.toString(), Comment::class.java).apply {
                isPushed = true
                id = commentJson.getInt("id")
            }
        )
    }

    private val privatePushListener = Emitter.Listener {
        pushedAnswer.postValue(
            Gson().fromJson(
                (it[1] as JSONObject).getJSONObject("data").getJSONObject("answer").toString(),
                Answer::class.java
            ).apply { isPushed = true }
        )
    }

    init {
        socket.on(EVENT_PUBLIC_PUSH, publicPushListener)
        socket.on(EVENT_USER_PUSH, privatePushListener)
        socket.connect()
        socket.emit(
            SocketIOUtils.SUBSCRIBE,
            SocketIOUtils.getSubscriptionBody(PUBLIC_PUSH, credentials.name, credentials.apiToken)
        )
        socket.emit(
            SocketIOUtils.SUBSCRIBE,
            SocketIOUtils.getSubscriptionBody(
                PRIVATE_USER + credentials.id, credentials.name, credentials.apiToken
            )
        )
    }

    fun loadNextPage() {
        if (isPageLoading) return

        isPageLoading = true
        subscriptions.add(
            commentsService.getComments(token, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ data ->
                    val page = Page<Comment>(currentPage++)
                    data.data.forEach { page.addItem(it) }
                    page.items.forEach { item ->
                        subscriptions.add(
                            commentsService.getAnswersFor(item.value.id, token)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ answers ->
                                    item.value.answers = answers.toMutableList()
                                    item.isLoaded = true
                                }, {
                                    it.printStackTrace()
                                    item.isLoaded = true
                                })
                        )
                    }

                    page.onLoadListener = object : Page.OnPageLoadedListener<Comment> {
                        override fun onPageLoaded(lp: Page<Comment>) {
                            isPageLoading = false
                            loadedPage.value = lp
                        }
                    }
                }, {
                    it.printStackTrace()
                })
        )

    }

    fun sendAnswer(answer: Answer) {
        executeDefaultRequest(
            commentsService.storeAnswer(answer.commentId, AnswerRequestBody(token, answer.message))
        )
    }

    fun makeComment() {
        router.navigateTo(MainScreens.MAKE_COMMENT_SCREEN)
    }

    override fun clear() {
        super.clear()
        socket.disconnect()
        socket.off(EVENT_PUBLIC_PUSH, publicPushListener)
        socket.off(EVENT_USER_PUSH, privatePushListener)
    }
}