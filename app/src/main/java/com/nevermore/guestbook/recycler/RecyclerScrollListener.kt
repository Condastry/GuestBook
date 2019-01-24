package com.nevermore.guestbook.recycler

import android.nfc.tech.MifareUltralight
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class RecyclerScrollListener : RecyclerView.OnScrollListener() {

    var loadItemEvent: (() -> Unit)? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager!! as LinearLayoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (
            visibleItemCount + firstVisibleItemPosition >= totalItemCount
            && firstVisibleItemPosition >= 0
            && totalItemCount >= MifareUltralight.PAGE_SIZE
        ) {
            loadItemEvent?.invoke()
        }
    }
}