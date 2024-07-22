package com.zeddikus.legohelper.presentation.setsadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeddikus.legohelper.databinding.SetListItemBinding
import com.zeddikus.legohelper.domain.models.ConstructorSet

class SetsAdapter(
    private val onNewsClick: (ConstructorSet) -> Unit
) : ListAdapter<ConstructorSet, SetsViewHolder>(SetsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetsViewHolder {
        val binding = SetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = SetsViewHolder(binding)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onNewsClick.invoke(getItem(position))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SetsDiffCallback : DiffUtil.ItemCallback<ConstructorSet>() {
    override fun areItemsTheSame(oldItem: ConstructorSet, newItem: ConstructorSet): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ConstructorSet, newItem: ConstructorSet): Boolean {
        return oldItem == newItem
    }
}