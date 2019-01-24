package com.nevermore.guestbook.data.bodies

import com.google.gson.annotations.SerializedName

class CommentResponseBody(
    var id: Int = 0,
    @SerializedName("user_id")
    var userID: Int = 0,
    var title: String = "",
    var message: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
)

