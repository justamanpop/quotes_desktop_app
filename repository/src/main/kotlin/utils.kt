package repository

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

fun initializeDb(conn: SQLiteConnection) {
    conn.execSQL("PRAGMA foreign_keys = ON;")
    conn.execSQL("BEGIN TRANSACTION;")

    try {
        conn.execSQL("CREATE TABLE IF NOT EXISTS quotes (id INTEGER PRIMARY KEY, content TEXT, source TEXT)")
        conn.execSQL("CREATE TABLE IF NOT EXISTS tags (id INTEGER PRIMARY KEY, name TEXT);")
        conn.execSQL("CREATE TABLE IF NOT EXISTS quote_tag_mapping (quote_id INTEGER, tag_id INTEGER, FOREIGN KEY(quote_id) REFERENCES quotes(id), FOREIGN KEY(tag_id) REFERENCES tags(id))")

        conn.execSQL("COMMIT;")
    } catch (e: Exception) {
        conn.execSQL("ROLLBACK;")
        throw e;
    }
}