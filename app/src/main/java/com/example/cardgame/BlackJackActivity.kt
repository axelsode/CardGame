package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_black_jack.*
import kotlinx.android.synthetic.main.activity_main.*

class BlackJackActivity : AppCompatActivity() {

    val dealerList : ArrayList<ImageView>? = ArrayList<ImageView>()
    val playerList : ArrayList<ImageView>? = ArrayList<ImageView>()
    val myDecks = Decks(1)
    var dealerHand = Dealer(myDecks)
    var playerHand = Dealer(myDecks)
    var dealercardNum = 0
    var playercardNum = 0
    var dealerScore = 0
    var playerScore = 0
    lateinit var dealerScoreText : TextView
    lateinit var playerScoreText : TextView


    /* visa poängen på dealers hand atm.
    lateinit var dealersHandValue : TextView
    lateinit var playersHandValue : TextView
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)

        dealerScoreText = findViewById<TextView>(R.id.dealerScoretextView)
        playerScoreText = findViewById<TextView>(R.id.playerScoretextView)
        dealerScoreText.text = getString(R.string.dealer_points, dealerScore.toString())
        playerScoreText.text = getString(R.string.player_points, playerScore.toString())

        /*
         dealersHandValue = findViewById(R.id.dealersHandValue)
         dealersHandValue.text = playerHand.valuateHand().toString()
         */


        val dealerCar1 = findViewById<ImageView>(R.id.dealer1)
        val dealerCar2 = findViewById<ImageView>(R.id.dealer2)
        val dealerCar3 = findViewById<ImageView>(R.id.dealer3)
        val dealerCar4 = findViewById<ImageView>(R.id.dealer4)
        val playerCar1 = findViewById<ImageView>(R.id.player1)
        val playerCar2 = findViewById<ImageView>(R.id.player2)
        val playerCar3 = findViewById<ImageView>(R.id.player3)
        val playerCar4 = findViewById<ImageView>(R.id.player4)

        dealerList?.add(dealerCar1)
        dealerList?.add(dealerCar2)
        dealerList?.add(dealerCar3)
        dealerList?.add(dealerCar4)

        playerList?.add(playerCar1)
        playerList?.add(playerCar2)
        playerList?.add(playerCar3)
        playerList?.add(playerCar4)

        myDecks.addDecks()
        myDecks.shuffleDecks()

        startGame()

        val hitButton = findViewById<Button>(R.id.hitButton)
        hitButton.setOnClickListener {
            hit()
        }

        val standButton = findViewById<Button>(R.id.standButton)
        standButton.setOnClickListener {
            stand()
        }

        val newGameButton = findViewById<Button>(R.id.playAgainButton)
        newGameButton.setOnClickListener {
            startGame()
        }
    }

    fun startGame(){
        for (dealer in dealerList!!){
            dealer.visibility = View.INVISIBLE
        }
        for (player in playerList!!){
            player.visibility = View.INVISIBLE
        }

        /*
        dealerList?.get(0)?.visibility = View.INVISIBLE
        dealerList?.get(1)?.visibility = View.INVISIBLE
        dealerList?.get(2)?.visibility = View.INVISIBLE
        dealerList?.get(3)?.visibility = View.INVISIBLE
        playerList?.get(0)?.visibility = View.INVISIBLE
        playerList?.get(1)?.visibility = View.INVISIBLE
        playerList?.get(2)?.visibility = View.INVISIBLE
        playerList?.get(3)?.visibility = View.INVISIBLE
        */
        dealerHand = Dealer(myDecks)
        playerHand = Dealer(myDecks)

        val dealerFirstCard = dealerHand.takeCard()
        dealerList?.get(0)?.setImageResource(dealerFirstCard.getImageId(this))
        dealerList?.get(0)?.visibility = View.VISIBLE

        val playerFirstCard = playerHand.takeCard()
        playerList?.get(0)?.setImageResource(playerFirstCard.getImageId(this))
        playerList?.get(0)?.visibility = View.VISIBLE

        val playerSecondCard = playerHand.takeCard()
        playerList?.get(1)?.setImageResource(playerSecondCard.getImageId(this))
        playerList?.get(1)?.visibility = View.VISIBLE

        dealercardNum = 1
        playercardNum = 2

        hitButton.visibility = View.VISIBLE
        standButton.visibility = View.VISIBLE


        if (playerHand.valuateHand() == 21){
            hitButton.visibility = View.INVISIBLE
            standButton.visibility = View.INVISIBLE
            playerWins()
        }

    }

    /* att new game knappen är gömd till spelaren över 20 och dealern över 16
    fun newGameButton (){
        when {
            playerHand.valuateHand()< 20 or dealerHand.valuateHand()< 16 -> {
                playAgainButton.visibility = View.INVISIBLE
            }
        }
    }
      */

    fun hit(){
        if (playercardNum < 4){
            val playedCard = playerHand.takeCard()
            playerList?.get(playercardNum)?.setImageResource(playedCard.getImageId(this))
            playerList?.get(playercardNum)?.visibility = View.VISIBLE
            playercardNum++
        }


        when{
            playerHand.valuateHand() > 21 -> {
                hitButton.visibility = View.INVISIBLE
                standButton.visibility = View.INVISIBLE
                dealerWins()

            }
            playerHand.valuateHand() == 21 -> {
                hitButton.visibility = View.INVISIBLE
                standButton.visibility = View.INVISIBLE

                playerWins()
            }
        }
    }

    fun stand(){
        while ((dealercardNum<4) && (dealerHand.valuateHand() < 17)){
            val playedCard = dealerHand.takeCard()
            dealerList?.get(dealercardNum)?.setImageResource(playedCard.getImageId(this))
            dealerList?.get(dealercardNum)?.visibility = View.VISIBLE
            dealercardNum++
        }

        hitButton.visibility = View.INVISIBLE
        standButton.visibility = View.INVISIBLE

        if (dealerHand.valuateHand() > 21){
            playerWins()
        }else if (playerHand.valuateHand() < dealerHand.valuateHand()){
            dealerWins()
        }else if (playerHand.valuateHand() > dealerHand.valuateHand()){
            playerWins()
        }else if (playerHand.valuateHand() > 18){
            playerWins()
        }else{
            dealerWins()
        }



        /*
    when{
        dealerHand.valuateHand() > 21 -> {
           playerWins()
        }
        playerHand.valuateHand() >= dealerHand.valuateHand() -> {
            playerWins()
        }
        playerHand.valuateHand() < dealerHand.valuateHand() -> {
            dealerWins()
        }
        */
    }

    fun playerWins(){
        playerScore++
        playerScoreText.text = getString(R.string.player_points, playerScore.toString())
        Toast.makeText(this, getString(R.string.player_wins), Toast.LENGTH_SHORT).show()
    }

    fun dealerWins(){
        dealerScore++
        dealerScoreText.text = getString(R.string.dealer_points, dealerScore.toString())
        Toast.makeText(this, getString(R.string.dealer_wins), Toast.LENGTH_SHORT).show()
    }
}