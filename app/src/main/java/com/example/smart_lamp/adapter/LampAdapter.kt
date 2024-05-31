package com.example.smart_lamp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_lamp.databinding.ItemLampBinding
import com.example.smart_lamp.model.LampResponse

class LampAdapter(
    private val lampList: ArrayList<LampResponse>,
    private val mListener: onItemClickListener,
) :
    RecyclerView.Adapter<LampAdapter.ViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    //    class ViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
//        val lampView: TextView
//
//        init {
//            view.setOnClickListener {
//                listener.onItemClick(adapterPosition)
//            }
//            lampView = view.findViewById(R.id.tv_lamp_name)
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lamp, parent, false)
//        return ViewHolder(view, mListener)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val currentLamp = lampList[position]
//        holder.lampView.text = currentLamp.lampName
//    }
//
//    override fun getItemCount() = lampList.size
    class ViewHolder(private val binding: ItemLampBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lamp: LampResponse, listener: onItemClickListener) {
            binding.apply {
                tvLampName.text = lamp.lampName
                cvLamp.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position)
                    }
                }
                lampSwitch.addOnStatusChangedListener { isChecked ->
                    if (isChecked) {
                        Toast.makeText(
                            itemView.context,
                            "${lamp.lampName} is ON",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            itemView.context,
                            "${lamp.lampName} is OFF",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLampBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currLamp = lampList[position]
        holder.bind(currLamp, mListener)
    }

    override fun getItemCount() = lampList.size
}
