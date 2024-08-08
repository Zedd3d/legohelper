package com.zeddikus.legohelper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.di.ErrorTypes
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.SettingsInteractor
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.models.Settings
import com.zeddikus.legohelper.domain.network.NetworkInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LinesScreenViewModel @Inject constructor(
    private val setsInteractor: SetsInteractor,
    private val settingsInteractor: SettingsInteractor
) :
    BaseViewModel() {

    private var currentSetId:Int = 0
    private var hideCollected: Boolean = false

    private val stateLiveData = MutableLiveData<LinesState>()
    fun observeState(): LiveData<LinesState> = stateLiveData

    fun loadLines(setId: Int) {
        currentSetId = setId
        viewModelScope.launch(Dispatchers.IO) {
            //runCatching {
                setsInteractor.loadLines(setId,hideCollected).collect {

                    val result = if (it is LinesState.Data){
                        it.copy(hideCollected = hideCollected)
                    } else { it }

                    stateLiveData.postValue(result)
                }
//            }.onFailure {
//                stateLiveData.postValue(LinesState.Error(ErrorTypes.Unknown))
//            }
        }
    }

    fun switchHide(){
        hideCollected = !hideCollected
        updateLines()
    }

    fun updateLines(){
        loadLines(currentSetId)
    }

    fun changeSpanCount(change: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val settings = settingsInteractor.loadColumnCount()
                when (settings) {
                    is Settings.Columns -> {
                       var columns = settings.count
                       columns = columns + change

                       if (columns > 8) {columns = 8}
                       if (columns < 2) {columns = 2} else {}
                       settingsInteractor.saveColumn(Settings.Columns(columns))
                    }

                    else -> null
                }
                updateLines()
            }.onFailure {
                //
            }
        }
    }

    fun sortList(sort: Settings.Sort){
        viewModelScope.launch(Dispatchers.IO) {
            settingsInteractor.saveSort(sort)
            updateLines()
        }
    }

    fun plusOne(constructorSetLine: ConstructorSetLine) {

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setsInteractor.saveLine(constructorSetLine).collect {
                }
                if (constructorSetLine.count == constructorSetLine.countFound){
                    updateLines()
                }
            }.onFailure {
                stateLiveData.postValue(LinesState.Error(ErrorTypes.Unknown))
            }
        }
    }

}
