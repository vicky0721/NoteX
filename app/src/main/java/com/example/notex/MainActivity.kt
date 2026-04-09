package com.example.notex

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

/*val colorPicker = findViewById<ColorPickerView>(R.id.colorPickerView)

colorPicker.setColorListener { color, _ ->
    // use selected color
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorPicker = findViewById<ColorPickerView>(R.id.colorPickerView)

        colorPicker.setColorListener { color, _ ->
            // use selected color
        }
    }
}
paste inside onview created
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val colorPicker = view.findViewById<ColorPickerView>(R.id.colorPickerView)

    colorPicker.setColorListener { color, _ ->
        // use selected color
    }
}

 */