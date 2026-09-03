package org.example.quotes.DI

import AppCore
import android.content.Context
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import ports.driven.QuoteRepository
import ports.driven.TagRepository
import repository.initializeDb
import repository.quotes.SqlLiteQuoteRepository
import repository.tags.SqlLiteTagRepository
import java.io.File


@Suppress("unused")
@DependencyGraph
@SingleIn(AppScope::class)
interface AppGraph {
    val context: Context
    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides context: Context): AppGraph
    }

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

    @Provides
    @SingleIn(AppScope::class)
    fun provideSqliteDbConnection(): SQLiteConnection {
        val dbName = "quotes.db"
        val fullPath = File(context.filesDir, dbName).absolutePath

        val conn = BundledSQLiteDriver().open(fullPath)
        initializeDb(conn)
        return conn
    }
}