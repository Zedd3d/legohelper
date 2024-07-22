package com.zeddikus.legohelper.presentation.setsadapter

import androidx.recyclerview.widget.RecyclerView
import com.zeddikus.legohelper.R
import com.zeddikus.legohelper.databinding.SetListItemBinding
import com.zeddikus.legohelper.domain.models.ConstructorSet

class SetsViewHolder(private val binding: SetListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val radius = binding.root.resources.getDimensionPixelSize(R.dimen.corner_radius_8)

    fun bind(constructorSet: ConstructorSet) {
        binding.apply {
            setDescription.setText("(${constructorSet.setIdExt}) - ${constructorSet.name}")
        }
    }
}
