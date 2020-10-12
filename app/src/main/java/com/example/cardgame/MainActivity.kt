package com.example.cardgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_black_jack.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var nameText: EditText
    lateinit var betText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        nameText = findViewById<EditText>(R.id.editTextTextPersonName)
        betText = findViewById<EditText>(R.id.cashEditText)
        button.setOnClickListener {

           if (betText.text.isNotBlank()){
               startBlackJackActivity()
           } else{
               Toast.makeText(this,getString(R.string.Fill_in_cash), Toast.LENGTH_SHORT).show()
           }
        }
    }

    private fun startBlackJackActivity(){
        val name = nameText.text.toString()
        val bet = betText.text.toString()
        val list = arrayOf(name, bet)
        val intent = Intent(this, BlackJackActivity::class.java)
        intent.putExtra("playerName", name)
        intent.putExtra("playerCash", bet)
        startActivity(intent)
    }
}