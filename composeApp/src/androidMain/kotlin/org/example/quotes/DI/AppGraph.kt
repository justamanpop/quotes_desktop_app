package org.example.quotes.DI

import AppCore
import androidx.sqlite.SQLiteConnection

import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

import ports.driven.QuoteRepository
import ports.driven.TagRepository
import repository.quotes.SqlLiteQuoteRepository
import repository.tags.SqlLiteTagRepository

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import repository.initializeDb
import repository.quotes.InMemoryQuoteRepository
import repository.tags.InMemoryTagRepository

@DependencyGraph
interface AppGraph {
    val appCore: AppCore

    @Provides
    fun provideQuoteRepository(): QuoteRepository {
        return InMemoryQuoteRepository()
    }
    @Provides
    fun provideTagRepository(): TagRepository {
        return InMemoryTagRepository()
    }
    @Provides
    fun provideAppCore(quoteRepository: QuoteRepository, tagRepository: TagRepository): AppCore {
        return AppCore(quoteRepository, tagRepository)
    }
}