package com.nevermore.guestbook.tools

class Page<T>(var number: Int = 0) {
    interface OnPageLoadedListener<T> {
        fun onPageLoaded(lp: Page<T>)
    }

    inner class PageItem<T>(var value: T) {
        var isLoaded = false
            set(value) {
                field = value
                this@Page.onPageLoaded()
            }

    }

    var isLoaded = false
    val items = mutableListOf<PageItem<T>>()
    var onLoadListener: OnPageLoadedListener<T>? = null

    fun addItem(value: T): PageItem<T> {
        val item = PageItem(value)
        items.add(item)
        return item
    }

    private fun onPageLoaded() {
        items.forEach { if (!it.isLoaded) return }
        isLoaded = true
        onLoadListener?.onPageLoaded(this)
    }
}