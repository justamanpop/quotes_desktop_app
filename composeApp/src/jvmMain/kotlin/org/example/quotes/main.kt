package org.example.quotes

import Quote
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import QuoteCore
import SqlLiteQuoteRepository
//import InMemoryQuoteRepository
import ports.driven.QuoteRepository

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "quotes",
        state = WindowState(placement = WindowPlacement.Maximized)
    ) {
//        val quoteRepository: QuoteRepository = InMemoryQuoteRepository()
        val quoteRepository: QuoteRepository = SqlLiteQuoteRepository("quotes.db")
        val quoteCore = QuoteCore(quoteRepository)

        App(quoteCore)
    }
}