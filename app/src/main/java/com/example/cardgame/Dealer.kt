package com.example.cardgame

class Dealer(var deck : Decks, var hand :  ArrayList<Card>? = ArrayList<Card>()) {

    fun takeCard() : Card{
        val card = deck.takeCard()!!
        hand?.add(card)
        return card
    }

    fun valuateHand() : Int{
        var ace = 0
        var restOfHand = 0
        for (elm in this.hand!!) {
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

    fun addCard(card: Card){
        hand?.add(card)
    }

    fun clear(){
        hand = ArrayList<Card>()
    }

}