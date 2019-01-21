package com.nevermore.guestbook.mvvm.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.nevermore.guestbook.R
import com.nevermore.guestbook.mvvm.repositories.PostsRepository
import com.nevermore.guestbook.mvvm.viewmodels.PostsViewModel
import com.nevermore.guestbook.retrofit.PusherCommentsService
import com.nevermore.guestbook.retrofit.PusherServiceProvider
import kotlinx.android.synthetic.main.fragment_make_comment.*
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment : BaseFragment() {
    override val contentLayoutID = R.layout.fragment_posts
    override val toolbarTitle = "Comments"
    override val isBackable = false
    private lateinit var vm: PostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        vm.initRepository(
            PostsRepository(
                PusherServiceProvider.createService(PusherCommentsService::class.java),
                mainVM.publicSocet!!,
                mainVM.privateSocket!!,
                mainVM.router,
                mainVM.prefs!!
            )
        )
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
}