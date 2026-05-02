package com.example.notex.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notex.R
import com.example.notex.databinding.FragmentSaveOrDeleteBinding
import com.example.notex.model.Note
import com.example.notex.utils.hideKeyboard
import com.example.notex.viewModel.NoteActivityViewModel
import com.google.android.material.transition.MaterialContainerTransform
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaveOrDeleteFragments : Fragment(R.layout.fragment_save_or_delete) {

    private lateinit var navController: NavController
    private var _binding: FragmentSaveOrDeleteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteActivityViewModel by activityViewModels()
    private val args: SaveOrDeleteFragmentsArgs by navArgs()

    private var note: Note? = null
    private val currentDate = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transform = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment
            scrimColor = android.graphics.Color.TRANSPARENT
            duration = 300L
        }

        sharedElementEnterTransition = transform
        sharedElementReturnTransition = transform
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSaveOrDeleteBinding.bind(view)
        navController = view.findNavController()
        
        binding.etNoteContent.setStylesBar(binding.styleBar)

        note = args.note
        Log.d("SaveOrDelete", "Received note: ${note?.id}")

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        binding.lastEdited.text = "Created on: $currentDate"
        
        note?.let {
            binding.etTitle.setText(it.title)
            binding.etNoteContent.renderMD(it.content)
            binding.lastEdited.text = "Edited on: ${it.date}"
            binding.deleteNote.visibility = View.VISIBLE
        }
    }

    private fun setupListeners() {

        binding.toolbar.setNavigationOnClickListener {
            requireView().hideKeyboard()
            navController.popBackStack()
        }

        binding.saveNote.setOnClickListener {
            saveNote()
        }

        binding.deleteNote.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete") { _, _ ->
                    note?.let {
                        viewModel.deleteNote(it)
                        Toast.makeText(requireContext(), "Note Deleted", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.etNoteContent.setOnFocusChangeListener { _, hasFocus ->
            binding.bottomBar.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etNoteContent.getMD().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Title or content is empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (note == null) {
            // Create new note
            viewModel.saveNote(
                Note(
                    id = 0,
                    title = title,
                    content = content,
                    date = currentDate
                )
            )
            Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_SHORT).show()
        } else {
            // Update existing note
            Log.d("SaveOrDelete", "Updating note: ${note!!.id}")
            viewModel.updateNote(
                Note(
                    id = note!!.id,
                    title = title,
                    content = content,
                    date = currentDate
                )
            )
            Toast.makeText(requireContext(), "Note Updated", Toast.LENGTH_SHORT).show()
        }

        navController.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
