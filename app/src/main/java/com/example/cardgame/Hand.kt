package com.example.cardgame

class Hand(var cards: MutableList<Card> = mutableListOf<Card>(), var win : Int = 0, var activ : Boolean = false,
           var valueAtPlayerHand : Int = 0){
    fun addCard(card : Card){
        cards.add(card)
    }
    fun removeCardSplitCard(){
        cards.removeAt(1)
    }
    fun valuateHand() : Int{
        var ace = 0
        var restOfHand = 0
        for (elm in this.cards!!) {
            when (elm.value) {
                14 -> ace++
                10, 11, 12, 13 -> restOfHand += 10
                else -> restOfHand += elm.value
            }
        }
        while (ace > 0) {
            if (restOfHand + 11 <= 21) {
                restOfHand += 11
                ace--
            } else {
                restOfHand++
                ace--
            }
        }
        return restOfHand
    }
}