package com.example.cardgame

class Dealer(var deck : Decks, var hand :  ArrayList<Int>? = ArrayList<Int>()) {

    fun takeCard() : Card{
        val card = deck.takeCard()!!
        hand?.add(card.value)
        return card
    }

    fun valuateHand() : Int{
        var ace = 0
        var restOfHand = 0
        for (elm in this.hand!!) {
            when (elm) {
                14 -> ace++
                10, 11, 12, 13 -> restOfHand += 10
                else -> restOfHand += elm
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

    fun playRestOfCards() : Int{
        while (valuateHand() < 17){
            val card = takeCard()
            hand?.add(card.value)
        }
        return valuateHand()
    }



}