package com.example.cardgame

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun insert(user : User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user")
    fun getAll() : List<User>


}