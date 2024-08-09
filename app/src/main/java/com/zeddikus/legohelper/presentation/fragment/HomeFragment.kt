package com.zeddikus.legohelper.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeddikus.legohelper.base.BaseFragment
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.databinding.FragmentHomeBinding
import com.zeddikus.legohelper.di.AppComponentHolder
import com.zeddikus.legohelper.di.ScreenComponent
import com.zeddikus.legohelper.di.featurecomponents.DaggerHomeComponent
import com.zeddikus.legohelper.domain.models.SetsState
import com.zeddikus.legohelper.presentation.setsadapter.SetsAdapter
import com.zeddikus.legohelper.presentation.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {

    override val viewModel by injectViewModel<HomeViewModel>()
    override fun diComponent(): ScreenComponent {
        val appComponent = AppComponentHolder.getComponent()
        return DaggerHomeComponent.builder()
            .appComponent(appComponent)
            .build()
    }
    private val setsAdapter: SetsAdapter by lazy {
        SetsAdapter { constructorSet ->
            val bundle = Bundle().apply {
                putInt(ConstructorSetFragment.CONSTRUCTOR_SET_ID, constructorSet.id)
            }
            viewModel.navigateTo(
                HomeFragmentDirections.actionHomeFragmentToSetFragment().actionId,
                bundle
            )
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSets.adapter = setsAdapter
        binding.rvSets.layoutManager = LinearLayoutManager(context)
        viewModel.updateSetList()

        setFragmentResultListener(ON_BACK_AFTER_COLLECT) { _: String, _: Bundle ->
            viewModel.updateSetList()
        }

        binding.btnAddNewSet.setOnClickListener {
            viewModel.navigateTo(HomeFragmentDirections.actionHomeFragmentToSetFragment())
        }

        binding.buttonTestCrash.setOnClickListener {
            throw RuntimeException("Test Crash")
        }

        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }
    }

    private fun render(state: SetsState){
        when (state) {
            is SetsState.Data -> {
                setsAdapter.submitList(state.constructorSetList)
            }
            SetsState.Error -> {
                Toast.makeText(requireContext(),"Ошибка",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateSetList()
    }

    companion object {
        const val ON_BACK_AFTER_COLLECT = "on_back_after_collect"
    }
}
