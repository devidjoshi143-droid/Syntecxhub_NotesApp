package com.devid.notesapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.devid.notesapp.model.Note

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_DESCRIPTION TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_DESCRIPTION, note.description)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotes(): MutableList<Note> {

        val notes = mutableListOf<Note>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME",
            null
        )

        if (cursor.moveToFirst()) {
            do {

                val id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_ID)
                )

                val title = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_TITLE)
                )

                val description = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)
                )

                notes.add(
                    Note(
                        id,
                        title,
                        description
                    )
                )

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return notes
    }

    fun deleteNote(id: Int) {

        val db = writableDatabase

        db.delete(
            TABLE_NAME,
            "$COLUMN_ID=?",
            arrayOf(id.toString())
        )

        db.close()
    }

    fun updateNote(note: Note) {

        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_DESCRIPTION, note.description)
        }

        db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID=?",
            arrayOf(note.id.toString())
        )

        db.close()
    }
}