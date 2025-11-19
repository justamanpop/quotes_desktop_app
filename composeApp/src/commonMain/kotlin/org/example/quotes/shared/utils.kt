package org.example.quotes.shared

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File

fun Modifier.moveFocusOnTab() = composed {
    val focusManager = LocalFocusManager.current
    onPreviewKeyEvent {
        if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
            focusManager.moveFocus(
                if (it.isShiftPressed) FocusDirection.Previous else FocusDirection.Next
            )
            true
        } else {
            false
        }
    }
}

fun Modifier.lightBorderIfFocused(isFocused: Boolean, borderWidth: Dp = 1.dp) = composed {
    Modifier.border(borderWidth, if (isFocused) Color.LightGray else Transparent)
}

expect fun getQuoteDirPath(): String
