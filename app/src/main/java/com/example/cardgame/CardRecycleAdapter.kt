package com.example.cardgame

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CardRecycleAdapter(private val context: Context, private val hands: List<Hand>): RecyclerView.Adapter<CardRecycleAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.activity_card_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardsItem.setBackgroundColor(Color.parseColor("#00000000"))
        setCards(holder, position)



        if (HandManager.gameFinished){
            setWinner(holder, position)
        }

    }

    private fun setActive(holder: ViewHolder){
        for (i in 0..5){
            holder.imageList[i]
            holder.imageList[i]
        }





    }

    private fun setCards(holder: ViewHolder, position: Int){
        val cardList = hands[position].cards
        for (i in 0..5){
            if (i < cardList.size){
                holder.imageList[i].setImageResource(cardList[i].getImageId(context))
                if (cardList[i].isVisible){
                    holder.imageList[i].visibility = View.VISIBLE
                }else{
                    holder.imageList[i].visibility = View.INVISIBLE
                }
            }else{
                holder.imageList[i].setImageResource(R.drawable.red_back)
                holder.imageList[i].visibility = View.INVISIBLE
            }
        }
    }

    private fun setWinner(holder: ViewHolder, position: Int){
        val player = HandManager.hands[position].valueAtPlayerHand
        val dealer = HandManager.valueAtDealerHand
        val winner : String
        winner = when {
            player > 21 -> {
                "lose"
            }
            dealer > 21 -> {
                "win"
            }
            player == 21 -> {
                "win"
            }
            player > dealer -> {
                "win"
            }
            player < dealer -> {
                "lose"
            }
            else -> {
                "draw"
            }
        }

        when(winner){
            "win" -> holder.cardsItem.setBackgroundColor(Color.parseColor("#2196F3"))
            "lose" -> holder.cardsItem.setBackgroundColor(Color.parseColor("#F44336"))
            "draw" -> holder.cardsItem.setBackgroundColor(Color.parseColor("FF9F12"))
        }

    }


    override fun getItemCount() = hands.size


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val cardsItem = itemView.findViewById<CardView>(R.id.cardView)!!
        private val card1 = itemView.findViewById<ImageView>(R.id.card1)!!
        private val card2 = itemView.findViewById<ImageView>(R.id.card2)!!
        private val card3 = itemView.findViewById<ImageView>(R.id.card3)!!
        private val card4 = itemView.findViewById<ImageView>(R.id.card4)!!
        private val card5 = itemView.findViewById<ImageView>(R.id.card5)!!
        private val card6 = itemView.findViewById<ImageView>(R.id.card6)!!
        val imageList = listOf(card1, card2, card3, card4, card5, card6)

    }
}