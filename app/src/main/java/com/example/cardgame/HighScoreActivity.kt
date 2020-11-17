package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.textclassifier.TextLinks
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class HighScoreActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job : Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var db : AppDatabase
    lateinit var testText : TextView
    lateinit var recyclerView: RecyclerView
    lateinit var highScoreList : MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        recyclerView = findViewById(R.id.highScoreHolder)
        recyclerView.layoutManager = LinearLayoutManager(this)

        job = Job()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user")
            .fallbackToDestructiveMigration()
            .build()

        testText = findViewById(R.id.testText)
        highScoreList = mutableListOf<User>()
        val users = loadAll()

        recyclerView.adapter = HighScoreRecycleAdapter(this@HighScoreActivity, highScoreList)
        val adapter = recyclerView.adapter

        launch{
            var data = users.await()
            var tmp = ""
            var i = 0
            for (elm in data){
                highScoreList.add(elm)
                i++
                tmp += "\n :$i"
                tmp += "\n" + elm.id
                tmp += "\n" + elm.name
                tmp += "\n" + elm.cash
                tmp += "\n" + elm.password
                tmp += "\n" + elm.time
            }
            testText.text = tmp
            adapter?.notifyDataSetChanged()

        }
    }
    fun loadAll() =
        async(Dispatchers.IO) {
            db.userDao.getAll()
        }
}