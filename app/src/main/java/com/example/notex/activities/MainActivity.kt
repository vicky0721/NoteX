package com.example.notex.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notex.databinding.ActivityMainBinding
import com.example.notex.db.NoteDatabase
import com.example.notex.repository.NoteRepository
import com.example.notex.viewModel.NoteActivityViewModel
import com.example.notex.viewModel.NoteActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var noteActivityViewModel: NoteActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ 1. Hide ActionBar
        supportActionBar?.hide()

        try {
            // ✅ 2. Initialize ViewBinding FIRST
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // ✅ 3. Edge-to-edge AFTER setting content view
            enableEdgeToEdge()

            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            // ✅ 4. ViewModel setup
            val noteRepository = NoteRepository(NoteDatabase(this))
            val factory = NoteActivityViewModelFactory(noteRepository)

            noteActivityViewModel = ViewModelProvider(this, factory)
                .get(NoteActivityViewModel::class.java)

        } catch (e: Exception) {
            Log.e("MainActivity", "Error: ${e.message}", e)
        }
    }
}