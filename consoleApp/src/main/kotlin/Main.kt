package org.example

import AppCore
import repository.quotes.InMemoryQuoteRepository
import repository.tags.InMemoryTagRepository

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val quoteRepository = InMemoryQuoteRepository()
    val tagRepository = InMemoryTagRepository()
    val app = AppCore(quoteRepository, tagRepository)

    val json = app.exportQuotesToJson()
    println(json)
}