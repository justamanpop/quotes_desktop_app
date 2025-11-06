package org.example.quotes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

import dev.zacsweers.metro.createGraph
import org.example.quotes.DI.AppGraph
import org.example.quotes.app.App
import org.example.quotes.app.AppViewModel
import org.example.quotes.shared.createQuoteDirectoryIfNotExist
import org.example.quotes.shared.ensureOneInstanceOfAppRunning

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Quotes App",
        state = WindowState(placement = WindowPlacement.Maximized),
    ) {
        ensureOneInstanceOfAppRunning()

        createQuoteDirectoryIfNotExist()

        val graph = createGraph<AppGraph>()
        val appViewModel = AppViewModel(graph.appCore)
        App(appViewModel)
    }
}