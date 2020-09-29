package com.example.cardgame

class Hand(var cards: MutableList<Card> = mutableListOf<Card>(), var win : Int = 0, var activ : Boolean = false){
    fun addCard(card : Card){
        cards.add(card)
    }
    fun removeCardSplitCard(){
        cards.removeAt(1)
    }
}