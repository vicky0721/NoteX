package com.example.notex.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.notex.R
import com.example.notex.databinding.FragmentSaveOrDeleteBinding
import com.example.notex.model.Note
import com.example.notex.utils.hideKeyboard
import com.example.notex.viewModel.NoteActivityViewModel
import com.google.android.material.transition.MaterialContainerTransform
import java.text.SimpleDateFormat
import java.util.Date

class SaveOrDeleteFragments : Fragment(R.layout.fragment_save_or_delete) {

    private lateinit var navController: NavController
    private var _binding: FragmentSaveOrDeleteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteActivityViewModel by activityViewModels()
    private val args: SaveOrDeleteFragmentsArgs by navArgs()

    private var note: Note? = null
    private val currentDate = SimpleDateFormat.getInstance().format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transform = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment // make sure this ID exists
            scrimColor = android.graphics.Color.TRANSPARENT
            duration = 300L
        }

        sharedElementEnterTransition = transform
        sharedElementReturnTransition = transform
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSaveOrDeleteBinding.bind(view)
        navController = Navigation.findNavController(view)

        note = args.note

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        note?.let {
            binding.etTitle.setText(it.title)
            binding.etNoteContent.setText(it.content)
        }
    }

    private fun setupListeners() {

        binding.backBtn.setOnClickListener {
            requireView().hideKeyboard()
            navController.popBackStack()
        }

        binding.saveNote.setOnClickListener {
            saveNote()
        }

        binding.etNoteContent.setOnFocusChangeListener { _, hasFocus ->
            binding.bottomBar.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etNoteContent.text.toString().trim()

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
        } else {
            // Update existing note
            viewModel.updateNote(
                Note(
                    id = note!!.id,
                    title = title,
                    content = content,
                    date = currentDate
                    )
            )
        }

        navController.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}