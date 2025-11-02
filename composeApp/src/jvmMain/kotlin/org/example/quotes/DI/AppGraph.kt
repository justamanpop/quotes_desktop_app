package org.example.quotes.DI

import AppCore
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import ports.driven.QuoteRepository
import ports.driven.TagRepository
import repository.quotes.SqlLiteQuoteRepository
import repository.tags.SqlLiteTagRepository

@DependencyGraph
interface AppGraph {
    val quoteRepository: QuoteRepository
    val tagRepository: TagRepository
    val appCore: AppCore

    @Provides
    fun provideQuoteRepository(): QuoteRepository {
        return SqlLiteQuoteRepository("quotes.db")
    }
    @Provides
    fun provideTagRepository(): TagRepository {
        return SqlLiteTagRepository("quotes.db")
    }
    @Provides
    fun provideAppCore(quoteRepository: QuoteRepository, tagRepository: TagRepository): AppCore {
        return AppCore(quoteRepository, tagRepository)
    }
}