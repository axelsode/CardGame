package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

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


        val playerCar1 = findViewById<ImageView>(R.id.player1)
        val playerCar2 = findViewById<ImageView>(R.id.player2)
        val playerCar3 = findViewById<ImageView>(R.id.player3)
        val playerCar4 = findViewById<ImageView>(R.id.player4)


        val playerList : ArrayList<ImageView>? = ArrayList<ImageView>()
        playerList?.add(playerCar1)
        playerList?.add(playerCar2)
        playerList?.add(playerCar3)
        playerList?.add(playerCar4)



        val myDecks = Decks(1)
        myDecks.addDecks()
        myDecks.shuffleDecks()
        val dealerDeck = Dealer(myDecks)
        val playerDeck = Dealer(myDecks)

        val dealerFirstCard = dealerDeck.takeCard()
        dealerList?.get(0)?.setImageResource(dealerFirstCard.getImageId(this))
        dealerList?.get(0)?.visibility = View.VISIBLE

        val playerFirstCard = playerDeck.takeCard()
        playerList?.get(0)?.setImageResource(playerFirstCard.getImageId(this))
        playerList?.get(0)?.visibility = View.VISIBLE

        val playerSecondCard = playerDeck.takeCard()
        playerList?.get(1)?.setImageResource(playerSecondCard.getImageId(this))
        playerList?.get(1)?.visibility = View.VISIBLE


        var dealercardNum = 1
        var playercardNum = 2
        val hitButton = findViewById<Button>(R.id.hitButton)

        hitButton.setOnClickListener {

            if (playercardNum < 3){
                val playedCard = playerDeck.takeCard()
                playerList?.get(playercardNum)?.setImageResource(playedCard.getImageId(this))
                playerList?.get(playercardNum)?.visibility = View.VISIBLE
                playercardNum++

            }else{
                val playedCard = playerDeck.takeCard()
                playerList?.get(playercardNum)?.setImageResource(playedCard.getImageId(this))
                playerList?.get(playercardNum)?.visibility = View.VISIBLE

            }

        }



        val standButton = findViewById<Button>(R.id.standButton)

        standButton.setOnClickListener {
            /*
            if (dealercardNum < 3){
                val playedCard = myDealer.takeCard()
                dealerList?.get(dealercardNum)?.setImageResource(playedCard.getImageId(this))
                dealerList?.get(dealercardNum)?.visibility = View.VISIBLE
                dealercardNum++

            }else{
                val playedCard = myDealer.takeCard()
                dealerList?.get(dealercardNum)?.setImageResource(playedCard.getImageId(this))
                dealerList?.get(dealercardNum)?.visibility = View.VISIBLE

            }
            */
            while ((dealercardNum<4) && (dealerDeck.valuateHand() < 17)){
                val playedCard = dealerDeck.takeCard()
                dealerList?.get(dealercardNum)?.setImageResource(playedCard.getImageId(this))
                dealerList?.get(dealercardNum)?.visibility = View.VISIBLE
            }

        }

        val newGameButton = findViewById<Button>(R.id.playAgainButton)

        newGameButton.setOnClickListener {
        //
        }

        val dealerScore = findViewById<TextView>(R.id.dealerScoretextView)


        val playerScore = findViewById<TextView>(R.id.dealerScoretextView)

        /*
        val firstCard = myDealer.takeCard()
        dealerCar1.visibility = View.VISIBLE
        dealerCar1.setImageResource(firstCard.getImageId(this))
        Log.d("card", "${firstCard.value}, ${firstCard.color}")

        val secondCard = myDealer.takeCard()
        dealerCar2.visibility = View.VISIBLE
        dealerCar2.setImageResource(secondCard.getImageId(this))
        */
        /*
        var cardNum = 0

        val testButton = findViewById<Button>(R.id.testButton)

        testButton.setOnClickListener {

            if (cardNum < 3){
                val playedCard = myDealer.takeCard()
                dealerList?.get(cardNum)?.setImageResource(playedCard.getImageId(this))
                dealerList?.get(cardNum)?.visibility = View.VISIBLE
                cardNum++
                SystemClock.sleep(1000)
            }else{
                val playedCard = myDealer.takeCard()
                dealerList?.get(cardNum)?.setImageResource(playedCard.getImageId(this))
                dealerList?.get(cardNum)?.visibility = View.VISIBLE
                SystemClock.sleep(1000)
            }
        }
        */


        /*
        for (cardNum in 0..3){
            val playedCard = myDealer.takeCard()
            dealerList?.get(cardNum)?.setImageResource(playedCard.getImageId(this))
            dealerList?.get(cardNum)?.visibility = View.VISIBLE
            SystemClock.sleep(1000)
        }

         */


    }

}