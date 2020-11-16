package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class HighScoreActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job : Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var db : AppDatabase
    lateinit var testText : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        job = Job()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user")
            .fallbackToDestructiveMigration()
            .build()

        testText = findViewById(R.id.testText)

        val users = loadAll()
        launch{
            val data = users.await()
            var tmp = ""
            var i = 0
            for (elm in data){
                i++
                tmp += "\n :$i"
                tmp += "\n" + elm.id
                tmp += "\n" + elm.name
                tmp += "\n" + elm.cash
                tmp += "\n" + elm.password
                tmp += "\n" + elm.time
            }
            testText.text = tmp

        }
    }
    fun loadAll() =
        async(Dispatchers.IO) {
            db.userDao.getAll()
        }
}