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

@DependencyGraph
interface AppGraph {
    val appCore: AppCore

    @Provides
    fun provideQuoteRepository(conn: SQLiteConnection): QuoteRepository {
        return SqlLiteQuoteRepository(conn)
    }
    @Provides
    fun provideTagRepository(conn: SQLiteConnection): TagRepository {
        return SqlLiteTagRepository(conn)
    }
    @Provides
    fun provideAppCore(quoteRepository: QuoteRepository, tagRepository: TagRepository): AppCore {
        return AppCore(quoteRepository, tagRepository)
    }

    @Provides
    fun provideSqliteDbConnection(): SQLiteConnection {
        val dbName = "quotes.db"
        val conn = BundledSQLiteDriver().open(dbName)
        initializeDb(conn)
        return conn
    }
}