package com.nevermore.guestbook.mvvm.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.nevermore.guestbook.R
import com.nevermore.guestbook.mvvm.repositories.MakeCommentRepository
import com.nevermore.guestbook.mvvm.viewmodels.MakeCommentViewModel
import com.nevermore.guestbook.retrofit.PusherCommentsService
import com.nevermore.guestbook.retrofit.PusherServiceProvider
import kotlinx.android.synthetic.main.fragment_make_comment.*

class MakeCommentFragment : BaseFragment(){
    override val contentLayoutID = R.layout.fragment_make_comment
    override val toolbarTitle = "Make comment"
    private lateinit var vm: MakeCommentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(MakeCommentViewModel::class.java)
        vm.initRepository(
            MakeCommentRepository(
                PusherServiceProvider.createService(PusherCommentsService::class.java),
                mainVM.publicSocet!!,
                mainVM.router,
                mainVM.prefs!!
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnMake.setOnClickListener {
            vm.makeComment(inputTitle.text, inputMessage.text)
        }
    }
}