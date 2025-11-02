package org.example.quotes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import dev.zacsweers.metro.createGraph
import org.example.quotes.DI.AppGraph

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Quotes App",
        state = WindowState(placement = WindowPlacement.Maximized),
    ) {
        val graph = createGraph<AppGraph>()
        val appCore = graph.appCore

        App(appCore)
    }
}