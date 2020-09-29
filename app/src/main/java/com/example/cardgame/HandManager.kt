package com.example.cardgame

object HandManager {
    val hands = mutableListOf<Hand>()
    var activeHand = 0
    var valueAtDealerHand = 0
    var gameFinished = false

    fun addHand(hand: Hand){
        hands.add(hand)
    }
    fun clearHands(){
        hands.clear()
        activeHand = 0
        gameFinished = false
        valueAtDealerHand = 0
    }

}