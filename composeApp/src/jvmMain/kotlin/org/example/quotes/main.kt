package org.example.quotes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import AppCore
import dev.zacsweers.metro.createGraph
import org.example.quotes.DI.AppGraph

import ports.driven.QuoteRepository
import ports.driven.TagRepository

import repository.initializeDb
import repository.quotes.SqlLiteQuoteRepository
import repository.tags.SqlLiteTagRepository

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Quotes App",
        state = WindowState(placement = WindowPlacement.Maximized),
    ) {
        val graph = createGraph<AppGraph>()

        initializeDb("quotes.db")

        val appCore = graph.appCore
        App(appCore)
    }
}