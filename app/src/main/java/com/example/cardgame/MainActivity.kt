package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        var time = 1000
        var timerOn = true

        fun a() = object : CountDownTimer(time.toLong(), 10000) {
            override fun onFinish() {

            }

            override fun onTick(p0: Long) {

            }

        }
        */

        val dealerCar1 = findViewById<ImageView>(R.id.dealer1)
        val dealerCar2 = findViewById<ImageView>(R.id.dealer2)
        val dealerCar3 = findViewById<ImageView>(R.id.dealer3)
        val dealerCar4 = findViewById<ImageView>(R.id.dealer4)


        val dealerList : ArrayList<ImageView>? = ArrayList<ImageView>()
        dealerList?.add(dealerCar1)
        dealerList?.add(dealerCar2)
        dealerList?.add(dealerCar3)
        dealerList?.add(dealerCar4)




        val myDecks = Decks(1)
        myDecks.addDecks()
        myDecks.shuffleDecks()
        val myDealer = Dealer(myDecks)

        /*
        val firstCard = myDealer.takeCard()
        dealerCar1.visibility = View.VISIBLE
        dealerCar1.setImageResource(firstCard.getImageId(this))
        Log.d("card", "${firstCard.value}, ${firstCard.color}")

        val secondCard = myDealer.takeCard()
        dealerCar2.visibility = View.VISIBLE
        dealerCar2.setImageResource(secondCard.getImageId(this))
        */


        //var cardNum = 0

        for (cardNum in 0..3){
            val playedCard = myDealer.takeCard()
            dealerList?.get(cardNum)?.setImageResource(playedCard.getImageId(this))
            dealerList?.get(cardNum)?.visibility = View.VISIBLE

        }

    }
}