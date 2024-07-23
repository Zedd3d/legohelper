package com.zeddikus.legohelper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.domain.models.LineSetState
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.network.NetworkInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LineDetailsViewModel @Inject constructor(
    private val setsInteractor: SetsInteractor,
) :
    BaseViewModel() {

    private val stateLiveData = MutableLiveData<LineSetState>()
    fun observeState(): LiveData<LineSetState> = stateLiveData

    fun loadLineData(lineId:Int = 0) {

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setsInteractor.loadLine(lineId).collect {
                    stateLiveData.postValue(it)
                }
            }.onFailure {
                stateLiveData.postValue(LineSetState.Error)
            }
        }

    }

    fun saveLine(constructorSetLine: ConstructorSetLine){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setsInteractor.saveLine(constructorSetLine)
            }.onFailure {
                stateLiveData.postValue(LineSetState.Error)
            }
        }
    }

}
