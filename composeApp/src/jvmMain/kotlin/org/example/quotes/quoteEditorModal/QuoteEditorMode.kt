package org.example.quotes.quoteEditorModal

import Quote
import Tag

sealed interface QuoteEditorMode {
    val quote: Quote?
    fun performAction(quoteToAddOrUpdate: Quote)
    fun getButtonText(): String
    fun getContent(): String
    fun getSource(): String

    data class AddMode(val addQuote: (Quote) -> Unit): QuoteEditorMode {
        override val quote: Quote? = null
        override fun performAction(quoteToAddOrUpdate: Quote) {
            addQuote(quoteToAddOrUpdate)
        }
        override fun getContent(): String {
            return ""
        }
        override fun getSource(): String {
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
        override fun getContent(): String {
            return quote.content
        }
        override fun getSource(): String {
            return quote.source
        }
        override fun getButtonText(): String {
            return "Update Quote"
        }
    }
}