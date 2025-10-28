package org.example.quotes.deleteQuoteConfirmationModal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteQuoteConfirmationModal(quoteIdToDelete: Int?, deleteFunction: (id: Int) -> Unit, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this quote?") },
        confirmButton = {
            Button(
                onClick = {
                    quoteIdToDelete?.let {
                        deleteFunction(quoteIdToDelete)
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