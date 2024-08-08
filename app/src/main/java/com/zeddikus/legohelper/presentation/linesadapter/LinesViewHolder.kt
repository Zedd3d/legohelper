package com.zeddikus.legohelper.presentation.linesadapter

import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zeddikus.legohelper.R
import com.zeddikus.legohelper.databinding.LineCollectItemBinding
import com.zeddikus.legohelper.di.ApiConstants
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.utils.setOnDebouncedClickListener

class LinesViewHolder(
    private val lifeCycleScope: LifecycleCoroutineScope,
    private val binding: LineCollectItemBinding,
    private val onLineClickListener: (ConstructorSetLine) -> Unit,
    private val onShowDetailsClickListener: (ConstructorSetLine) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(constructorSetLine: ConstructorSetLine) {
        val imgUrl = ApiConstants.IMG_URL + constructorSetLine.part.imgUrl
        Glide.with(binding.partCover)
            .load(imgUrl)
            .placeholder(R.drawable.lego_placeholder)
            .fitCenter()
            .into(binding.partCover)

        binding.apply {
            setTextCollectProcess(constructorSetLine)
        }

        binding.btnOpenDetails.setOnDebouncedClickListener(lifeCycleScope){
            onShowDetailsClickListener.invoke(constructorSetLine)
        }

        binding.partCover.setOnDebouncedClickListener(lifeCycleScope,50){
            if (constructorSetLine.countFound + 1 <= constructorSetLine.count){
                constructorSetLine.countFound ++
                setTextCollectProcess(constructorSetLine)
                onLineClickListener(constructorSetLine)
            } else {
                Toast.makeText(binding.root.context,binding.root.resources.getText(R.string.msg_noneedcollect), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setTextCollectProcess(constructorSetLine: ConstructorSetLine){
        //tvDescr.text = "" + binding.root.resources.getText(R.string.collect_status_line) + "    ${constructorSetLine.countFound} / ${constructorSetLine.count}"
        binding.tvDescr.text = "${constructorSetLine.countFound} / ${constructorSetLine.count}"
    }
}
