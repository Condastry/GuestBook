package com.nevermore.guestbook.data.bodies

import com.google.gson.annotations.SerializedName

class CommentRequestBody(
    @SerializedName("api_token")
    var apiToken: String = "",
    var title: String = "",
    var message: String = ""
)