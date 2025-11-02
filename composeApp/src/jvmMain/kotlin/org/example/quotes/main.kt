package org.example.quotes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import AppCore

import ports.driven.QuoteRepository
import ports.driven.TagRepository

import repository.quotes.InMemoryQuoteRepository
import repository.tags.InMemoryTagRepository

import repository.initializeDb
import repository.quotes.SqlLiteQuoteRepository
import repository.tags.SqlLiteTagRepository

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Quotes App",
        state = WindowState(placement = WindowPlacement.Maximized),
    ) {
//        val quoteRepository: QuoteRepository = InMemoryQuoteRepository()
//        val tagRepository: TagRepository = InMemoryTagRepository()

        initializeDb("quotes.db")
        val quoteRepository: QuoteRepository = SqlLiteQuoteRepository("quotes.db")
        val tagRepository: TagRepository = SqlLiteTagRepository("quotes.db")

        val appCore = AppCore(quoteRepository, tagRepository)
        App(appCore)
    }
}