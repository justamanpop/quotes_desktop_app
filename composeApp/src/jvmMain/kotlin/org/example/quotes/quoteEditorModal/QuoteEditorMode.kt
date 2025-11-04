package org.example.quotes.quoteEditorModal

import Quote

sealed interface QuoteEditorMode {
    data class AddMode(val addQuote: (Quote) -> Unit): QuoteEditorMode
    data class EditMode(val quote: Quote, val updateTag: (tagId: Int, newName: String) -> Unit): QuoteEditorMode
}