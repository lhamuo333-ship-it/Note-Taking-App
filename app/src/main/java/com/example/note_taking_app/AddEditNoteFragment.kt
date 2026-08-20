package com.example.note_taking_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.note_taking_app.databinding.FragmentAddEditNoteBinding

class AddEditNoteFragment : Fragment() {

    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!

    // Contains the ID of the note being edited.
    // null means we are creating a new note.
    private var noteId: Int? = null

    // Determines whether this screen is Add or Edit mode.
    private val isEditMode: Boolean
        get() = noteId != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get note ID if editing
        noteId = arguments?.getInt(ARG_NOTE_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddEditNoteBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // If editing an existing note,
        // load its information into the fields.
        if (isEditMode) {
            loadNote()
        }

        // Save / Update button
        binding.saveButton.setOnClickListener {
            saveNote()
        }

        // Cancel button
        binding.cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    // Load an existing note for editing
    private fun loadNote() {

        val id = noteId ?: return

        val note = NoteRepository.notes.find {
            it.id == id
        }

        if (note != null) {

            // Change title from "Add Note" to "Edit Note"
            binding.screenTitleTextView.text = "Edit Note"

            // Put existing title into title field
            binding.titleEditText.setText(note.title)

            // Put existing content into content field
            binding.contentEditText.setText(note.content)

            // Change button text
            binding.saveButton.text = "Update Note"
        }
    }

    // Save or update note
    private fun saveNote() {

        // Get title
        val title = binding.titleEditText.text
            ?.toString()
            ?.trim()

        // Get content
        val content = binding.contentEditText.text
            ?.toString()
            ?.trim()

        // -------------------------
        // Validate title
        // -------------------------

        if (title.isNullOrEmpty()) {

            binding.titleInputLayout.error =
                "Title is required"

            return
        }

        binding.titleInputLayout.error = null

        // -------------------------
        // Validate content
        // -------------------------

        if (content.isNullOrEmpty()) {

            binding.contentInputLayout.error =
                "Content is required"

            return
        }

        binding.contentInputLayout.error = null

        // -------------------------
        // EDIT MODE
        // -------------------------

        if (isEditMode) {

            NoteRepository.updateNote(
                context = requireContext(),
                id = noteId!!,
                title = title,
                content = content
            )

            Toast.makeText(
                requireContext(),
                "Note updated",
                Toast.LENGTH_SHORT
            ).show()

        }

        // -------------------------
        // ADD MODE
        // -------------------------

        else {

            NoteRepository.addNote(
                context = requireContext(),
                title = title,
                content = content
            )

            Toast.makeText(
                requireContext(),
                "Note saved",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Return to NoteListFragment
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {

        private const val ARG_NOTE_ID = "note_id"

        // Creates this Fragment for editing
        fun newInstance(noteId: Int): AddEditNoteFragment {

            return AddEditNoteFragment().apply {

                arguments = Bundle().apply {

                    putInt(
                        ARG_NOTE_ID,
                        noteId
                    )
                }
            }
        }
    }
}