package com.example.cardgame

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HighScoreRecycleAdapter(val context: Context, val userlist: List<UserScore> ) :
   RecyclerView.Adapter<HighScoreRecycleAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.activity_highscore_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HighScoreRecycleAdapter.ViewHolder, position: Int) {
        val user = userlist[position]
        holder.higScoreNameItem.text = user.name.toString()
        holder.higScoreScoreItem.text = user.cash.toString()
        holder.higScoreTimeItem.text =  user.getDateTime(user.time)
        holder.higScoreChangeItem.text = user.change.toString()
        if (user.change > 0){
            holder.higScoreChangeItem.setTextColor(Color.GREEN)
        }else{
            holder.higScoreChangeItem.setTextColor(Color.RED)
        }


    }

    override fun getItemCount() = userlist.size



    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val higScoreNameItem = itemView.findViewById<TextView>(R.id.textViewName)!!
        val higScoreScoreItem = itemView.findViewById<TextView>(R.id.textViewScore)!!
        val higScoreTimeItem = itemView.findViewById<TextView>(R.id.textViewTime)!!
        val higScoreChangeItem = itemView.findViewById<TextView>(R.id.textViewChange)!!
    }


}