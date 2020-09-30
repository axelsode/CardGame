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

        setCards(holder, position)

        setActive(holder, position)

        if (HandManager.gameFinished){
            setWin(holder, position)
        }else{
            holder.cardsItem.setCardBackgroundColor(R.drawable.button_default)
        }

    }

    private fun setActive(holder: ViewHolder, position: Int){


        if (hands[position].activ){
            holder.imageList[2].setImageResource(R.drawable.red_back)
            //holder.imageList[0].width
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

    private fun setWin(holder: ViewHolder, position: Int){
        val player = HandManager.hands[position].valueAtPlayerHand
        val dealer = HandManager.valueAtDealerHand
        val winner : Int
        winner = when {
            player > 21 -> {
                2
            }
            dealer > 21 -> {
                1
            }
            player == 21 -> {
                1
            }
            player > dealer -> {
                1
            }
            player < dealer -> {
                2
            }
            else -> {
                3
            }
        }

        when(winner){
            1 -> holder.cardsItem.setBackgroundColor(Color.parseColor("#2196F3"))
            2 -> holder.cardsItem.setCardBackgroundColor(Color.parseColor("#F44336"))
            3 -> holder.cardsItem.setCardBackgroundColor(Color.parseColor("FF9F12"))
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