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

    private var currentLine: ConstructorSetLine? = null

    private val stateLiveData = MutableLiveData<LineSetState>()
    fun observeState(): LiveData<LineSetState> = stateLiveData

    fun loadLineData(lineId:Int = 0) {

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setsInteractor.loadLine(lineId).collect {
                    if (it is LineSetState.Data) {
                        currentLine = it.line
                    }
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
                setsInteractor.saveLine(constructorSetLine).collect{}
            }.onFailure {
                stateLiveData.postValue(LineSetState.Error)
            }
        }
    }

    fun setCountFromButton(countFound: Int){
        currentLine?.let {
            applyChanges(countFound,it.count,it)
        }
    }

    fun changeCountFromButton(change: Int){
        currentLine?.let {
            var resultInt = it.countFound + change
            applyChanges(resultInt,it.count,it)
        }
    }

    fun applyChanges(countFound: Int, count: Int, line: ConstructorSetLine){
        var resultInt = countFound
        if (resultInt<0) {resultInt = 0} else {}
        if (resultInt>count) {resultInt = count} else {}
        val result = line.copy(countFound = resultInt)
        currentLine = result
        saveLine(result)
        stateLiveData.postValue(LineSetState.Data(result))
    }
}
