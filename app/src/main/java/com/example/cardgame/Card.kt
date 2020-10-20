package com.example.cardgame
import android.content.Context

class Card (var value: Int = -1, private var color : String = "T", var isVisible : Boolean = true){

    fun getImageId(context: Context) : Int {
        val str = color + value
        return context.getResources().getIdentifier("drawable/" + str, null, context.getPackageName())
    }
}