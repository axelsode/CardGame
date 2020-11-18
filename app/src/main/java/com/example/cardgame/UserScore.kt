package com.example.cardgame

import java.text.SimpleDateFormat
import java.util.*

class UserScore(val name: String, val cash : Int, val time : Long, val change : Double) {

    fun getDateTime(time: Long):  String?{
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val calender = Calendar.getInstance()
        sdf.timeZone = TimeZone.getTimeZone(calender.timeZone.toString())
        val date = Date(time)
        return sdf.format(date)
    }
}