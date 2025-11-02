package org.example.quotes.deleteConfirmationModal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationModal(idToDelete: Int?, deleteFunction: (id: Int) -> Unit, nameOfDeletionItem: String, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this $nameOfDeletionItem?") },
        confirmButton = {
            Button(
                onClick = {
                    idToDelete?.let {
                        deleteFunction(idToDelete)
                    }
                    onDismissRequest()
                },
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
            ) {
                Text("Cancel")
            }
        }
    )
}