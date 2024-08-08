package com.zeddikus.legohelper.base

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zeddikus.legohelper.R
import com.zeddikus.legohelper.di.ErrorTypes
import com.zeddikus.legohelper.di.ScreenComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewBinding, VM : BaseViewModel> : Fragment() {

    abstract val viewModel: VM
    private var _binding: T? = null
    protected val binding get() = _binding!!

    open val viewModelFactory: ViewModelProvider.Factory by lazy {
        val component = diComponent()
        component.viewModelFactory
    }

    protected abstract fun diComponent(): ScreenComponent

    inline fun <reified VM : BaseViewModel> injectViewModel() = viewModels<VM>(
        factoryProducer = { viewModelFactory }
    )

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateFlow.collectLatest { navEvent ->
                navEvent?.navigate(this@BaseFragment)
            }
        }
        initViews()
        subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.setState(null)
    }

    protected open fun initViews() = Unit
    protected open fun subscribe() = Unit

    fun showDialog(
        messageText: String,
        consumer: (Int) -> Unit?
        ) {
        val dialogWindow = MaterialAlertDialogBuilder(requireActivity())
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                consumer.invoke(0)
            }
            .setPositiveButton(getString(R.string.complete)) { dialog, which ->
                consumer.invoke(1)
            }.setBackground(requireContext().getDrawable(R.drawable.btn_corners))
            .create()
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog, null)
        dialogView.findViewById<TextView>(R.id.tvTextTitle).text =
            getString(R.string.question)
        dialogView.findViewById<TextView>(R.id.tvTextMessage).text = messageText
        dialogWindow.setView(dialogView)
        dialogWindow.setOnShowListener {
            setValuesDialogWindow(dialogWindow)
        }

        dialogWindow.show()
    }

    private fun setValuesDialogWindow(dialogWindow: androidx.appcompat.app.AlertDialog) {
        val textColor = resources.getColor(R.color.WhiteDay_BlackNight, requireContext().theme)
        val backgroundColor =
            resources.getColor(R.color.BlackDay_WhiteNight, requireContext().theme)

        val btnPositive = dialogWindow.getButton(AlertDialog.BUTTON_POSITIVE)
        btnPositive.setTextColor(textColor)
        btnPositive.setBackgroundColor(backgroundColor)

        val btnCancel = dialogWindow.getButton(DialogInterface.BUTTON_NEUTRAL)
        btnCancel.setTextColor(textColor)
        btnCancel.setBackgroundColor(backgroundColor)

        dialogWindow.window?.decorView?.setBackgroundColor(backgroundColor)
        dialogWindow.window?.decorView?.setBackgroundResource(R.drawable.alert_background)//setBackgroundColor(backgroundColor)
    }

    fun showError(error:ErrorTypes) {
        when (error){
            ErrorTypes.NoData -> showToast(getString(R.string.e_nodata))
            ErrorTypes.NoNetwork -> showToast(getString(R.string.e_nonetwork))
            ErrorTypes.Unknown -> showToast(getString(R.string.e_unknown))
        }

    }

    fun showToast(messageText: String, toastType: Int = Toast.LENGTH_SHORT ){
        Toast.makeText(context,messageText,toastType).show()
    }

}
