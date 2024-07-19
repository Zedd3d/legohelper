package com.zeddikus.legohelper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.domain.LoadSetsUseCase
import com.zeddikus.legohelper.domain.models.HomeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val useCase: LoadSetsUseCase
) :
    BaseViewModel() {

    private val stateLiveData = MutableLiveData<HomeState>()
    fun observeState(): LiveData<HomeState> = stateLiveData
    fun updateSetList() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                useCase.loadSets().collect {
                    stateLiveData.postValue(it)
                }
            }.onFailure {
                stateLiveData.postValue(HomeState.Error)
            }
        }
    }
}
