package com.example.note_taking_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_taking_app.databinding.FragmentNoteListBinding

class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NoteAdapter

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
        updateEmptyMessage()
    }

    private fun setupRecyclerView() {

        adapter = NoteAdapter(
            notes = NoteRepository.notes,

            onNoteClick = { note ->
                // We will implement editing later
            },

            onDeleteClick = { note ->
                // We will implement deleting later
            }
        )

        binding.recyclerViewNotes.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerViewNotes.adapter = adapter
    }

    private fun updateEmptyMessage() {

        if (NoteRepository.notes.isEmpty()) {

            binding.tvEmptyMessage.visibility = View.VISIBLE
            binding.recyclerViewNotes.visibility = View.GONE

        } else {

            binding.tvEmptyMessage.visibility = View.GONE
            binding.recyclerViewNotes.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}