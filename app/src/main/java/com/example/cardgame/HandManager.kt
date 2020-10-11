package com.example.cardgame

object HandManager {
    val hands = mutableListOf<Hand>()
    var activeHand = 0
    var valueAtDealerHand = 0
    var gameFinished = false

    fun addHand(hand: Hand){
        hands.add(hand)
    }
    fun addHandAt(hand: Hand){
        /*val first = hands[0]
        val rest = hands
        rest.removeAt(0)
        hands.clear()
        hands.add(first)
        hands.add(hand)
        hands.addAll(rest)*/
        hands.add(1, hand)
    }
    fun clearHands(){
        hands.clear()
        activeHand = 0
        gameFinished = false
        valueAtDealerHand = 0
    }

}