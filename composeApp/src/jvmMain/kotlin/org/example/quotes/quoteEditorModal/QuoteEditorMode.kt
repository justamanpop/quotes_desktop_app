package org.example.quotes.quoteEditorModal

import Quote
import Tag

sealed interface QuoteEditorMode {
    val quote: Quote?
    fun performAction(quoteToAddOrUpdate: Quote)

    data class AddMode(val addQuote: (Quote) -> Unit): QuoteEditorMode {
        override val quote: Quote? = null
        override fun performAction(quoteToAddOrUpdate: Quote) {
            addQuote(quoteToAddOrUpdate)
        }
    }
    data class EditMode(override val quote: Quote, val updateQuote: (quote: Quote) -> Unit): QuoteEditorMode {
        override fun performAction(quoteToAddOrUpdate: Quote) {
            updateQuote(quoteToAddOrUpdate.copy(id = quote.id))
        }
    }
}