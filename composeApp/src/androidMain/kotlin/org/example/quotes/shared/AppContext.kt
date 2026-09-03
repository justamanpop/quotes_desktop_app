package org.example.quotes.shared

import android.content.Context

object AppContext {
    lateinit var context: Context
        private set

    fun init(context: Context) {
        this.context = context
    }
}
