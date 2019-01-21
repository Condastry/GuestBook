package com.nevermore.guestbook.recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nevermore.guestbook.R
import com.nevermore.guestbook.data.Answer
import com.nevermore.guestbook.data.Comment
import kotlinx.android.synthetic.main.item_answer.view.*
import kotlinx.android.synthetic.main.item_post.view.*


class PostViewHolder(parent: ViewGroup, isAdmin: Boolean) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)) {
    private val tvTitle = itemView.tvTitle
    private val tvText = itemView.tvText
    private val tvDate = itemView.tvDate
    private val commentsContainer = itemView.commentsContainer
    private val inputContainer = itemView.inputContainer

    private var comment: Comment? = null

    var sendAnswer: ((answer : Answer) -> Unit)? = null

    init {
        inputContainer.visibility = if (isAdmin) View.VISIBLE else View.GONE

        itemView.apply {
            btnMakeAnswer.setOnClickListener {
                val answer = Answer(commentId = comment!!.id, message = editAnswer.text.toString())
                sendAnswer?.invoke(answer)
                comment?.answers?.add(answer)
                editAnswer.text?.clear()
                bindAnswers(comment!!.answers)
            }
        }
    }

    fun bind(item: Comment) {
        comment = item
        tvTitle.text = item.title
        tvText.text = item.message
        tvDate.text = item.createdAt

        bindAnswers(item.answers)
    }

    private fun bindAnswers(answers: List<Answer>) {
        val viewCount = commentsContainer.childCount
        val ansCount = answers.size
        for (i in 0 until ansCount - viewCount) {
            LayoutInflater.from(itemView.context).inflate(R.layout.item_answer, commentsContainer, true)
        }

        for (i in 0 until ansCount) {
            commentsContainer.getChildAt(i).apply {
                tvAnswer.text = answers[i].message
                tvAnswerDate.text = answers[i].createdAt
                visibility = View.VISIBLE
            }
        }

        for (i in ansCount until viewCount) {
            commentsContainer.getChildAt(i).visibility = View.GONE
        }
    }
}