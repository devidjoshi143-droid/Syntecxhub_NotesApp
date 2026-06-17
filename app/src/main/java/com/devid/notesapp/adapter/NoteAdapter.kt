package com.devid.notesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devid.notesapp.R
import com.devid.notesapp.model.Note

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onClick: (Note) -> Unit,
    private val onLongClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val title: TextView =
            itemView.findViewById(R.id.tvTitle)

        val description: TextView =
            itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.note_item,
                parent,
                false
            )

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {

        val note = notes[position]

        holder.title.text = note.title
        holder.description.text = note.description

        holder.itemView.setOnClickListener {
            onClick(note)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(note)
            true
        }
    }
    fun updateList(newList: MutableList<Note>) {
        notes.clear()
        notes.addAll(newList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return notes.size
    }
}