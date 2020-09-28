package com.example.cardgame

object HandManager {
    val hands = mutableListOf<Hand>()

    init {

    }




    fun addHand(hand: Hand){
        hands.add(hand)
    }
}