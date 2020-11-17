package com.example.cardgame

import android.widget.TextView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User(@PrimaryKey(autoGenerate = true) val id: Int,
           @ColumnInfo(name = "name")val name: String,
           @ColumnInfo(name = "password")val password: String,
           @ColumnInfo(name = "cash")val cash: Int,
           @ColumnInfo(name = "time")val time: Long){

}