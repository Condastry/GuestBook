package com.nevermore.guestbook.tools

import org.json.JSONObject

object SocketIOUtils {
    const val HOST = "http://pusher.cpl.by:6020"
    const val PUBLIC_PUSH = "public-push"
    const val PRIVATE_USER = "private-user."

    const val SUBSCRIBE = "subscribe"
    const val EVENT_PUBLIC_PUSH = "App\\Events\\PublicPush"
    const val EVENT_USER_PUSH = "App\\Events\\UserPush"

    fun getSubscriptionBody(channel: String, name: String, token: String): JSONObject {
        return JSONObject().apply {
            put("channel", channel)
            put("name", name)
            put("auth", JSONObject().apply {
                put("headers", JSONObject().apply {
                    put("Accept", "application/json")
                    put("Authorization", token)
                })
            })
        }
    }
}