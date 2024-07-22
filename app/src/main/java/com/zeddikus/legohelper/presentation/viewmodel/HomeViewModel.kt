package com.zeddikus.legohelper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.models.SetsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val setsInteractor: SetsInteractor
) :
    BaseViewModel() {

    private val stateLiveData = MutableLiveData<SetsState>()
    fun observeState(): LiveData<SetsState> = stateLiveData
    fun updateSetList() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                try {
                    setsInteractor.loadSets().collect {
                        stateLiveData.postValue(it)
                    }
                } catch (e: Exception){
                    print(e.printStackTrace())
                }

            }.onFailure {
                stateLiveData.postValue(SetsState.Error)
            }
        }
    }
}
