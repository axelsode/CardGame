package layout

class Dealer(var deck : Decks, var hand :  ArrayList<Int>? = ArrayList<Int>()) {

    fun takeCard() : Card{
        val card = deck.takeCard()!!
        hand?.add(card.value)
        return card
    }

    fun valuateHand() : Int{
        var ess = 0
        var restOfHand = 0
        for (elm in this.hand!!) {
            when (elm) {
                14 -> ess++
                10, 11, 12, 13 -> restOfHand += 10
                else -> restOfHand += elm
            }
        }
        while (ess > 0) {
            if (restOfHand + 11 < 21) {
                restOfHand += 11
                ess--
            } else {
                restOfHand++
                ess--
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