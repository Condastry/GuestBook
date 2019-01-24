package com.nevermore.guestbook.mvvm.repositories

import com.nevermore.guestbook.app.AppPreferences
import com.nevermore.guestbook.data.bodies.CommentRequestBody
import com.nevermore.guestbook.navigation.MainScreens
import com.nevermore.guestbook.retrofit.PusherCommentsService
import ru.terrakok.cicerone.Router

class MakeCommentRepository(
    private val commentsService: PusherCommentsService,
    router: Router,
    prefs: AppPreferences
) : BaseRepository(router, prefs) {
    private val token = prefs.userCredentials.apiToken

    fun makeComment(title: String, message: String) {
        if (checkIsEmpty(title)) return
        if (checkIsEmpty(message)) return

        executeDefaultRequest(
            commentsService.storeCommnet(CommentRequestBody(token, title, message))
        ) {
            router.backTo(MainScreens.POSTS_SCREEN)
        }
    }
}