package com.devid.notesapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devid.notesapp.adapter.NoteAdapter
import com.devid.notesapp.database.NotesDatabaseHelper
import com.devid.notesapp.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.SearchView
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    private lateinit var notes: MutableList<Note>
    private lateinit var allNotes: MutableList<Note>
    private lateinit var databaseHelper: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        val searchView = findViewById<SearchView>(R.id.searchView)
        databaseHelper = NotesDatabaseHelper(this)

        allNotes = databaseHelper.getAllNotes()
        notes = allNotes.toMutableList()

        adapter = NoteAdapter(

            notes,

            { note ->

                val intent = Intent(
                    this,
                    AddEditNoteActivity::class.java
                )

                intent.putExtra(
                    "note_id",
                    note.id
                )

                intent.putExtra(
                    "note_title",
                    note.title
                )

                intent.putExtra(
                    "note_description",
                    note.description
                )

                startActivity(intent)
            },

            { note ->

                AlertDialog.Builder(this)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Delete") { _, _ ->

                        databaseHelper.deleteNote(note.id)

                        allNotes.clear()
                        allNotes.addAll(databaseHelper.getAllNotes())

                        notes.clear()
                        notes.addAll(allNotes)
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText.isNullOrEmpty()) {

                        adapter.updateList(
                            allNotes.toMutableList()
                        )

                    } else {

                        val filteredList = allNotes.filter {

                            it.title.contains(
                                newText,
                                ignoreCase = true
                            )

                        }.toMutableList()

                        adapter.updateList(filteredList)
                    }

                    return true
                }
            }
        )
        fabAdd.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddEditNoteActivity::class.java
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()

        notes.clear()
        notes.addAll(databaseHelper.getAllNotes())

        adapter.notifyDataSetChanged()
    }
}