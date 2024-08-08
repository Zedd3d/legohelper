package com.zeddikus.legohelper.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.zeddikus.legohelper.R
import com.zeddikus.legohelper.base.BaseFragment
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.databinding.FragmentLinesscreenBinding
import com.zeddikus.legohelper.di.AppComponentHolder
import com.zeddikus.legohelper.di.ScreenComponent
import com.zeddikus.legohelper.di.featurecomponents.DaggerLinesScreenComponent
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.getGroupSorts
import com.zeddikus.legohelper.presentation.fragment.ConstructorSetFragment.Companion.CONSTRUCTOR_SET_ID
import com.zeddikus.legohelper.presentation.linesadapter.LinesAdapter
import com.zeddikus.legohelper.presentation.viewmodel.LinesScreenViewModel
import com.zeddikus.legohelper.utils.setOnDebouncedClickListener

class LinesScreenFragment : BaseFragment<FragmentLinesscreenBinding, BaseViewModel>() {

    private lateinit var layoutManager: GridLayoutManager

    override val viewModel by injectViewModel<LinesScreenViewModel>()
    override fun diComponent(): ScreenComponent {
        val appComponent = AppComponentHolder.getComponent()
        return DaggerLinesScreenComponent.builder()
            .appComponent(appComponent)
            .build()
    }


    private val linesAdapter: LinesAdapter by lazy {
        LinesAdapter (
            lifecycleScope,
            onLineClick = {constructorSetLine ->
                viewModel.plusOne(constructorSetLine)
            },
            onShowDetailsClick = { constructorSetLine ->
                val bundle = Bundle().apply {
                    putInt(CONSTRUCTOR_SET_ID, constructorSetLine.lineId)
                }
                viewModel.navigateTo(
                    LinesScreenFragmentDirections.actionLinesScreenFragmentToLinesDetailsFragment().actionId,
                    bundle
                )
            })
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLinesscreenBinding {
        return FragmentLinesscreenBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvLines.layoutManager = layoutManager
        binding.rvLines.adapter = linesAdapter

        val setId = arguments?.getInt(CONSTRUCTOR_SET_ID)?:0
        viewModel.loadLines(setId)

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

        binding.btnUp.setOnDebouncedClickListener(lifecycleScope){
            viewModel.changeSpanCount(1)
        }

        binding.btnDown.setOnDebouncedClickListener(lifecycleScope){
            viewModel.changeSpanCount(-1)
        }

        binding.btnSelectSort.setOnDebouncedClickListener(lifecycleScope){
            showPopUpMenu(it)
        }

        binding.btnHide.setOnDebouncedClickListener(lifecycleScope){
            viewModel.switchHide()
        }
    }

    private fun showPopUpMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)

        var k = -1
        var o = -1
        val listSorts = getGroupSorts(requireContext())
        for (sort in listSorts) {
            k++
            o++
            popupMenu.menu.add(0, k, o, sort.description)
        }

        // Устанавливаем слушатель нажатий на элементы меню
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId>0) {
                viewModel.sortList(listSorts[menuItem.itemId])
                true
            } else {false}

        }

        // Показываем PopupMenu
        popupMenu.show()
    }

    private fun changeGridSpanCount(spanCount: Int) {
        if (layoutManager.spanCount != spanCount) {
            layoutManager.spanCount = spanCount
            linesAdapter.notifyItemRangeChanged(0, linesAdapter.itemCount) // Обновить адаптер
        }
    }

    private fun onBackPressed() {
        viewModel.navigateUp()
    }

    private fun render(state: LinesState){
        when (state) {
            is LinesState.Data -> {
                changeGridSpanCount(state.spanCount.count)
                binding.btnSelectSort.text = state.sort.description
                linesAdapter.submitList(state.list)

                if (state.hideCollected) {
                    binding.btnHide.text = getText(R.string.btnHideOn)
                } else {
                    binding.btnHide.text = getText(R.string.btnHideOff)
                }
            }
            is LinesState.Error -> showError(state.errorType)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val ON_BACK_AFTER_COLLECT = "on_back_after_collect"
    }
}
