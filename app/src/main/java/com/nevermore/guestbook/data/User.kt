package com.nevermore.guestbook.data

import com.google.gson.annotations.SerializedName

class User(
    var id: Int = 0,
    var name: String = "",
    var email: String = "",
    @SerializedName("is_admin")
    var isAdmin: Int = 0,
    @SerializedName("api_token")
    var apiToken: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = ""
)