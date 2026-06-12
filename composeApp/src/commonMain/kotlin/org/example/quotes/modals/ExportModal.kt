package org.example.quotes.modals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.example.quotes.shared.lightBorderIfFocused
import org.example.quotes.shared.moveFocusOnTab
import org.example.quotes.shared.writeFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportModal(
    getExportJson: () -> String,
    emitSnackbarMessage: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Card(modifier = Modifier.width(600.dp)) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth().moveFocusOnTab()
            ) {
                var isExportJsonButtonFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        val jsonString = getExportJson()
                        writeFile(jsonString)
                        emitSnackbarMessage("Success: Quotes successfully exported as JSON")
                        onDismissRequest()
                    },
                    colors = ButtonColors(
                        containerColor = if (isExportJsonButtonFocused) Color(15, 81, 186) else Color(52, 161, 235),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .onFocusChanged { focusState -> isExportJsonButtonFocused = focusState.isFocused }
                        .lightBorderIfFocused(isExportJsonButtonFocused)
                ) {
                    Text("JSON {}", color = Color.White, fontSize = 24.sp)
                }
            }
        }
    }
}