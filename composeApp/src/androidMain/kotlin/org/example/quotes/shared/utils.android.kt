package org.example.quotes.shared

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi

actual fun getQuoteDirPath(): String {
    return AppContext.context.filesDir.absolutePath
}

@RequiresApi(Build.VERSION_CODES.Q)
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

actual val isAndroid: Boolean = true