package com.example.notex.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.notex.activities.MainActivity
import com.example.notex.R
import com.example.notex.databinding.FragmentNoteBinding
import com.example.notex.utils.hideKeyboard
import com.example.notex.viewModel.NoteActivityViewModel
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteFragment : Fragment(R.layout.fragment_note) {

    private lateinit var noteBinding: FragmentNoteBinding

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

        val activity = requireActivity() as MainActivity
        val navController = Navigation.findNavController(view)

        requireView().hideKeyboard()

        viewLifecycleOwner.lifecycleScope.launch {
            delay(10)

            activity.window.statusBarColor = Color.parseColor("#9E9D9D")
            activity.window.clearFlags(
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            )
        }

        // Example button click (uncomment when needed)
        /*
        noteBinding.addNoteFab.setOnClickListener {
            noteBinding.appBarLayout.visibility = View.INVISIBLE
            navController.navigate(
                NoteFragmentDirections.actionNoteFragmentToSaveOrUpdate()
            )
        }
        */
    }
}