package org.example.quotes.DI

import AppCore
import android.content.Context
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import ports.driven.QuoteRepository
import ports.driven.TagRepository
import repository.initializeDb
import repository.quotes.SqlLiteQuoteRepository
import repository.tags.SqlLiteTagRepository
import java.io.File
import java.io.IOException


@DependencyGraph
interface AppGraph {
    val context: Context
    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides context: Context): AppGraph
    }

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
        val fullPath = File(context.filesDir, dbName).absolutePath

        val conn = BundledSQLiteDriver().open(fullPath)
        initializeDb(conn)
        return conn
    }
}