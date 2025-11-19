package org.example.quotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.zacsweers.metro.createGraphFactory
import org.example.quotes.DI.AppGraph
import org.example.quotes.app.App

import org.example.quotes.app.AppViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val ctx = applicationContext
        val graph = createGraphFactory<AppGraph.Factory>().create(ctx)

        val appViewModel = AppViewModel(graph.appCore)
        setContent {
            App(appViewModel)
        }
    }
}