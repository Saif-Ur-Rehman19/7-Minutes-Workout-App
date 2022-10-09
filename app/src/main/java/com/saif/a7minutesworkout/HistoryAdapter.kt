package com.saif.a7minutesworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saif.a7minutesworkout.databinding.ItemHistoryRowItemBinding

class HistoryAdapter(val items : ArrayList<String>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {



    class ViewHolder(binding : ItemHistoryRowItemBinding) : RecyclerView.ViewHolder(binding.root){

        val llHistoryItem = binding.llHistoryItem
        val tvItem = binding.tvItem
        val tvPosition = binding.tvPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowItemBinding.inflate(LayoutInflater.from(parent.context)
            ,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Every single row would be handled in this method
        val date : String = items.get(position)
        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = date
    }

    override fun getItemCount(): Int {
        return items.size
    }
}