package com.example.note_taking_app

class NoteRepository {
    val notes = mutableListOf<Note>()

    private var nextId = 1

    fun addNote(title: String, content: String) {
        notes.add(
            Note(
                id = nextId++,
                title = title,
                content = content
            )
        )
    }

    fun getNote(id: Int): Note? {
        return notes.find { it.id == id }
    }

    fun updateNote(
        id: Int,
        title: String,
        content: String
    ) {
        val note = notes.find { it.id == id }

        note?.let {
            it.title = title
            it.content = content
        }
    }

    fun deleteNote(id: Int) {
        notes.removeAll { it.id == id }
    }

}