package com.nevermore.guestbook.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nevermore.guestbook.data.Answer
import com.nevermore.guestbook.data.Comment


class PostsAdapter(private val isAdmin : Boolean) : RecyclerView.Adapter<PostViewHolder>() {
    var items = mutableListOf<Comment>()
    var sendAnswer: ((answer : Answer) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(parent, isAdmin).apply {
            sendAnswer = this@PostsAdapter.sendAnswer
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}