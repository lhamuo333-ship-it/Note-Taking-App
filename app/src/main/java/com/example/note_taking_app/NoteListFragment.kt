package com.example.note_taking_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_taking_app.databinding.FragmentNoteListBinding

class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteListBinding.inflate(
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

        setupRecyclerView()
        updateEmptyView()

        // + button
        binding.addButton.setOnClickListener {
            openAddNoteScreen()
        }
    }

    // -----------------------------
    // RecyclerView setup
    // -----------------------------

    private fun setupRecyclerView() {

        noteAdapter = NoteAdapter(
            notes = NoteRepository.notes,

            // When a note is clicked
            onNoteClick = { note ->
                openEditNoteScreen(note)
            },

            // When delete button is clicked
            onDeleteClick = { note ->
                showDeleteConfirmation(note)
            }
        )

        binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(
                requireContext()
            )

            adapter = noteAdapter
        }
    }

    // -----------------------------
    // Empty list message
    // -----------------------------

    private fun updateEmptyView() {

        if (NoteRepository.notes.isEmpty()) {

            binding.emptyTextView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE

        } else {

            binding.emptyTextView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    // -----------------------------
    // Open Add Note screen
    // -----------------------------

    private fun openAddNoteScreen() {

        parentFragmentManager.beginTransaction()
            .replace(
                android.R.id.content,
                AddEditNoteFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    // -----------------------------
    // Open Edit Note screen
    // -----------------------------

    private fun openEditNoteScreen(note: Note) {

        val fragment =
            AddEditNoteFragment.newInstance(note.id)

        parentFragmentManager.beginTransaction()
            .replace(
                android.R.id.content,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }

    // -----------------------------
    // Delete confirmation
    // -----------------------------

    private fun showDeleteConfirmation(note: Note) {

        AlertDialog.Builder(requireContext())

            .setTitle("Delete Note")

            .setMessage(
                "Are you sure you want to delete this note?"
            )

            .setNegativeButton(
                "Cancel",
                null
            )

            .setPositiveButton(
                "Delete"
            ) { _, _ ->

                // Delete from list AND SharedPreferences
                NoteRepository.deleteNote(
                    requireContext(),
                    note
                )

                // Update RecyclerView
                noteAdapter.notifyDataSetChanged()

                // Update empty message
                updateEmptyView()
            }

            .show()
    }

    // -----------------------------
    // Refresh list when returning
    // -----------------------------

    override fun onResume() {
        super.onResume()

        if (::noteAdapter.isInitialized) {

            noteAdapter.notifyDataSetChanged()

            updateEmptyView()
        }
    }

    // -----------------------------
    // Clean up View Binding
    // -----------------------------

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}