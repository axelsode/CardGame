package com.example.cardgame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cardgame.AppDatabase.Companion.getInstance
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

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
        val db = FirebaseFirestore.getInstance()

        db = AppDatabase.getInstance(this)


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

           if (betText.text.isNotBlank() && betText.text.toString().toInt() > 0 ){
               startBlackJackActivity()
           } else{
               Toast.makeText(this, getString(R.string.Fill_in_cash), Toast.LENGTH_SHORT).show()
           }
            //saveLogin()
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
    }

    private fun startBlackJackActivity(){
        val name = nameText.text.toString()
        val bet = betText.text.toString()
        val intent = Intent(this, BlackJackActivity::class.java)
        intent.putExtra("playerName", name)
        intent.putExtra("playerCash", bet)
        startActivity(intent)
    }

    /*private fun saveLogin(){
        val name = nameText.text.toString().trim()
        // om man vill ha ett varnings tecken
        if (name.isEmpty()){
            nameText.error = "Please enter a name"
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("Names")
        val userUid = ref.push().key

        val user = User(userUid, name, 0)

        ref.child(userUid.toString()).setValue(user).addOnCompleteListener {
            Toast.makeText(applicationContext, "User saved successfully", Toast.LENGTH_LONG).show()
        }
    }
     */

}