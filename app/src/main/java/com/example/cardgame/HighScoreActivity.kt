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
import java.lang.NumberFormatException
import kotlin.coroutines.CoroutineContext


class HighScoreActivity : AppCompatActivity(), CoroutineScope {

    private val ON_WEEK =  604800000
    private lateinit var job : Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var db : AppDatabase
    lateinit var testText : TextView
    lateinit var recyclerView: RecyclerView
    lateinit var highScoreList : MutableList<UserScore>
    //lateinit var highScoreListFinal : MutableList<UserScore>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        recyclerView = findViewById(R.id.highScoreHolder)
        recyclerView.layoutManager = LinearLayoutManager(this)

        job = Job()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user")
            .fallbackToDestructiveMigration()
            .build()


        highScoreList = mutableListOf<UserScore>()
        recyclerView.adapter = HighScoreRecycleAdapter(this@HighScoreActivity, highScoreList)
        val adapter = recyclerView.adapter
        val users = loadAll()


        launch{
            var data = users.await()

            var users = mutableListOf<String>()
            for (elm in data){
                if (!users.contains(elm.name)) {
                    users.add(elm.name)
                }
            }
            for(id in users){
                val thisUser = data.filterByName(id)
                val last = thisUser.sortByTime()[0]
                val oldList: List<User> =  try {
                    thisUser.filterByTimeBefore(System.currentTimeMillis() - ON_WEEK)
                } catch (e: NumberFormatException) {
                    emptyList()
                }

                var change: Double
                if (!oldList.isEmpty()){
                    val old = oldList.sortByTime()[0]
                   change = 100 * (last.cash - old.cash).toDouble() / old.cash.toDouble()
                }else{
                    val tempLast = thisUser.filterByTimeAfter(System.currentTimeMillis() - ON_WEEK).sortByTimeLast()[0]
                    change = 100 * (last.cash - tempLast.cash).toDouble() / tempLast.cash.toDouble()
                }
                val user = UserScore(id, last.cash, last.time, change)
                highScoreList.add(user)
            }
            val tmpList = highScoreList.sortByChange() as MutableList<UserScore>
            highScoreList.clear()
            highScoreList.addAll(tmpList)

            adapter?.notifyDataSetChanged()

        }
    }
    fun loadAll() =
        async(Dispatchers.IO) {
            db.userDao.getAll()
        }
    fun lastActivity(name: String, time: Long){
        launch(Dispatchers.IO) {
            db.userDao.findByUserNameFIRST(name, time)
        }
    }

    fun List<UserScore>.sortByChange() = this.sortedByDescending { it.change }

    fun List<User>.filterByName(name: String) = this.filter { it.name == name }

    fun List<User>.sortByTime() = this.sortedByDescending { it.time }

    fun List<User>.filterByTimeAfter(time: Long) = this.filter { it.time > time }

    fun List<User>.filterByTimeBefore(time: Long) = this.filter { it.time < time }

    fun List<User>.sortByTimeLast() = this.sortedBy { it.time }
}