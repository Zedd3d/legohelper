package com.zeddikus.legohelper.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.zeddikus.legohelper.base.BaseFragment
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.databinding.FragmentConstructorsetBinding
import com.zeddikus.legohelper.di.AppComponentHolder
import com.zeddikus.legohelper.di.ScreenComponent
import com.zeddikus.legohelper.di.featurecomponents.DaggerConstructorSetComponent
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.presentation.viewmodel.SetViewModel

class ConstructorSetFragment : BaseFragment<FragmentConstructorsetBinding, BaseViewModel>()  {
    override val viewModel by injectViewModel<SetViewModel>()
    override fun diComponent(): ScreenComponent {
        val appComponent = AppComponentHolder.getComponent()
        return DaggerConstructorSetComponent.builder()
            .appComponent(appComponent)
            .build()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentConstructorsetBinding {
        return FragmentConstructorsetBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val setId = arguments?.getInt(CONSTRUCTOR_SET_ID)?:0
        viewModel.updateSetData(setId)

        binding.btnLoadFromBrickLink.setOnClickListener {
            if (binding.editId.editText.text?.isNotEmpty()?:false
                && binding.editName.editText.text?.isNotEmpty()?:false) {
                binding.editId.tilEdit.isErrorEnabled = false
                binding.editName.tilEdit.isErrorEnabled = false
                viewModel.loadLines(binding.editId.editText.text.toString())
            } else {
                binding.editId.tilEdit.isErrorEnabled = binding.editId.editText.text?.isNotEmpty()?:false == false
                binding.editId.tilEdit.error = "Необходимо заполнить"
                binding.editName.tilEdit.isErrorEnabled = binding.editName.editText.text?.isNotEmpty()?:false == false
                binding.editName.tilEdit.error = "Необходимо заполнить"
            }
        }

        binding.btnGoToCollect.setOnClickListener {
            //to-do
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

        setTexts(ConstructorSet(0,"",""))

        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }
    }

    private fun onBackPressed(){
        if (binding.editId.editText.text?.isNotEmpty()?:false
            && binding.editName.editText.text?.isNotEmpty()?:false) {
            viewModel.saveSetData(
                binding.editId.editText.text.toString(),
                binding.editName.editText.text.toString()
            )
        }
        viewModel.navigateUp()
    }

    private fun render(state: SetState){
        when (state){
            is SetState.Data -> {
                setTexts(state.set)
            }
            SetState.Empty -> {
                setTexts(ConstructorSet(0,"",""))
            }
            SetState.Error -> Toast.makeText(requireContext(),"Ошибка",Toast.LENGTH_SHORT).show()
        }

    }

    private fun setTexts(constructorSet: ConstructorSet){
        binding.editId.tilEdit.hint = "Номер набора"
        binding.editName.tilEdit.hint = "Наименование набора"

        binding.editId.editText.setText(constructorSet.setIdExt)
        binding.editName.editText.setText(constructorSet.name)
    }

    companion object{
        const val CONSTRUCTOR_SET_ID = "constructor_set_id"
    }
}