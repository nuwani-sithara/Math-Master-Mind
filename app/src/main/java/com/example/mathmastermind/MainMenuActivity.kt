package com.example.mathmastermind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val startQuizButton: Button = findViewById(R.id.startQuizButton)
        startQuizButton.setOnClickListener {
            // Navigate to the game activity when the "Start the Quiz" button is clicked
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the main menu activity to prevent going back to it using the back button
        }
    }
}

