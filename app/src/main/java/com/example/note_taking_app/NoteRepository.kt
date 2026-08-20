package com.example.note_taking_app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object NoteRepository {

    val notes = mutableListOf<Note>()

    private var nextId = 1

    private const val PREF_NAME = "note_preferences"
    private const val NOTES_KEY = "notes"

    // Initialize the repository when the app starts
    fun initialize(context: Context) {

        notes.clear()

        val preferences = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        val savedNotes = preferences.getString(
            NOTES_KEY,
            null
        )

        if (savedNotes != null) {

            try {

                val jsonArray = JSONArray(savedNotes)

                for (i in 0 until jsonArray.length()) {

                    val jsonObject = jsonArray.getJSONObject(i)

                    val note = Note(
                        id = jsonObject.getInt("id"),
                        title = jsonObject.getString("title"),
                        content = jsonObject.getString("content")
                    )

                    notes.add(note)
                }

                // Make sure the next ID is unique
                if (notes.isNotEmpty()) {
                    nextId = notes.maxOf { it.id } + 1
                }

            } catch (e: Exception) {

                notes.clear()
                nextId = 1
            }
        }
    }

    // Save all notes to SharedPreferences
    private fun saveNotes(context: Context) {

        val jsonArray = JSONArray()

        for (note in notes) {

            val jsonObject = JSONObject()

            jsonObject.put("id", note.id)
            jsonObject.put("title", note.title)
            jsonObject.put("content", note.content)

            jsonArray.put(jsonObject)
        }

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(
                NOTES_KEY,
                jsonArray.toString()
            )
            .apply()
    }

    // Add a new note
    fun addNote(
        context: Context,
        title: String,
        content: String
    ) {

        notes.add(
            Note(
                id = nextId++,
                title = title,
                content = content
            )
        )

        saveNotes(context)
    }

    // Delete a note
    fun deleteNote(
        context: Context,
        note: Note
    ) {

        notes.remove(note)

        saveNotes(context)
    }

    // Update an existing note
    fun updateNote(
        context: Context,
        id: Int,
        title: String,
        content: String
    ) {

        val note = notes.find {
            it.id == id
        }

        if (note != null) {

            note.title = title
            note.content = content

            saveNotes(context)
        }
    }
}