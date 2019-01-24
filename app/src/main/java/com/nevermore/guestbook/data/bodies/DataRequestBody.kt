package com.nevermore.guestbook.data.bodies

import com.nevermore.guestbook.data.Comment

class DataRequestBody(
    var data: List<Comment> = listOf()
)