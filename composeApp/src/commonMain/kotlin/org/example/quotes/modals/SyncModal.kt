package org.example.quotes.modals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncModal(
    getExportJson: () -> String,
    importJson: (String, Boolean) -> Unit,
    emitSnackbarMessage: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var overwriteImport by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val launcher = rememberFilePickerLauncher { file ->
        if (file == null) {
            return@rememberFilePickerLauncher
        }
        scope.launch {
            try {
                val contents = file.readString()
                importJson(contents, overwriteImport)
                emitSnackbarMessage("Success: imported from file ${file.name}")
            } catch(e: Exception) {
                emitSnackbarMessage("Error: unable to import quotes: ${e.message}")
            }
            onDismissRequest()
        }
    }
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
                modifier = Modifier.padding(16.dp).fillMaxWidth().padding(start = 8.dp).moveFocusOnTab()
            ) {
                var isExportJsonButtonFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        try {
                            val jsonString = getExportJson()
                            writeFile("quotes.json", jsonString)
                            emitSnackbarMessage("Success: Quotes successfully exported to quotes.json in downloads directory")
                        } catch(e: Exception) {
                            emitSnackbarMessage("Error: unable to export quotes: ${e.message}")
                        }

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
                    Text("Export", color = Color.White, fontSize = 24.sp)
                }
                Spacer(Modifier.height(16.dp))

                var isImportJsonButtonFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        overwriteImport = false
                        launcher.launch()
                    },
                    colors = ButtonColors(
                        containerColor = if (isImportJsonButtonFocused) Color(15, 81, 186) else Color(52, 161, 235),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.padding(top = 12.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .onFocusChanged { focusState -> isImportJsonButtonFocused = focusState.isFocused }
                        .lightBorderIfFocused(isImportJsonButtonFocused)
                ) {
                    Text("Import (append)", color = Color.White, fontSize = 24.sp)
                }
                Text("Adds non-duplicate quotes to existing quotes and merges tags")

                var isImportOverwriteJsonButtonFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        overwriteImport = true
                        launcher.launch()
                    },
                    colors = ButtonColors(
                        containerColor = if (isImportOverwriteJsonButtonFocused) Color(15, 81, 186) else Color(
                            52,
                            161,
                            235
                        ),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.padding(top = 12.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .onFocusChanged { focusState -> isImportOverwriteJsonButtonFocused = focusState.isFocused }
                        .lightBorderIfFocused(isImportOverwriteJsonButtonFocused)
                ) {
                    Text("Import (overwrite)", color = Color.White, fontSize = 24.sp)
                }
                Text("⚠\uFE0F Existing quotes will be lost and replaced with imported quotes")

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}