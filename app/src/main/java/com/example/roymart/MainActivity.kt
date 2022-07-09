package com.example.roymart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var buttonChangeText: Button
    private lateinit var textViewWord: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonChangeText = findViewById(R.id.buttonChangeText)
        textViewWord = findViewById(R.id.textViewWord)

        buttonChangeText.setOnClickListener {
            textViewWord.text = "GAAAAAAA"
        }
    }

}