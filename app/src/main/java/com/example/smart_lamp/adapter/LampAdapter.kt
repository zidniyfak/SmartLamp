package com.example.smart_lamp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_lamp.R
import com.example.smart_lamp.databinding.ItemLampBinding
import com.example.smart_lamp.model.LampResponse

class LampAdapter(
    private var lampList: MutableList<LampResponse>,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<LampAdapter.LampViewHolder>() {

    inner class LampViewHolder(private val binding: ItemLampBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
            binding.lampSwitch.addOnStatusChangedListener { isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemSwitch(position, isChecked)
                    lampList[position].localStatus = if (isChecked) 1 else 0
                    binding.ivLamp.setImageResource(if (isChecked) R.drawable.ic_bulb_on else R.drawable.ic_bulb_off)
                }
            }
        }

        fun bind(lamp: LampResponse) {
            binding.apply {
                tvLampName.text = lamp.lampName
                lampSwitch.isChecked = lamp.localStatus == 1
                ivLamp.setImageResource(if (lamp.localStatus == 1) R.drawable.ic_bulb_on else R.drawable.ic_bulb_off)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemSwitch(position: Int, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LampViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLampBinding.inflate(inflater, parent, false)
        return LampViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LampViewHolder, position: Int) {
        val currentItem = lampList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = lampList.size
}
