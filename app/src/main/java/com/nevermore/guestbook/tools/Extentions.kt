package com.nevermore.guestbook.tools

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

fun Activity.hideKeyboard(){
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.isGone(value : Boolean){
    visibility = if(value) View.GONE else View.VISIBLE
}

fun getCurrentTimeStr() : String{
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
}