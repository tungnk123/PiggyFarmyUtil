package com.example.piggyfarmyutil

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ThongTinAdapter(private val context : Context, private var data : List<Pair<String, Int>>) :
    RecyclerView.Adapter<ThongTinAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvTitle : TextView = itemView.findViewById(R.id.tv_itemTitle)
        val tvValue : TextView = itemView.findViewById(R.id.tv_itemValue)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rcv, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : Pair<String, Int> = data[position]
        holder.tvTitle.text = item.first
        holder.tvValue.text = item.second.toString()
    }

    fun updateData(newData: List<Pair<String, Int>>) {
        data = newData
        notifyDataSetChanged()
    }
}