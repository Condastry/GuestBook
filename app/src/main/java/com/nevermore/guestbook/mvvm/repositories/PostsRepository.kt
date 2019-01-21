package com.nevermore.guestbook.mvvm.repositories

import android.arch.lifecycle.MutableLiveData
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.data.Answer
import com.nevermore.guestbook.data.Comment
import com.nevermore.guestbook.data.bodies.AnswerBody
import com.nevermore.guestbook.navigation.MainScreens
import com.nevermore.guestbook.retrofit.PusherCommentsService
import com.nevermore.guestbook.tools.Page
import com.nevermore.guestbook.tools.SocketIOUtils.PRIVATE_EVENT
import com.nevermore.guestbook.tools.SocketIOUtils.PUBLIC_EVENT
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router

class PostsRepository(
    private val commentsService: PusherCommentsService,
    private val publicSocket: Socket,
    private val privateSocket: Socket,
    router: Router,
    prefs: AppPreferences
) : BaseRepository(router, prefs) {
    private val credentials = prefs.userCredentials
    val isAdmin  = credentials.isAdmin != 0
    private val token = credentials.apiToken
    private var currentPage = 0
    private var isPageLoading = false
    val loadedPage = MutableLiveData<Page<Comment>>()

    private val publicPushListener = Emitter.Listener {

    }

    private val privatePushListener = Emitter.Listener {

    }

    init {
        publicSocket.on(PUBLIC_EVENT, publicPushListener)
        privateSocket.on(PRIVATE_EVENT, privatePushListener)
    }

    fun loadNextPage() {
        if(isPageLoading) return

        isPageLoading = true
        subscriptions.add(
            commentsService.getComments(token, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({data ->
                    val page = Page<Comment>(++currentPage)
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

                    page.onLoadListener = object : Page.OnPageLoadedListener<Comment>{
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

    fun sendAnswer(answer : Answer){
        executeDefaultRequest(
            commentsService.storeAnswer(answer.commentId, AnswerBody(token, answer.message))
        )
    }

    fun makeComment(){
        router.navigateTo(MainScreens.MAKE_COMMENT_SCREEN)
    }

    override fun clear() {
        super.clear()
        publicSocket.disconnect()
        privateSocket.disconnect()
        publicSocket.off(PRIVATE_EVENT, publicPushListener)
        privateSocket.off(PRIVATE_EVENT, privatePushListener)
    }


}