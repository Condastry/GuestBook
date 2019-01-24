package com.nevermore.guestbook.mvvm.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.github.nkzawa.socketio.client.IO
import com.nevermore.guestbook.R
import com.nevermore.guestbook.mvvm.repositories.PostsRepository
import com.nevermore.guestbook.mvvm.viewmodels.PostsViewModel
import com.nevermore.guestbook.retrofit.PusherCommentsService
import com.nevermore.guestbook.retrofit.PusherServiceProvider
import com.nevermore.guestbook.tools.SocketIOUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment : BaseFragment() {
    override val contentLayoutID = R.layout.fragment_posts
    override val toolbarTitle = "Comments"
    override val isBackable = false
    override val inputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
    private lateinit var vm: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        vm.initRepository(
            PostsRepository(
                PusherServiceProvider.createService(PusherCommentsService::class.java),
                IO.socket(SocketIOUtils.HOST),
                mainVM.router,
                mainVM.prefs!!
            )
        )
        addEventEmmiter(vm.eventEmmiter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        fab.setOnClickListener { vm.makeComment() }
    }

    private fun setupRecycler() {
        recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            adapter = vm.adapter
            addOnScrollListener(vm.scrollListener)
        }
    }

    override fun onEvent(event: String) {
        super.onEvent(event)
        when (event) {
            vm.EVENT_COMMENT_PUSH -> {
                (recycler.layoutManager as LinearLayoutManager).apply {
                    if (findFirstCompletelyVisibleItemPosition() <= 2) {
                        scrollToPosition(0)
                    }
                }
            }
        }
    }
}