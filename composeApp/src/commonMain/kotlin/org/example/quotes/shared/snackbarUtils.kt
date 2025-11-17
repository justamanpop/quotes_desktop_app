package org.example.quotes.shared

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.graphics.Color

fun getSnackbarColor(message: String): Color {
    return if (message.startsWith("Error:")) {
        Color.Companion.Red
    } else if (message.startsWith("Success:")) {
        Color.Companion.Green
    } else {
        Color(52, 161, 235)
    }
}

fun stripSnackbarMessage(message: String): String {
    return if (message.startsWith("Error:")) {
        message.removePrefix("Error:")
    } else if (message.startsWith("Success:")) {
        message.removePrefix("Success:")
    } else if (message.startsWith("Info:")) {
        message.removePrefix("Info:")
    } else {
        message
    }
}

fun constructSnackbarDataObject(message: String): SnackbarData {
    return object : SnackbarData {
        override val visuals = object : SnackbarVisuals {
            override val message = message
            override val actionLabel = null
            override val withDismissAction = false
            override val duration = SnackbarDuration.Short
        }

        override fun performAction() {}
        override fun dismiss() {}
    }
}