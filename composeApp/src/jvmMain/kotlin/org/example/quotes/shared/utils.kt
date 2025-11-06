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

fun copyToClipboard(text: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val stringSelection = StringSelection(text)
    clipboard.setContents(stringSelection, null)
}

fun getQuoteDirPath(): String {
    val os = System.getProperty("os.name").lowercase()
    val home = System.getProperty("user.home")

    return when {
        os.contains("win") -> File(System.getenv("APPDATA"), "Quote Manager").absolutePath
        os.contains("mac") -> File(home, "Library/Application Support/Quote Manager").absolutePath
        else -> File(home, ".local/state/quotes").absolutePath
    }
}

fun createQuoteDirectoryIfNotExist() {
    val quoteDirPath = getQuoteDirPath()
    val file = File(quoteDirPath)
    file.mkdirs()
}

fun ensureOneInstanceOfAppRunning() {
    val dataDir = File(getQuoteDirPath())
    val lockFile = File(dataDir, ".lock")
    if (!lockFile.exists()) {
        createQuoteDirectoryIfNotExist()
    }

    val lockFileChannel: java.nio.channels.FileChannel
    val appLock: java.nio.channels.FileLock?

    try {
        lockFileChannel = java.io.RandomAccessFile(lockFile, "rw").channel
        appLock = lockFileChannel.tryLock()
    } catch (e: Exception) {
        javax.swing.JOptionPane.showMessageDialog(
            null,
            "An unexpected error occurred while trying to lock the application file.",
            "Error",
            javax.swing.JOptionPane.ERROR_MESSAGE
        )
        kotlin.system.exitProcess(1)
    }

    if (appLock == null) {
        javax.swing.JOptionPane.showMessageDialog(
            null,
            "Another instance of the application is already running.",
            "Application Running",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        )
        kotlin.system.exitProcess(0)
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        appLock.release()
        lockFileChannel.close()
        lockFile.delete()
    })
}