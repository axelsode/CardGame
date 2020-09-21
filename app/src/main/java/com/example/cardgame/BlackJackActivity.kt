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
    val playerSplitList : ArrayList<Card>? = ArrayList<Card>()
    val playerResultList : ArrayList<Int>? = ArrayList<Int>()
    val myDecks = Decks(1)
    var dealerHand = Dealer(myDecks)
    var playerHand = Dealer(myDecks)
    var playerFirstCard = Card()
    var playerSecondCard = Card()
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

    @ExperimentalStdlibApi
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

        val splitButton = findViewById<Button>(R.id.splitButton)
        splitButton.setOnClickListener {
            split()
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
        playerSplitList?.clear()
        playerResultList?.clear()

        for (dealer in dealerList!!){
            dealer.visibility = View.INVISIBLE
        }
        for (player in playerList!!){
            player.visibility = View.INVISIBLE
        }

        dealerHand = Dealer(myDecks)
        playerHand = Dealer(myDecks)

        val dealerFirstCard = dealerHand.takeCard()
        dealerList?.get(0)?.setImageResource(dealerFirstCard.getImageId(this))
        dealerList?.get(0)?.visibility = View.VISIBLE

        playerFirstCard = playerHand.takeCard()
        playerList?.get(0)?.setImageResource(playerFirstCard.getImageId(this))
        playerList?.get(0)?.visibility = View.VISIBLE

        playerSecondCard = playerHand.takeCard()
        playerList?.get(1)?.setImageResource(playerSecondCard.getImageId(this))
        playerList?.get(1)?.visibility = View.VISIBLE

        dealercardNum = 1
        playercardNum = 2

        hitButton.visibility = View.VISIBLE
        standButton.visibility = View.VISIBLE
        isSplitable()

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
            isSplitable()
        }

        if (playerSplitList.isNullOrEmpty() && playerResultList.isNullOrEmpty()){
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
        }else{
            when{
                playerHand.valuateHand() > 21 -> {
                    hitButton.visibility = View.INVISIBLE
                    dealerWins()
                }
                playerHand.valuateHand() == 21 -> {
                    hitButton.visibility = View.INVISIBLE
                    playerWins()
                }
            }
        }
    }

    fun split(){
        val cardToMove = playerSecondCard
        val cardValue = playerHand.hand?.removeAt(1)
        playerSplitList?.add(cardToMove)
        playerList?.get(1)?.visibility = View.INVISIBLE
        playercardNum = 1
    }

    fun isSplitable() {
        val firstCard : Int
        val secondCard : Int
        when(playerFirstCard.value){
            10,11,12,13 -> firstCard = 10
            else -> firstCard = playerFirstCard.value
        }
        when(playerSecondCard.value){
            10,11,12,13 -> secondCard = 10
            else -> secondCard = playerSecondCard.value
        }
        if (playercardNum == 2 && firstCard == secondCard){
            splitButton.visibility = View.VISIBLE
        }else{
            splitButton.visibility = View.INVISIBLE
        }
    }



    @ExperimentalStdlibApi
    fun stand(){
        playerResultList?.add(playerHand.valuateHand())
        if (!playerSplitList.isNullOrEmpty()){
            playerHand.clear()
            val firstCard = playerSplitList.first()
            playerHand.addCard(firstCard)
            for (cardIm in playerList!!){
                cardIm.visibility = View.INVISIBLE
            }
            playerList.get(0).setImageResource(firstCard.getImageId(this))
            playerList.get(0).visibility = View.VISIBLE
            playerSplitList?.removeFirst()
            playercardNum = 1

        }else{
            while ((dealercardNum<4) && (dealerHand.valuateHand() < 17)){
                val playedCard = dealerHand.takeCard()
                dealerList?.get(dealercardNum)?.setImageResource(playedCard.getImageId(this))
                dealerList?.get(dealercardNum)?.visibility = View.VISIBLE
                dealercardNum++
            }

            hitButton.visibility = View.INVISIBLE
            standButton.visibility = View.INVISIBLE

            if (playerResultList != null) {
                for (elm in playerResultList){
                    if (elm == 21){
                        // this case have already assigned player win! in hit function.
                    }else if (dealerHand.valuateHand() > 21){
                        playerWins()
                    }else if (elm < dealerHand.valuateHand()){
                        dealerWins()
                    }else if (elm > dealerHand.valuateHand()){
                        playerWins()
                    }else if (elm > 19){
                        playerWins()
                    }else{
                        dealerWins()
                    }
                }
            }
        }
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