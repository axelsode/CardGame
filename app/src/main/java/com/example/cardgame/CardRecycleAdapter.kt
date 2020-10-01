package com.example.cardgame

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_card_item.view.*

class CardRecycleAdapter(private val context: Context, private val hands: List<Hand>): RecyclerView.Adapter<CardRecycleAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.activity_card_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        setCards(holder, position)

        //setActive(holder, position)

        if (HandManager.gameFinished){
            setWinner(holder, position)
        }else{
            holder.cardsItem.winLoseText.text = ""
            holder.cardsItem.setBackgroundColor(Color.parseColor("#00000000"))
        }

    }

    /*
    private fun setActive(holder: ViewHolder, position: Int){
        if(HandManager.activeHand == position){
            val itemViewActive = layoutInflater.inflate(R.layout.activity_card_item_active, parent, false)
                holder

        }
    }
*/






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
            "win" -> {holder.cardsItem.setBackgroundColor(Color.parseColor("#2196F3"))
                        holder.cardsItem.winLoseText.text = "Win"  }
            "lose" -> {holder.cardsItem.setBackgroundColor(Color.parseColor("#F44336"))
                        holder.cardsItem.winLoseText.text = "Lose"}
            "draw" -> {holder.cardsItem.setBackgroundColor(Color.parseColor("#00000000"))
                        holder.cardsItem.winLoseText.text = "Draw"}
        }

    }


    override fun getItemCount() = hands.size


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
         val cardsItem = itemView.findViewById<CardView>(R.id.cardView)!!
         val winLoseText = itemView.findViewById<TextView>(R.id.winLoseText)
        private val card1 = itemView.findViewById<ImageView>(R.id.card1)!!
        private val card2 = itemView.findViewById<ImageView>(R.id.card2)!!
        private val card3 = itemView.findViewById<ImageView>(R.id.card3)!!
        private val card4 = itemView.findViewById<ImageView>(R.id.card4)!!
        private val card5 = itemView.findViewById<ImageView>(R.id.card5)!!
        private val card6 = itemView.findViewById<ImageView>(R.id.card6)!!
        val imageList = listOf(card1, card2, card3, card4, card5, card6)

    }
}