package com.example.cardgame

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HighScoreRecycleAdapter.ViewHolder, position: Int) {
        val user = userlist[position]
        val change = "%.2f".format(user.change)
        holder.higScoreNameItem.text = user.name.toString()
        holder.higScoreScoreItem.text = user.cash.toString()
        holder.higScoreTimeItem.text = "Last played " + user.getDateTime(user.time)
        holder.higScoreChangeItem.text = "Change last week:  " + change +" %" //user.change.toString()
        if (user.change > 0){
            holder.higScoreChangeItem.setTextColor(Color.GREEN)
            holder.higScoreNameItem.setTextColor(Color.GREEN)
            holder.higScoreScoreItem.setTextColor(Color.GREEN)
            holder.higScoreTimeItem.setTextColor(Color.GREEN)
        }
        if(user.change < 0){
            holder.higScoreChangeItem.setTextColor(Color.RED)
            holder.higScoreNameItem.setTextColor(Color.RED)
            holder.higScoreScoreItem.setTextColor(Color.RED)
            holder.higScoreTimeItem.setTextColor(Color.RED)

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