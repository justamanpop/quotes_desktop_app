import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import org.example.quotes.tagEditorModal.TagEditorMode
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

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

fun copyToClipboard(text: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val stringSelection = StringSelection(text)
    clipboard.setContents(stringSelection, null)
}

fun getSnackbarColor(message: String): Color {
    return if (message.startsWith("Error:")) {
        Color.Red
    } else if (message.startsWith("Success:")) {
        Color.Green
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