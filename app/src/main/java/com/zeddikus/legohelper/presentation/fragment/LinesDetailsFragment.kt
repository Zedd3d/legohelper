package com.zeddikus.legohelper.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zeddikus.legohelper.R
import com.zeddikus.legohelper.base.BaseFragment
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.databinding.FragmentLinedetailsBinding
import com.zeddikus.legohelper.databinding.FragmentLinesscreenBinding
import com.zeddikus.legohelper.di.ApiConstants
import com.zeddikus.legohelper.di.AppComponentHolder
import com.zeddikus.legohelper.di.ScreenComponent
import com.zeddikus.legohelper.di.featurecomponents.DaggerHomeComponent
import com.zeddikus.legohelper.di.featurecomponents.DaggerLineDetailsComponent
import com.zeddikus.legohelper.domain.models.LineSetState
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetsState
import com.zeddikus.legohelper.presentation.fragment.ConstructorSetFragment.Companion.CONSTRUCTOR_SET_ID
import com.zeddikus.legohelper.presentation.linesadapter.LinesAdapter
import com.zeddikus.legohelper.presentation.setsadapter.SetsAdapter
import com.zeddikus.legohelper.presentation.viewmodel.LineDetailsViewModel
import com.zeddikus.legohelper.presentation.viewmodel.LinesScreenViewModel
import com.zeddikus.legohelper.utils.setOnDebouncedClickListener

class LinesDetailsFragment : BaseFragment<FragmentLinedetailsBinding, BaseViewModel>() {

    override val viewModel by injectViewModel<LineDetailsViewModel>()
    override fun diComponent(): ScreenComponent {
        val appComponent = AppComponentHolder.getComponent()
        return DaggerLineDetailsComponent.builder()
            .appComponent(appComponent)
            .build()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLinedetailsBinding {
        return FragmentLinedetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineId = arguments?.getInt(CONSTRUCTOR_SET_ID)?:0
        viewModel.loadLineData(lineId)

        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }
        setListeners()
    }

    private fun setListeners(){
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        binding.llNumericBlock.setOnDebouncedClickListener(lifecycleScope){
            val a=1
        }

        // Перебор всех дочерних элементов в LinearLayout
        for (i in 0 until binding.llNumericBlock.childCount) {
            val view: View = binding.llNumericBlock.getChildAt(i)
            if (view is LinearLayout) {
                for (j in 0 until view.childCount) {
                    val viewB: View = view.getChildAt(j)
                    if (viewB is Button) {
                        // Установка одинакового слушателя нажатия на кнопку
                        viewB.setOnClickListener { button ->
                            handleButtonClick(button as Button)
                        }
                    }
                }
            }
        }

        binding.btnPlusOne.setOnDebouncedClickListener(lifecycleScope){
            viewModel.changeCountFromButton(1)
        }
        binding.btnMinusOne.setOnDebouncedClickListener(lifecycleScope){
            viewModel.changeCountFromButton(-1)
        }
    }

    private fun handleButtonClick(button: Button) {
        viewModel.setCountFromButton(button.text.toString().toInt())
    }

    private fun onBackPressed() {
        viewModel.navigateUp()
    }

    private fun render(state: LineSetState){
        when (state) {
            is LineSetState.Data -> {
                val line = state.line
                binding.apply {
                    tvName.text = "${getText(R.string.part_name)} ${line.part.name}"
                    tvColorCode.text = "${getText(R.string.part_id)} ${line.part.id}"
                    tvCount.text = line.count.toString()
                    tvCountFind.text = line.countFound.toString()
                    Glide.with(binding.partCover)
                        .load(ApiConstants.IMG_URL + line.part.imgUrl)
                        .placeholder(R.drawable.lego_placeholder)
                        .fitCenter()
                        .into(binding.partCover)
                }
            }
            LineSetState.Error -> {
                Toast.makeText(requireContext(),"Error render Lines State",Toast.LENGTH_LONG).show()
            }
        }
    }

}
