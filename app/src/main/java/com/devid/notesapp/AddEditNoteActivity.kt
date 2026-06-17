package com.devid.notesapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devid.notesapp.database.NotesDatabaseHelper
import com.devid.notesapp.model.Note

class AddEditNoteActivity : AppCompatActivity() {

    private var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val databaseHelper = NotesDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)

        if (noteId != -1) {

            etTitle.setText(
                intent.getStringExtra("note_title")
            )

            etDescription.setText(
                intent.getStringExtra("note_description")
            )

            btnSave.text = "Update Note"
        }

        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter a title",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (noteId == -1) {

                databaseHelper.insertNote(
                    Note(
                        0,
                        title,
                        description
                    )
                )

                Toast.makeText(
                    this,
                    "Note Saved",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                databaseHelper.updateNote(
                    Note(
                        noteId,
                        title,
                        description
                    )
                )

                Toast.makeText(
                    this,
                    "Note Updated",
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish()
        }
    }
}