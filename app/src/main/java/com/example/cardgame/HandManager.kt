package com.example.cardgame

object HandManager {
    val hands = mutableListOf<Hand>()
    var activeHand = 0

    fun addHand(hand: Hand){
        hands.add(hand)
    }
    fun clearHands(){
        hands.clear()
        activeHand = 0
    }

}