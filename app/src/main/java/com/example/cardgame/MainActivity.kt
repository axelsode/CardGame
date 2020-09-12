package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dealerCar1 = findViewById<ImageView>(R.id.dealer1)
        val dealerCar2 = findViewById<ImageView>(R.id.dealer2)
        val dealerCar3 = findViewById<ImageView>(R.id.dealer3)
        val dealerCar4 = findViewById<ImageView>(R.id.dealer4)

        dealerCar1.visibility = View.VISIBLE
        dealerCar2.visibility = View.INVISIBLE
        dealerCar3.visibility = View.INVISIBLE
        dealerCar4.visibility = View.INVISIBLE


    }
}