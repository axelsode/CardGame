package com.example.cardgame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cardgame.AppDatabase.Companion.getInstance
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() , CoroutineScope {

    private lateinit var job : Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var nameText: EditText
    lateinit var passwordText: EditText
    lateinit var betText: EditText
    lateinit var login_button: Button
    lateinit var register_button: Button
    lateinit var back_button: Button
    lateinit var start_button: Button
    var loginRegister = "none"
    private lateinit var db : AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user")
            .fallbackToDestructiveMigration()
            .build()

        //test users
        addNewUser(User(0,"Axel","123456789", 90))
        //addNewUser(User(0,"David","111111111", 80))


        start_button = findViewById(R.id.button_start)
        nameText = findViewById(R.id.editTextTextPersonName)
        passwordText = findViewById(R.id.editPasswordText)
        betText = findViewById(R.id.cashEditText)
        login_button = findViewById(R.id.button_login)
        register_button = findViewById(R.id.button_new_player)
        back_button = findViewById(R.id.button_back)


        back_button.setOnClickListener {
            backFun()
        }

        login_button.setOnClickListener {
            login()
            //saveLogin()

        }

        register_button.setOnClickListener {
            register()
        }

        start_button.setOnClickListener {

            val name = nameText.text.toString()
            val password = passwordText.text.toString()
            val user = loadByUserName(name)

            if (loginRegister == "login"){
                launch{
                    val profile = user.await()
                    when {
                        profile.isEmpty() -> {
                            backFun()
                        }
                        profile[0].password == password -> {
                            startBlackJackActivity(name, profile[0].cash.toString())
                        }
                        else -> {
                            backFun()
                        }
                    }
                }
            }else if (loginRegister == "register"){
                launch{
                    val newCash = betText.text.toString().toInt()
                    val profile = user.await()
                    if (profile.isNotEmpty()){
                        Log.d("!!!", "User name exist")
                        Toast.makeText(this@MainActivity,getString(R.string.user_already_exists) , Toast.LENGTH_SHORT).show()
                    }else{
                        val newUser = User(0, name, password, newCash)
                        saveUser(newUser)
                        startBlackJackActivity(name, newCash.toString())

                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout -> {
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()

                true
            }
            R.id.action_highscore -> {
                Toast.makeText(this, "High Score", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addNewUser(user: User){
        launch(Dispatchers.IO) {
            db.userDao.insert(user)
        }
    }

    fun backFun(){
        login_button.visibility = View.VISIBLE
        register_button.visibility = View.VISIBLE
        start_button.visibility = View.INVISIBLE
        back_button.visibility = View.INVISIBLE
        nameText.visibility = View.INVISIBLE
        passwordText.visibility = View.INVISIBLE
        betText.visibility = View.INVISIBLE

    }

    private fun login(){

        back_button.visibility = View.VISIBLE
        start_button.visibility = View.VISIBLE
        nameText.visibility = View.VISIBLE
        passwordText.visibility = View.VISIBLE
        login_button.visibility = View.INVISIBLE
        register_button.visibility = View.INVISIBLE
        loginRegister = "login"





    }

    private fun register(){
        back_button.visibility = View.VISIBLE
        start_button.visibility = View.VISIBLE
        nameText.visibility = View.VISIBLE
        passwordText.visibility = View.VISIBLE
        betText.visibility = View.VISIBLE
        login_button.visibility = View.INVISIBLE
        register_button.visibility = View.INVISIBLE
        loginRegister = "register"

        val name = nameText.text.toString()
        val password = passwordText.text.toString()
    }

    private fun startBlackJackActivity(name :String, cash : String){
        //val name = nameText.text.toString()
        //val bet = betText.text.toString()
        val intent = Intent(this, BlackJackActivity::class.java)
        intent.putExtra("playerName", name)
        intent.putExtra("playerCash", cash)
        startActivity(intent)
    }

    fun saveUser(user : User){
        launch(Dispatchers.IO) {
            db.userDao.insert(user)
        }

    }
    fun loadByUserName(name : String) =
        async(Dispatchers.IO) {
            db.userDao.findByUserName(name)
        }


}