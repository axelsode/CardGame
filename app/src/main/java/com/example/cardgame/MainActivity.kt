package com.example.cardgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var nameText: EditText
    private lateinit var betText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = FirebaseFirestore.getInstance()

        val button = findViewById<Button>(R.id.button)
        nameText = findViewById(R.id.editTextTextPersonName)
        betText = findViewById(R.id.cashEditText)
        button.setOnClickListener {

           if (betText.text.isNotBlank() && betText.text.toString().toInt() > 0 ){
               startBlackJackActivity()
           } else{
               Toast.makeText(this,getString(R.string.Fill_in_cash), Toast.LENGTH_SHORT).show()
           }
        }
    }

    private fun startBlackJackActivity(){
        val name = nameText.text.toString()
        val bet = betText.text.toString()
        val intent = Intent(this, BlackJackActivity::class.java)
        intent.putExtra("playerName", name)
        intent.putExtra("playerCash", bet)
        startActivity(intent)
    }
}