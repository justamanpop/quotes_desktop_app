package org.example.quotes.shared

import android.app.Application

object AppContext {
    lateinit var application: Application
        private set

    fun init(context: Application) {
        this.application = context
    }
}
