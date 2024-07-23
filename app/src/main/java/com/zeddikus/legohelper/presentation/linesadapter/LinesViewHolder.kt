package com.zeddikus.legohelper.presentation.linesadapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.zeddikus.legohelper.R
import com.zeddikus.legohelper.databinding.LineCollectItemBinding
import com.zeddikus.legohelper.databinding.SetListItemBinding
import com.zeddikus.legohelper.di.ApiConstants
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine

class LinesViewHolder(private val binding: LineCollectItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(constructorSetLine: ConstructorSetLine) {
        Glide.with(binding.partCover)
            .load(ApiConstants.IMG_URL + constructorSetLine.part.imgUrl)
            .placeholder(R.drawable.lego_placeholder)
            .fitCenter()
            .into(binding.partCover)

        binding.apply {
            tvDescr.text = "" + binding.root.resources.getText(R.string.collect_status_line) + "    ${constructorSetLine.countFound} / ${constructorSetLine.count}"
        }
    }
}
