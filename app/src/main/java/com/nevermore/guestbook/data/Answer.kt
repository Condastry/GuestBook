package com.nevermore.guestbook.data

import com.google.gson.annotations.SerializedName

class Answer(
    var id: Int = 0,
    @SerializedName("comment_id")
    var commentId: Int = 0,
    var message: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
)