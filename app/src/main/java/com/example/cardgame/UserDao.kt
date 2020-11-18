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

    @Query("SELECT * FROM user WHERE name LIKE :categoryName" )
    fun findByUserName(categoryName: String) : List<User>

    @Query("""SELECT * FROM user WHERE name LIKE :categoryName ORDER BY time LIKE :time DESC LIMIT 1""")
    fun findByUserNameFIRST(categoryName: String, time: Long) : User

    @Query("""SELECT * FROM user WHERE name LIKE :categoryName ORDER BY time LIKE :time DESC""")
    fun findByUserNameDESC(categoryName: String, time: Long) : List<User>
}