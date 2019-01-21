package com.nevermore.guestbook.mvvm.viewmodels

import com.nevermore.guestbook.mvvm.repositories.PostsRepository
import com.nevermore.guestbook.recycler.PostsAdapter
import com.nevermore.guestbook.recycler.RecyclerScrollListener


class PostsViewModel : BaseViewModel<PostsRepository>() {
    private var lastPageNumber = 0
    var adapter : PostsAdapter? = null
    val scrollListener = RecyclerScrollListener().apply {
        loadItemEvent = { repository?.loadNextPage() }
    }


    override fun onRepositoryInited() {
        super.onRepositoryInited()
        adapter = PostsAdapter(repository!!.isAdmin ).apply {
            sendAnswer = {repository!!.sendAnswer(it)}
        }

        repository!!.loadedPage.observeForever {
            if (it!!.number > lastPageNumber) {
                val oldCount = adapter!!.items.size
                adapter!!.items.addAll(it.items.map { it.value })
                adapter!!.notifyItemRangeInserted(oldCount, adapter!!.itemCount - 1)
                lastPageNumber++
            }
        }
        repository!!.loadNextPage()
    }

    fun makeComment() = repository!!.makeComment()


}