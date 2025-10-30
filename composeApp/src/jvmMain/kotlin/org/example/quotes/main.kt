package org.example.quotes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import AppCore
import quotes.InMemoryQuoteRepository
import ports.driven.QuoteRepository
import ports.driven.TagRepository
import tags.InMemoryTagRepository

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "quotes",
        state = WindowState(placement = WindowPlacement.Maximized),
    ) {
        val quoteRepository: QuoteRepository = InMemoryQuoteRepository()
        val tagRepository: TagRepository = InMemoryTagRepository()
//        val quoteRepository: QuoteRepository = SqlLiteQuoteRepository("quotes.db")
//        val tagRepository: TagRepository = SqlLiteTagRepository("quotes.db")

        val appCore = AppCore(quoteRepository, tagRepository)
        App(appCore)
    }
}