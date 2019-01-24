package com.nevermore.guestbook.mvvm.viewmodels

import com.nevermore.guestbook.mvvm.repositories.PostsRepository
import com.nevermore.guestbook.recycler.PostsAdapter
import com.nevermore.guestbook.recycler.RecyclerScrollListener


class PostsViewModel : BaseViewModel<PostsRepository>() {
    val EVENT_COMMENT_PUSH = "EVENT_COMMENT_PUSH"
    private var lastPageNumber = 0
    var adapter: PostsAdapter? = null

    val scrollListener = RecyclerScrollListener().apply {
        loadItemEvent = { repository?.loadNextPage() }
    }

    override fun onRepositoryInited() {
        super.onRepositoryInited()
        adapter = PostsAdapter(repository!!.isAdmin).apply {
            sendAnswer = { repository!!.sendAnswer(it) }
        }

        repository!!.apply {
            loadedPage.observeForever {
                if (it!!.number > lastPageNumber) {
                    val oldCount = adapter!!.items.size
                    adapter!!.items.addAll(it.items.map { it.value })
                    adapter!!.notifyItemRangeInserted(oldCount, adapter!!.itemCount - 1)
                    lastPageNumber++
                }
            }
            loadNextPage()

            pushedComment.observeForever {
                adapter!!.apply {
                    items.add(0, it!!)
                    notifyItemInserted(0)
                }
                sendEvent(EVENT_COMMENT_PUSH)
            }

            pushedAnswer.observeForever { answer ->
                adapter!!.items.find {
                    it.id == answer!!.commentId
                }?.let { comment ->
                    comment.answers.apply {
                        forEach {
                            if (it.id <= 0 && it.message == answer!!.message) remove(it)
                        }
                        add(answer!!.apply { isPushed = true })
                        adapter!!.notifyItemChanged(adapter!!.items.indexOf(comment))
                    }
                }
            }
        }
    }

    fun makeComment() = repository!!.makeComment()
}