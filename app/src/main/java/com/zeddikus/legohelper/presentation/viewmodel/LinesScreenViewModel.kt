package com.zeddikus.legohelper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.network.NetworkInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LinesScreenViewModel @Inject constructor(
    private val setsInteractor: SetsInteractor,
) :
    BaseViewModel() {

    private val stateLiveData = MutableLiveData<LinesState>()
    fun observeState(): LiveData<LinesState> = stateLiveData

    fun loadLines(setId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setsInteractor.loadSet(setId).collect {
                    when (it) {
                        is SetState.Data -> stateLiveData.postValue(LinesState.Data(it.set.lines.map{it.value}))
                        SetState.Empty -> stateLiveData.postValue(LinesState.Data(listOf()))
                        SetState.Error -> stateLiveData.postValue(LinesState.Error)
                    }
                }
            }.onFailure {
                stateLiveData.postValue(LinesState.Error)
            }
        }
    }

}
