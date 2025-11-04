package org.example.quotes.quoteEditorModal

import Quote

sealed interface QuoteEditorMode {
    val quote: Quote?
    fun performAction(quoteToAddOrUpdate: Quote)
    fun getInitialContent(): String
    fun getInitialSource(): String
    fun getButtonText(): String

    data class AddMode(val addQuote: (Quote) -> Unit): QuoteEditorMode {
        override val quote: Quote? = null
        override fun performAction(quoteToAddOrUpdate: Quote) {
            addQuote(quoteToAddOrUpdate)
        }
        override fun getInitialContent(): String {
            return ""
        }
        override fun getInitialSource(): String {
            return ""
        }
        override fun getButtonText(): String {
            return "Create Quote"
        }
    }
    data class EditMode(override val quote: Quote, val updateQuote: (quote: Quote) -> Unit): QuoteEditorMode {
        override fun performAction(quoteToAddOrUpdate: Quote) {
            updateQuote(quoteToAddOrUpdate.copy(id = quote.id))
        }
        override fun getInitialContent(): String {
            return quote.content
        }
        override fun getInitialSource(): String {
            return quote.source
        }
        override fun getButtonText(): String {
            return "Update Quote"
        }
    }
}