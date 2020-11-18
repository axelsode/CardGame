package com.example.cardgame

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.cardgame.AppDatabase.Companion.getInstance
import kotlinx.android.synthetic.main.activity_black_jack.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs
import kotlin.properties.Delegates


class BlackJackActivity : AppCompatActivity(), CoroutineScope {

    private val ON_WEEK =  604800000
    private lateinit var job : Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var db : AppDatabase

    private lateinit var frontAnim : AnimatorSet
    private lateinit var backAnim : AnimatorSet

    private val dealerList : ArrayList<ImageView>? = ArrayList()
    private val dealerInvisibleList : ArrayList<ImageView>? = ArrayList()
    private val playerList : ArrayList<ImageView>? = ArrayList()
    private val playerInvisibleList : ArrayList<ImageView>? = ArrayList()
    private val playerSplitList : ArrayList<Card>? = ArrayList()
    private val playerResultList : ArrayList<Int>? = ArrayList()
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


    @SuppressLint("CutPasteId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_jack)
        job = Job()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user")
            .fallbackToDestructiveMigration()
            .build()
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
        betSize = 5.coerceAtMost(cash)
        betTextView.text = getString(R.string.Bet) + ": " + betSize
        setBetSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                betSize = progress.coerceAtMost(cash)
                betTextView.text = getString(R.string.Bet) + ": " + betSize
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
                    "Bet changed to ${seekBar.progress.coerceAtMost(cash)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_blackjack, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout -> {

               // val intent = Intent(this, MainActivity::class.java)
               // Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
               // startActivity(intent)
               // finish()
                exitToMain()
                true

            }
            R.id.action_highscore -> {
                val intent = Intent(this, HighScoreActivity::class.java)
                Toast.makeText(this, "High Score", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun exitToMain(){
        val name = intent.getStringExtra("playerName")
        val thisUser = name?.let { loadByUserName(it) }
        launch{
            val newCash = cash
            val password = intent.getStringExtra("password")
            val time = System.currentTimeMillis()
            val newUser = User(0, name!!, password!!, newCash, time)
            saveUser(newUser)
            val profile = thisUser?.await()
            if (profile != null){
                var i = 0
                for (elm in profile) {
                         if ((elm.time < time - ON_WEEK)){
                             if(i != 0){
                              deleteUser(elm)
                             }
                             i++
                         }
                    }
                }
            }

        val intent = Intent(this, MainActivity::class.java)
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }
    fun deleteUser(user : User){
        launch(Dispatchers.IO) {
            db.userDao.delete(user)
        }

    }
    fun saveUser(user : User){
        launch(Dispatchers.IO) {
            db.userDao.insert(user)
        }

    }

    fun loadByUserName(name : String) =
        async(Dispatchers.IO) {
            db.userDao.findByUserName(name)
        }

    private fun startGame(){

        //outOfMoney ()

        betSize = betSize.coerceAtMost(cash)
        if (cash > betSize){
            cash -= betSize
        }else{
            cash = 0
        }

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
                playerHand.valuateHand() == 21 && playercardNum >= 2->{
                    hitButton.visibility = View.INVISIBLE
                    standButton.visibility = View.VISIBLE
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
                if ((playercardNum == 2) && (playerHand.valuateHand() == 21) && playerSplitList.isNullOrEmpty()
                    && (dealercardNum == 2)){
                    break
                }
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
                            val case1 = (((dealercardNum == 2) && (dealerHand.valuateHand() == 21))
                                                      && !((playercardNum == 2) && (elm == 21)))
                            val case2 = (((dealercardNum == 2) && (dealerHand.valuateHand() == 21))
                                                      && ((playercardNum == 2) && (elm == 21)))
                            val case3 = (((dealercardNum > 2))
                                                      && ((playercardNum == 2) && (elm == 21)))
                            val case4 = ((elm != 21) && (dealerHand.valuateHand() > 21))
                            val case5 = ((elm <= 21) && (elm > dealerHand.valuateHand()))
                            val case6 = ((elm < 21) && (elm < dealerHand.valuateHand()) && (dealerHand.valuateHand() <= 21))
                            val case7 = (elm == dealerHand.valuateHand())

                            when{
                                case1 -> dealerWins()
                                case2 -> draw()
                                case3 -> playerWins()
                                case4 -> playerWins()
                                case5 -> playerWins()
                                case6 -> dealerWins()
                                case7 -> draw()
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
        playerScoretextView.text = cash.toString()


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
        playerScoretextView.text = cash.toString()


      Toast.makeText(this, getString(R.string.dealer_wins), Toast.LENGTH_SHORT).show()
        outOfMoney ()
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
        playerScoretextView.text = cash.toString()

        Toast.makeText(this, getString(R.string.draw_wins), Toast.LENGTH_SHORT).show()
        outOfMoney ()
    }

    private fun outOfMoney (){
        if (cash <= 0){
            val intent = Intent(this, MainActivity::class.java)
            var timeLeft = 5
            object : CountDownTimer(5000, 1000) {
                override fun onFinish() {
                   // startActivity(intent)
                    exitToMain()
                }

                @SuppressLint("SetTextI18n")
                override fun onTick(p0: Long) {
                    val gameOver = findViewById<TextView>(R.id.gameOverView)
                    val front = findViewById<ImageView>(R.id.game_over_image_front)
                    val back = findViewById<ImageView>(R.id.game_over_image_back)
                    //back.visibility = View.VISIBLE
                    if(timeLeft < 4) {
                        gameOver.text = """${getString((R.string.game_over))}$timeLeft"""
                    }
                    if (timeLeft == 1){
                        back.visibility = View.VISIBLE
                        flipCard(back, front)
                    }
                    timeLeft--
                }

            }.start()
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

