package com.example.cardgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            startBlackJackActivity()
        }

    }

    private fun startBlackJackActivity(){
      //  val rightAnswer =checkAnswer()
        val intent = Intent(this, BlackJackActivity::class.java)
        //intent.putExtra("correctAnswer", rightAnswer)
        startActivity(intent)
    }
}