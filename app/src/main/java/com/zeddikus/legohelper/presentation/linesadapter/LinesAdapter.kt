package com.zeddikus.legohelper.presentation.linesadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeddikus.legohelper.databinding.LineCollectItemBinding
import com.zeddikus.legohelper.databinding.SetListItemBinding
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine

class LinesAdapter(
    private val lifecycleCoroutineScope: LifecycleCoroutineScope,
    private val onLineClick: (ConstructorSetLine) -> Unit,
    private val onShowDetailsClick: (ConstructorSetLine) -> Unit
) : ListAdapter<ConstructorSetLine, LinesViewHolder>(SetsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinesViewHolder {
        val binding = LineCollectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = LinesViewHolder(lifecycleCoroutineScope, binding,onLineClick, onShowDetailsClick)

        return holder
    }

    override fun onBindViewHolder(holder: LinesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SetsDiffCallback : DiffUtil.ItemCallback<ConstructorSetLine>() {
    override fun areItemsTheSame(oldItem: ConstructorSetLine, newItem: ConstructorSetLine): Boolean {
        return oldItem.lineId == newItem.lineId
    }

    override fun areContentsTheSame(oldItem: ConstructorSetLine, newItem: ConstructorSetLine): Boolean {
        return oldItem == newItem
    }
}