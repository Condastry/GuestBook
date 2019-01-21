package com.nevermore.guestbook.data

import com.google.gson.annotations.SerializedName

class Comment(
    @SerializedName("comment_id")
    var id : Int = 0,
    var user : User? = null,
    var title : String = "",
    var message : String = "",
    var answers : MutableList<Answer> = mutableListOf(),
    @SerializedName("created_at")
    var createdAt: String = ""
)