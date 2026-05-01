package com.example.notex.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
        setupRecyclerView()

        val navController = Navigation.findNavController(view)

        requireView().hideKeyboard()

        noteBinding.innerFab.setOnClickListener {
            navController.navigate(
                NoteFragmentDirections.actionNoteFragmentToSaveOrUpdate()
            )
        }

        noteBinding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    searchNotes(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        observeNotes()
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter()
        noteBinding.rvNote.apply {
            adapter = noteAdapter
        }
    }

    private fun observeNotes() {
        noteActivityViewModel.getAllNotes().observe(viewLifecycleOwner) { notes ->
            noteAdapter.differ.submitList(notes)
            updateUI(notes)
        }
    }

    private fun searchNotes(query: String) {
        val searchQuery = "%$query%"
        noteActivityViewModel.searchNote(searchQuery).observe(viewLifecycleOwner) { notes ->
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
