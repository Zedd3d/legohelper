package com.zeddikus.legohelper.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeddikus.legohelper.base.BaseFragment
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.databinding.FragmentLinesscreenBinding
import com.zeddikus.legohelper.di.AppComponentHolder
import com.zeddikus.legohelper.di.ScreenComponent
import com.zeddikus.legohelper.di.featurecomponents.DaggerHomeComponent
import com.zeddikus.legohelper.di.featurecomponents.DaggerLinesScreenComponent
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetsState
import com.zeddikus.legohelper.presentation.fragment.ConstructorSetFragment.Companion.CONSTRUCTOR_SET_ID
import com.zeddikus.legohelper.presentation.linesadapter.LinesAdapter
import com.zeddikus.legohelper.presentation.setsadapter.SetsAdapter
import com.zeddikus.legohelper.presentation.viewmodel.LinesScreenViewModel

class LinesScreenFragment : BaseFragment<FragmentLinesscreenBinding, BaseViewModel>() {

    override val viewModel by injectViewModel<LinesScreenViewModel>()
    override fun diComponent(): ScreenComponent {
        val appComponent = AppComponentHolder.getComponent()
        return DaggerLinesScreenComponent.builder()
            .appComponent(appComponent)
            .build()
    }
    private val linesAdapter: LinesAdapter by lazy {
        LinesAdapter { constructorSetLine ->
            val bundle = Bundle().apply {
                putInt(ConstructorSetFragment.CONSTRUCTOR_SET_ID, constructorSetLine.lineId)
            }
//            viewModel.navigateTo(
//                HomeFragmentDirections.actionHomeFragmentToSetFragment().actionId,
//                bundle
//            )
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLinesscreenBinding {
        return FragmentLinesscreenBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLines.adapter = linesAdapter
        binding.rvLines.layoutManager = GridLayoutManager(requireContext(), 5)

        val setId = arguments?.getInt(CONSTRUCTOR_SET_ID)?:0
        viewModel.loadLines(setId)

        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

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
    }

    private fun onBackPressed() {
        viewModel.navigateUp()
    }

    private fun render(state: LinesState){
        when (state) {
            is LinesState.Data -> {
                linesAdapter.submitList(state.list)
            }
            LinesState.Error -> {
                Toast.makeText(requireContext(),"Error render Lines State",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val ON_BACK_AFTER_COLLECT = "on_back_after_collect"
    }
}
