package com.example.cardgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HighScoreRecycleAdapter(val context: Context, val userlist: List<User> ) :
   RecyclerView.Adapter<HighScoreRecycleAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.activity_highscore_item, parent, false)
        TODO("Not yet implemented")
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HighScoreRecycleAdapter.ViewHolder, position: Int) {
        val user = userlist[position]
        holder.higScoreNameItem.text = user.name
        holder.higScoreScoreItem.text = user.cash.toString()
        holder.higScoreTimeItem.text = user.time.toString()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val higScoreNameItem = itemView.findViewById<TextView>(R.id.textViewName)!!
        val higScoreScoreItem = itemView.findViewById<TextView>(R.id.textViewScore)!!
        val higScoreTimeItem = itemView.findViewById<TextView>(R.id.textViewTime)!!
    }


}