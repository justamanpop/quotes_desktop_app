package org.example

import AppCore
import ports.driving.ForSync
import repository.quotes.InMemoryQuoteRepository
import repository.tags.InMemoryTagRepository

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val tagRepository = InMemoryTagRepository()
    val quoteRepository = InMemoryQuoteRepository(tagRepository)

    val app: ForSync = AppCore(quoteRepository, tagRepository)

    val json = app.exportQuotesToJson()
    println(json)
}