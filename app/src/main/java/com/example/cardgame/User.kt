package com.example.cardgame

import android.widget.TextView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*


@Entity
class User(@PrimaryKey(autoGenerate = true) val id: Int,
           @ColumnInfo(name = "name")val name: String,
           @ColumnInfo(name = "password")val password: String,
           @ColumnInfo(name = "cash")val cash: Int,
           @ColumnInfo(name = "time")val time: Long){

    fun getDateTime(time: Long):  String?{
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val calender = Calendar.getInstance()
        sdf.timeZone = TimeZone.getTimeZone(calender.timeZone.toString())
        val date = Date(time)
        return sdf.format(date)
    }
}