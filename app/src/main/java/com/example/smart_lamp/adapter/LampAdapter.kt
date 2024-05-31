package com.example.smart_lamp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_lamp.R
import com.example.smart_lamp.model.LampResponse

class LampAdapter(private val lampList: ArrayList<LampResponse>) :
    RecyclerView.Adapter<LampAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lampView: TextView

        init {
            lampView = view.findViewById(R.id.tv_lamp_name)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lamp, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLamp = lampList[position]
        holder.lampView.text = currentLamp.lampName
    }

    override fun getItemCount() = lampList.size
}
