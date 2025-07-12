package com.example.myapplication.Linguin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class LinguinShoppingRecyclerAdapter(
    private val itemList: ArrayList<LinguinShopData>,
    private val onItemClick: (LinguinShopData) -> Unit
) : RecyclerView.Adapter<LinguinShoppingRecyclerAdapter.LinguinViewHolder>() {

    inner class LinguinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageIcon: ImageView = itemView.findViewById(R.id.itempic)
        val imageText: TextView = itemView.findViewById(R.id.itemtext)

        fun bind(item: LinguinShopData) {
            imageText.text = item.Itemtxt
            imageIcon.setImageResource(item.ItemIcon)

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinguinViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.linguinrecyclerdefault, parent, false)
        return LinguinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LinguinViewHolder, position: Int) {
        val cItem = itemList[position]
        holder.bind(cItem)
    }

    override fun getItemCount(): Int = itemList.size
}
