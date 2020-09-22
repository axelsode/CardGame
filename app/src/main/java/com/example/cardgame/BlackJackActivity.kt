package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_black_jack.*


class BlackJackActivity : AppCompatActivity() {

    private val dealerList : ArrayList<ImageView>? = ArrayList<ImageView>()
    private val playerList : ArrayList<ImageView>? = ArrayList<ImageView>()
    private val playerSplitList : ArrayList<Card>? = ArrayList<Card>()
    private val playerResultList : ArrayList<Int>? = ArrayList<Int>()
    private val myDecks = Decks(6)
    private var dealerHand = Dealer(myDecks)
    private var playerHand = Dealer(myDecks)
    private var playerFirstCard = Card()
    private var playerSecondCard = Card()
    private var dealercardNum = 0
    private var playercardNum = 0
    private var dealerScore = 0
    private var playerScore = 0
    private lateinit var dealerScoreText : TextView
    private lateinit var playerScoreText : TextView
    private lateinit var newGameButton : Button

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
        playerScoreText.text = getString(R.string.player_points, intent.getStringExtra("playerName"), playerScore.toString())
        val player_name = findViewById<TextView>(R.id.playertextView)
        player_name.text = intent.getStringExtra("playerName")
        /*
         dealersHandValue = findViewById(R.id.dealersHandValue)
         dealersHandValue.text = playerHand.valuateHand().toString()
         */

        newGameButton = findViewById<Button>(R.id.playAgainButton)

        val dealerCard1 = findViewById<ImageView>(R.id.dealer1)
        val dealerCard2 = findViewById<ImageView>(R.id.dealer2)
        val dealerCard3 = findViewById<ImageView>(R.id.dealer3)
        val dealerCard4 = findViewById<ImageView>(R.id.dealer4)
        val dealerCard5 = findViewById<ImageView>(R.id.dealer5)
        val dealerCard6 = findViewById<ImageView>(R.id.dealer6)

        val playerCard1 = findViewById<ImageView>(R.id.player1)
        val playerCard2 = findViewById<ImageView>(R.id.player2)
        val playerCard3 = findViewById<ImageView>(R.id.player3)
        val playerCard4 = findViewById<ImageView>(R.id.player4)
        val playerCard5 = findViewById<ImageView>(R.id.player5)
        val playerCard6 = findViewById<ImageView>(R.id.player6)

        dealerList?.add(dealerCard1)
        dealerList?.add(dealerCard2)
        dealerList?.add(dealerCard3)
        dealerList?.add(dealerCard4)
        dealerList?.add(dealerCard5)
        dealerList?.add(dealerCard6)

        playerList?.add(playerCard1)
        playerList?.add(playerCard2)
        playerList?.add(playerCard3)
        playerList?.add(playerCard4)
        playerList?.add(playerCard5)
        playerList?.add(playerCard6)

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


        newGameButton.setOnClickListener {
            startGame()
        }

    }

    private fun startGame(){
        playerSplitList?.clear()
        playerResultList?.clear()
        newGameButton.visibility = View.INVISIBLE

        for (dealer in dealerList!!){
            dealer.visibility = View.INVISIBLE
        }
        for (player in playerList!!){
            player.visibility = View.INVISIBLE
        }

        this.dealerHand = Dealer(myDecks)
        this.playerHand = Dealer(myDecks)

        val dealerFirstCard = dealerHand.takeCard()
        dealerList[0].setImageResource(dealerFirstCard.getImageId(this))
        dealerList[0].visibility = View.VISIBLE

        playerFirstCard = playerHand.takeCard()
        playerList[0].setImageResource(playerFirstCard.getImageId(this))
        playerList[0].visibility = View.VISIBLE

        playerSecondCard = playerHand.takeCard()
        playerList[1].setImageResource(playerSecondCard.getImageId(this))
        playerList[1].visibility = View.VISIBLE

        dealercardNum = 1
        playercardNum = 2

        hitButton.visibility = View.VISIBLE
        standButton.visibility = View.VISIBLE
        isSplitable()

        if (playerHand.valuateHand() == 21){
            hitButton.visibility = View.INVISIBLE
            standButton.visibility = View.INVISIBLE
            newGameButton.visibility = View.VISIBLE
            playerWins()
        }

    }

    private fun hit(){
        isSplitable()
        if (playercardNum < 5){
            val playedCard = playerHand.takeCard()
            playerList?.get(playercardNum)?.setImageResource(playedCard.getImageId(this))
            playerList?.get(playercardNum)?.visibility = View.VISIBLE
            playercardNum++

        }

        if (playerSplitList.isNullOrEmpty() && playerResultList.isNullOrEmpty()){
            when{
                playerHand.valuateHand() > 21 -> {
                    hitButton.visibility = View.INVISIBLE
                    standButton.visibility = View.INVISIBLE
                    newGameButton.visibility = View.VISIBLE
                    dealerWins()
                }
                playerHand.valuateHand() == 21 -> {
                    hitButton.visibility = View.INVISIBLE
                    standButton.visibility = View.INVISIBLE
                    newGameButton.visibility = View.VISIBLE
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

    private fun split(){
        val cardToMove = playerSecondCard
        playerHand.hand?.removeAt(1)
        playerSplitList?.add(cardToMove)
        playerList?.get(1)?.visibility = View.INVISIBLE
        playercardNum = 1
        playerSecondCard = Card()
        splitButton.visibility = View.INVISIBLE
    }

    private fun isSplitable() {
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
    private fun stand(){
        playerResultList?.add(playerHand.valuateHand())
        if (!playerSplitList.isNullOrEmpty()){
            hitButton.visibility = View.VISIBLE
            this.playerHand.clear()
            val firstCard = playerSplitList.first()
            this.playerHand.addCard(firstCard)
            for (cardIm in playerList!!){
                cardIm.visibility = View.INVISIBLE
            }
            playerList[0].setImageResource(firstCard.getImageId(this))
            playerList[0].visibility = View.VISIBLE
            playerSplitList.removeFirst()
            playercardNum = 1

        }else{
            newGameButton.visibility = View.VISIBLE
            while ((dealercardNum<5) && (dealerHand.valuateHand() < 17)){
                val playedCard = dealerHand.takeCard()
                dealerList?.get(dealercardNum)?.setImageResource(playedCard.getImageId(this))
                dealerList?.get(dealercardNum)?.visibility = View.VISIBLE
                dealercardNum++
            }

            hitButton.visibility = View.INVISIBLE
            standButton.visibility = View.INVISIBLE
            splitButton.visibility = View.INVISIBLE

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
        playerScoreText.text = getString(R.string.player_points, intent.getStringExtra("playerName"), playerScore.toString())
        Toast.makeText(this, playerScoreText.text, Toast.LENGTH_SHORT).show()
    }

    fun dealerWins(){
        dealerScore++
        dealerScoreText.text = getString(R.string.dealer_points, dealerScore.toString())
        Toast.makeText(this, dealerScoreText.text, Toast.LENGTH_SHORT).show()
    }
}