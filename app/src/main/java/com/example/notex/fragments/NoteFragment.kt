package com.example.notex.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notex.R
import com.example.notex.adapter.NoteAdapter
import com.example.notex.databinding.FragmentNoteBinding
import com.example.notex.utils.hideKeyboard
import com.example.notex.viewModel.NoteActivityViewModel
import com.google.android.material.transition.MaterialElevationScale

class NoteFragment : Fragment(R.layout.fragment_note) {

    private lateinit var noteBinding: FragmentNoteBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var navController: NavController

    private val noteActivityViewModel: NoteActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false).apply {
            duration = 350
        }

        enterTransition = MaterialElevationScale(true).apply {
            duration = 350
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteBinding = FragmentNoteBinding.bind(view)
        navController = Navigation.findNavController(view)

        setupRecyclerView()
        
        // Initial observation
        observeNotes()

        requireView().hideKeyboard()

        noteAdapter.onItemClick = { note ->
            Log.d("NoteFragment", "Note clicked: ${note.id}")
            val action = NoteFragmentDirections
                .actionNoteFragmentToSaveOrUpdate(note)

            navController.navigate(action)
        }

        noteBinding.innerFab.setOnClickListener {
            Log.d("NoteFragment", "FAB clicked")
            val action = NoteFragmentDirections
                .actionNoteFragmentToSaveOrUpdate(null)

            navController.navigate(action)
        }

        noteBinding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                if (query.isNotEmpty()) {
                    searchNotes(query)
                } else {
                    observeNotes()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter()
        noteBinding.rvNote.adapter = noteAdapter
    }

    private fun observeNotes() {
        noteActivityViewModel.getAllNotes().removeObservers(viewLifecycleOwner)
        noteActivityViewModel.getAllNotes().observe(viewLifecycleOwner) { notes ->
            Log.d("NoteFragment", "Observed all notes: ${notes?.size ?: 0}")
            noteAdapter.differ.submitList(notes)
            updateUI(notes)
        }
    }

    private fun searchNotes(query: String) {
        val searchQuery = "%$query%"
        noteActivityViewModel.searchNote(searchQuery).removeObservers(viewLifecycleOwner)
        noteActivityViewModel.searchNote(searchQuery).observe(viewLifecycleOwner) { notes ->
            Log.d("NoteFragment", "Search results for '$query': ${notes?.size ?: 0}")
            noteAdapter.differ.submitList(notes)
            updateUI(notes)
        }
    }

    private fun updateUI(notes: List<com.example.notex.model.Note>?) {
        if (notes.isNullOrEmpty()) {
            noteBinding.noData.visibility = View.VISIBLE
            noteBinding.rvNote.visibility = View.GONE
        } else {
            noteBinding.noData.visibility = View.GONE
            noteBinding.rvNote.visibility = View.VISIBLE
        }
    }
}
