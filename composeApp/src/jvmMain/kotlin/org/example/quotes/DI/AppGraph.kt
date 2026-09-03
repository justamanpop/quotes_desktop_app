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
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn
import org.example.quotes.shared.getQuoteDirPath
import repository.initializeDb
import java.nio.file.Paths

@DependencyGraph
@SingleIn(AppScope::class)
interface AppGraph {
    val appCore: AppCore

    @Provides
    fun provideQuoteRepository(conn: SQLiteConnection, tagRepository: TagRepository): QuoteRepository {
        return SqlLiteQuoteRepository(conn, tagRepository)
    }
    @Provides
    fun provideTagRepository(conn: SQLiteConnection): TagRepository {
        return SqlLiteTagRepository(conn)
    }
    @Provides
    fun provideAppCore(quoteRepository: QuoteRepository, tagRepository: TagRepository): AppCore {
        return AppCore(quoteRepository, tagRepository)
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideSqliteDbConnection(): SQLiteConnection {
        //TODO make it generic expected and put actual values for 
        val dbName = "quotes.db"
        val fullPath = Paths.get(getQuoteDirPath(), dbName)

        val conn = BundledSQLiteDriver().open(fullPath.toString())
        initializeDb(conn)
        return conn
    }
}