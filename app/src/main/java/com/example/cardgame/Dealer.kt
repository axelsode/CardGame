package com.example.cardgame

import kotlin.math.min

class Dealer(private var deck : Decks, var hand :  ArrayList<Card>? = ArrayList<Card>()) {

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
        if (ace == 0){
            return restOfHand
        }else if(restOfHand + 11 + ace -1 <= 21){
            return restOfHand + 11 + ace -1
        }else
            return restOfHand +ace
    }

    fun addCard(card: Card){
        hand?.add(card)
    }

    fun clear(){
        hand = ArrayList()
    }

}