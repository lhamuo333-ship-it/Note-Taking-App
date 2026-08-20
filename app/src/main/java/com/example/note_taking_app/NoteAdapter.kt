package com.example.note_taking_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_taking_app.databinding.ItemNoteBinding

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onNoteClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            note: Note,
            onNoteClick: (Note) -> Unit,
            onDeleteClick: (Note) -> Unit
        ) {
            binding.noteTitleTextView.text = note.title
            binding.noteContentTextView.text = note.content

            binding.root.setOnClickListener {
                onNoteClick(note)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(note)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {

        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        holder.bind(
            notes[position],
            onNoteClick,
            onDeleteClick
        )
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}