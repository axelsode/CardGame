package com.example.cardgame
//package layout

class Decks(var numberOfDecks : Int = 1, var decks : ArrayList<Card>? = ArrayList<Card>(), var numberOfCards : Int = 0) {

    fun addDecks(){
        val colors = arrayOf("h","s","d","c")
        //val numbers = arrayOf(2,3,4,5,6,7,8,9,10,11,12,13,14)
        val numbers = arrayOf(10,11,12,13,14)
        for (num in numbers){
            for (color in  colors){
                if (numberOfDecks == 1){
                    numberOfCards++
                    decks?.add(Card(num, color))
                }else{
                    for (i in 1..numberOfDecks){
                        numberOfCards++
                        decks?.add(Card(num, color))
                    }
                }
            }
        }
    }

    fun clearDecks(){
        decks = ArrayList<Card>()
    }

    fun shuffleDecks(){
        decks?.shuffle()
    }

    fun takeCard() : Card? {
        if (numberOfCards < 40){
            clearDecks()
            addDecks()
            shuffleDecks()
        }
        val card = decks?.get(0)
        decks = decks?.drop(1) as ArrayList<Card>?
        if (numberOfCards > 0){
            numberOfCards--
        }
        return card
    }
}