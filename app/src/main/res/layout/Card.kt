package layout

import android.content.Context

class Card (var value: Int = 14, var color : String = "h"){

    fun initRandomCard(){
        value = (2..14).random()
        val num = (1..4).random()
        when(num){
            1 -> color = "c"
            2 -> color = "d"
            3 -> color = "h"
            4 -> color = "s"
        }
    }

    fun getImageId(context: Context) : Int {
        val str = color + value
        return context.getResources().getIdentifier("drawable/" + str, null, context.getPackageName())
    }
}