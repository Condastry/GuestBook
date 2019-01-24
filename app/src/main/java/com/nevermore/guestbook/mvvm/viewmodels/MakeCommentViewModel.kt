package com.nevermore.guestbook.mvvm.viewmodels

import com.nevermore.guestbook.mvvm.repositories.MakeCommentRepository

class MakeCommentViewModel : BaseViewModel<MakeCommentRepository>() {

    fun makeComment(title: String, message: String) {
        repository!!.makeComment(title, message)
    }
}