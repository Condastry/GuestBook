package com.nevermore.guestbook.data.bodies

import com.google.gson.annotations.SerializedName

class AnswerBody(
    @SerializedName("api_token")
    var apiToken : String = "",
    var message : String = ""
)