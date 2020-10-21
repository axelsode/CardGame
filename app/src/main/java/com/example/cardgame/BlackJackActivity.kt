package com.example.cardgame

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_black_jack.*
import kotlin.math.abs
import kotlin.properties.Delegates


class BlackJackActivity : AppCompatActivity() {
    private lateinit var frontAnim : AnimatorSet
    private lateinit var backAnim : AnimatorSet

    private val dealerList : ArrayList<ImageView>? = ArrayList()
    private val dealerInvisibleList : ArrayList<ImageView>? = ArrayList()
    private val playerList : ArrayList<ImageView>? = ArrayList<ImageView>()
    private val playerInvisibleList : ArrayList<ImageView>? = ArrayList()
    private val playerSplitList : ArrayList<Card>? = ArrayList<Card>()
    private val playerResultList : ArrayList<Int>? = ArrayList<Int>()
    private val myDecks = Decks(6)
    private var dealerHand = Dealer(myDecks)
    private var playerHand = Dealer(myDecks)
    private var playerFirstCard = Card()
    private var playerSecondCard = Card()
    private var dealercardNum = 0
    private var playercardNum = 0
    private var playerScore = 0
    private var startPoint = 0
    private var endPoint = 10
    private var betSize = 0
    var cash by Delegates.notNull<Int>()
    private lateinit var playerScoreText : TextView
    private lateinit var newGameButton : Button
    private lateinit var setBetSeek : SeekBar
    private lateinit var cardsLeft : TextView


    lateinit var dealersHandValue : TextView
    lateinit var playersHandValue : TextView


    @SuppressLint("CutPasteId")
    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)

        cardsLeft = findViewById(R.id.cardleft)

        playerScoreText = findViewById(R.id.playerScoretextView)
        playerScoreText.text = getString(
            R.string.player_points,
            intent.getStringExtra("playerName"),
            playerScore.toString()
        )

        val playerName = findViewById<TextView>(R.id.playertextView)
        playerName.text = intent.getStringExtra("playerName")

        val playerCash = findViewById<TextView>(R.id.playerScoretextView)
        cash = abs(intent.getStringExtra("playerCash")!!.toInt())
        playerCash.text = cash.toString()

        endPoint = intent.getStringExtra("playerCash")!!.toInt()

        dealersHandValue = findViewById(R.id.dealersHandValue)
        dealersHandValue.text = dealerHand.valuateHand().toString()

        playersHandValue = findViewById(R.id.playersHandValue)
        playersHandValue.text = playerHand.valuateHand().toString()

        newGameButton = findViewById(R.id.playAgainButton)
        setBetSeek = findViewById(R.id.seekBar)

        dealerList?.add(dealer1)
        dealerList?.add(dealer2)
        dealerList?.add(dealer3)
        dealerList?.add(dealer4)
        dealerList?.add(dealer5)
        dealerList?.add(dealer6)

        dealerInvisibleList?.add(dealer1_invisible)
        dealerInvisibleList?.add(dealer2_invisible)
        dealerInvisibleList?.add(dealer3_invisible)
        dealerInvisibleList?.add(dealer4_invisible)
        dealerInvisibleList?.add(dealer5_invisible)
        dealerInvisibleList?.add(dealer6_invisible)

        playerList?.add(player1)
        playerList?.add(player2)
        playerList?.add(player3)
        playerList?.add(player4)
        playerList?.add(player5)
        playerList?.add(player6)

        playerInvisibleList?.add(player1_invisible)
        playerInvisibleList?.add(player2_invisible)
        playerInvisibleList?.add(player3_invisible)
        playerInvisibleList?.add(player4_invisible)
        playerInvisibleList?.add(player5_invisible)
        playerInvisibleList?.add(player6_invisible)

        myDecks.addDecks()
        myDecks.shuffleDecks()

        setBetSeek.visibility = View.VISIBLE
        newGameButton.visibility = View.VISIBLE
        hitButton.visibility = View.INVISIBLE
        standButton.visibility = View.INVISIBLE
        splitButton.visibility = View.INVISIBLE

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

        setBetSeek.max = cash
        setBetSeek.min = 5.coerceAtMost(cash)
        var betSizeText = 5.coerceAtMost(cash).toString()
        betTextView.text = getString(R.string.Bet) + ": " + betSizeText
        setBetSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                betSizeText = progress.coerceAtMost(cash).toString()
                betTextView.text = getString(R.string.Bet) + ": " + betSizeText
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    startPoint = seekBar.progress.coerceAtMost(cash)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    endPoint = seekBar.progress
                }
                betSize = seekBar?.progress!!
                //Toast.makeText(this@BlackJackActivity, "Bet changed by ${endPoint-startPoint}", Toast.LENGTH_SHORT).show()
                Toast.makeText(
                    this@BlackJackActivity,
                    "Bet changed to ${seekBar.progress}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        )
    }

    private fun startGame(){

        outOfMoney ()
        betSize = betSize.coerceAtMost(cash)
        cash -= betSize
        playerScoretextView.text = cash.toString()

        playerSplitList?.clear()
        playerResultList?.clear()
        dealersHandValue.text = ""
        playersHandValue.text = ""
        newGameButton.visibility = View.INVISIBLE
        setBetSeek.visibility = View.INVISIBLE
        splitButton.visibility = View.INVISIBLE

        for (dealer in dealerList!!){
            dealer.visibility = View.INVISIBLE
        }
        for (player in playerList!!){
            player.visibility = View.INVISIBLE
        }
        //********
        HandManager.clearHands()
        this.dealerHand = Dealer(myDecks)
        this.playerHand = Dealer(myDecks)

        val dealerFirstCard = dealerHand.takeCard()
        dealerList[0].setImageResource(dealerFirstCard.getImageId(this))


        playerFirstCard = playerHand.takeCard()
        playerList[0].setImageResource(playerFirstCard.getImageId(this))


        playerSecondCard = playerHand.takeCard()
        playerList[1].setImageResource(playerSecondCard.getImageId(this))


        object : CountDownTimer(3000, 1000) {

            var time = 0

            override fun onFinish() {
                isSplitable()
                playersHandValue.text = getString(
                    R.string.player_points, intent.getStringExtra("playerName"),
                    playerHand.valuateHand().toString()
                )
                dealersHandValue.text = getString(
                    R.string.dealer_points,
                    dealerHand.valuateHand().toString()
                )

            }


            override fun onTick(p0: Long) {
                when (time){
                    0 -> {
                        playerList[0].visibility = View.VISIBLE
                        flipCard(player1, player1_invisible)
                        time++
                    }
                    1 -> {
                        dealerList[0].visibility = View.VISIBLE
                        flipCard(dealer1, dealer1_invisible)
                        time++
                    }
                    2 -> {
                        playerList[1].visibility = View.VISIBLE
                        flipCard(player2, player2_invisible)
                    }
                }
            }
        }.start()

        HandManager.addHand(Hand(mutableListOf(playerFirstCard, playerSecondCard)))
        HandManager.hands[HandManager.activeHand].valueAtPlayerHand = HandManager.hands[HandManager.activeHand].valuateHand()

        dealercardNum = 1
        playercardNum = 2

        standButton.visibility = View.VISIBLE

        if(playerHand.valuateHand() == 21){
            hitButton.visibility = View.INVISIBLE
        }else{
            hitButton.visibility = View.VISIBLE
        }

        if (playerHand.valuateHand() == 21 && playercardNum == 2 && dealerHand.valuateHand()<10){
            standButton.visibility = View.INVISIBLE
            newGameButton.visibility = View.VISIBLE
            setBetSeek.visibility = View.VISIBLE
            playerWins()
            HandManager.gameFinished = true
        }
    }

    private fun hit(){

        if(playercardNum == 1){
            playerFirstCard = if (playerHand.valuateHand() == 11){
                Card(14, "s")
            }else{
                Card(playerHand.valuateHand(), "s")
            }
            val playedCard = playerHand.takeCard()
            playerSecondCard = playedCard
            HandManager.hands[HandManager.activeHand].addCard(playedCard)
            HandManager.hands[HandManager.activeHand].valueAtPlayerHand = HandManager.hands[HandManager.activeHand].valuateHand()

            playerList?.get(playercardNum)?.setImageResource(playedCard.getImageId(this))
            playerList?.get(playercardNum)?.visibility = View.VISIBLE
            playerList?.get(playercardNum)?.let { playerInvisibleList?.get(playercardNum)?.let { it1 ->
                flipCard(
                    it,
                    it1
                )
            } }
            playercardNum++
        }else if (playercardNum < 6){
            val playedCard = playerHand.takeCard()
            HandManager.hands[HandManager.activeHand].addCard(playedCard)
            HandManager.hands[HandManager.activeHand].valueAtPlayerHand = HandManager.hands[HandManager.activeHand].valuateHand()

            playerList?.get(playercardNum)?.setImageResource(playedCard.getImageId(this))
            playerList?.get(playercardNum)?.visibility = View.VISIBLE
            playerList?.get(playercardNum)?.let { playerInvisibleList?.get(playercardNum)?.let { it1 ->
                flipCard(
                    it,
                    it1
                )
                }
            }
            playercardNum++
        }

        if (playerSplitList.isNullOrEmpty() && playerResultList.isNullOrEmpty()){
            when{
                playerHand.valuateHand() > 21 -> {
                    hitButton.visibility = View.INVISIBLE
                    standButton.visibility = View.INVISIBLE
                    newGameButton.visibility = View.VISIBLE
                    setBetSeek.visibility = View.VISIBLE
                    dealerWins()
                    HandManager.gameFinished = true

                }
                playerHand.valuateHand() == 21 && playercardNum == 2-> {
                    hitButton.visibility = View.INVISIBLE
                    standButton.visibility = View.INVISIBLE
                    newGameButton.visibility = View.VISIBLE
                    setBetSeek.visibility = View.VISIBLE
                    playerWins()
                    HandManager.gameFinished = true
                }
                playerHand.valuateHand() == 21 -> {
                    hitButton.visibility = View.INVISIBLE
                }
            }
        }else {
            when {
                playerHand.valuateHand() > 21 -> {
                    hitButton.visibility = View.INVISIBLE
                    dealerWins()
                    Toast.makeText(this, "You bust", Toast.LENGTH_SHORT).show()
                }
            }
        }
        playersHandValue.text = getString(
            R.string.player_points, intent.getStringExtra("playerName"),
            playerHand.valuateHand().toString()
        )
        dealersHandValue.text = getString(
            R.string.dealer_points,
            dealerHand.valuateHand().toString()
        )
        isSplitable()
    }

    private fun split(){
        val cardToMove = playerSecondCard
        playerHand.hand?.removeAt(1)
        playerSplitList?.add(cardToMove)
        HandManager.hands[HandManager.activeHand].removeCardSplitCard()
        HandManager.addHand(Hand(mutableListOf(cardToMove)))

        playerList?.get(1)?.visibility = View.INVISIBLE
        playercardNum = 1
        playerSecondCard = Card()
        splitButton.visibility = View.INVISIBLE
        cash -= betSize
        playerScoretextView.text = cash.toString()
    }

    private fun isSplitable() {
        val firstCard : Int = when(playerFirstCard.value){
            10, 11, 12, 13 -> 10
            else -> playerFirstCard.value
        }
        val secondCard : Int = when(playerSecondCard.value){
            10, 11, 12, 13 -> 10
            else -> playerSecondCard.value
        }
        if (playercardNum == 2 && firstCard == secondCard && cash >= betSize){
            splitButton.visibility = View.VISIBLE
        }else{
            splitButton.visibility = View.INVISIBLE
        }
    }



    @ExperimentalStdlibApi
    private fun stand(){
        dealersHandValue.text = getString(
            R.string.dealer_points,
            dealerHand.valuateHand().toString()
        )
        HandManager.hands[HandManager.activeHand].valueAtPlayerHand = HandManager.hands[HandManager.activeHand].valuateHand()
        HandManager.activeHand++
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
            setBetSeek.visibility = View.VISIBLE
            while ((dealercardNum<6) && (dealerHand.valuateHand() < 17)){
                val playedCard = dealerHand.takeCard()
                dealerList?.get(dealercardNum)?.setImageResource(playedCard.getImageId(this))
                dealercardNum++
            }

            val time = dealercardNum * 1000

            object : CountDownTimer(time.toLong(), 1000) {
                var cardnum = 1
                override fun onFinish() {
                    if (playerResultList != null) {
                        for (elm in playerResultList){
                            val case1 = ((elm != 21) && (dealerHand.valuateHand() > 21))
                            val case2 = ((elm <= 21) && (elm > dealerHand.valuateHand()))
                            val case3 = ((elm < 21) && (elm < dealerHand.valuateHand()) && (dealerHand.valuateHand() <= 21))
                            val case4 = (elm == dealerHand.valuateHand())

                            when{
                                case1 -> playerWins()
                                case2 -> playerWins()
                                case3 -> dealerWins()
                                case4 -> draw()
                            }
                        }
                    }
                }

                override fun onTick(p0: Long) {
                    if (cardnum < dealercardNum){
                        dealerList?.get(cardnum)?.visibility = View.VISIBLE
                        dealerList?.get((cardnum).toInt())?.let {
                            dealerInvisibleList?.get((cardnum))?.let { it1 ->
                                flipCard(
                                    it,
                                    it1
                                )
                            }
                        }
                        cardnum++
                    }
                }

            }.start()

            hitButton.visibility = View.INVISIBLE
            standButton.visibility = View.INVISIBLE
            splitButton.visibility = View.INVISIBLE
            HandManager.valueAtDealerHand = dealerHand.valuateHand()
            HandManager.gameFinished = true

        }
    }

    private fun playerWins(){
        cash += 2 * betSize
        setBetSeek.max = cash
        playerScoretextView.text = cash.toString()
        dealersHandValue.text = getString(
            R.string.dealer_points,
            dealerHand.valuateHand().toString()
        )

        playersHandValue.text = getString(
            R.string.player_points, intent.getStringExtra("playerName"),
            playerHand.valuateHand().toString()
        )

        //playerScoreText.text = getString(R.string.player_points, intent.getStringExtra("playerName"), playerScore.toString())
        playerScoreText.text = cash.toString()

       Toast.makeText(this, getString(R.string.player_wins), Toast.LENGTH_SHORT).show()
    }

    private fun dealerWins(){
        setBetSeek.max = cash
        playerScoretextView.text = cash.toString()
        playersHandValue.text = getString(
            R.string.player_points, intent.getStringExtra("playerName"),
            playerHand.valuateHand().toString()
        )

        dealersHandValue.text = getString(
            R.string.dealer_points,
            dealerHand.valuateHand().toString()
        )
        playerScoreText.text = cash.toString()

      Toast.makeText(this, getString(R.string.dealer_wins), Toast.LENGTH_SHORT).show()
    }

    private fun draw(){
        cash += betSize
        setBetSeek.max = cash
        playerScoretextView.text = cash.toString()
        playersHandValue.text = getString(
            R.string.player_points, intent.getStringExtra("playerName"),
            playerHand.valuateHand().toString()
        )
        dealersHandValue.text = getString(
            R.string.dealer_points,
            dealerHand.valuateHand().toString()
        )
        playerScoreText.text = cash.toString()

        Toast.makeText(this, getString(R.string.draw_wins), Toast.LENGTH_SHORT).show()
    }

    private fun outOfMoney (){
        if (cash <= 0){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun flipCard(cardTo: ImageView, cardFrom: ImageView){
        frontAnim = AnimatorInflater.loadAnimator(applicationContext, R.animator.front_animation) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(applicationContext, R.animator.back_animation) as AnimatorSet
        frontAnim.setTarget(cardFrom)
        backAnim.setTarget(cardTo)
        frontAnim.start()
        backAnim.start()
        val mediaPlayer = MediaPlayer.create(this, R.raw.flipcardsound)
        mediaPlayer?.start()
    }
}


