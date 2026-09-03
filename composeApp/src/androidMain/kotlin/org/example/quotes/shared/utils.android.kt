package org.example.quotes.shared

import android.content.ContentValues
import android.provider.MediaStore
import java.io.File

actual fun getQuoteDirPath(): String {
    val os = System.getProperty("os.name").lowercase()
    val home = System.getProperty("user.home")

    return when {
        os.contains("win") -> File(System.getenv("APPDATA"), "Quote Manager").absolutePath
        os.contains("mac") -> File(home, "Library/Application Support/Quote Manager").absolutePath
        else -> File(home, ".local/state/quotes").absolutePath
    }
}

actual fun writeFile(fileName: String, contents: String) {
    val ctx = AppContext.context
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "application/json")
    }
    val uri = ctx.contentResolver.insert(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues
    )
    uri?.let {
        ctx.contentResolver.openOutputStream(it)?.use { stream ->
            stream.write(contents.toByteArray())
        }
    }
}