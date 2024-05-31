package com.example.smart_lamp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_lamp.R

class LampAdapter(private val lampList:Array<String>):RecyclerView.Adapter<LampAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val lampView:TextView
        init {
            lampView=view.findViewById(R.id.tv_lamp)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_lamp,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lampView.text=lampList[position]
    }
    override fun getItemCount()=lampList.size


}