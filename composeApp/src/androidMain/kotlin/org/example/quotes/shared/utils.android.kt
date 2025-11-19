package org.example.quotes.shared

import java.io.File

actual fun copyToClipboard(text: String) {
}

actual fun getQuoteDirPath(): String {
    val os = System.getProperty("os.name").lowercase()
    val home = System.getProperty("user.home")

    return when {
        os.contains("win") -> File(System.getenv("APPDATA"), "Quote Manager").absolutePath
        os.contains("mac") -> File(home, "Library/Application Support/Quote Manager").absolutePath
        else -> File(home, ".local/state/quotes").absolutePath
    }
}