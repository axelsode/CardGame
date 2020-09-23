package com.example.cardgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    lateinit var nameText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        nameText = findViewById<EditText>(R.id.editTextTextPersonName)

        button.setOnClickListener {
            startBlackJackActivity()
        }

    }

    private fun startBlackJackActivity(){
        val name = nameText.text.toString()
        val intent = Intent(this, BlackJackActivity::class.java)
        intent.putExtra("playerName", name)
        startActivity(intent)
    }
}