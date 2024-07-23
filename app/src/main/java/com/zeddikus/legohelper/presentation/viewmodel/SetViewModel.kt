package com.zeddikus.legohelper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.network.NetworkInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetViewModel @Inject constructor(
    private val setsInteractor: SetsInteractor,
    private val networkInteractor: NetworkInteractor
) :
    BaseViewModel() {

    private val stateLiveData = MutableLiveData<SetState>()
    fun observeState(): LiveData<SetState> = stateLiveData

    private val stateSingleLiveData = MutableLiveData<Int>()
    fun observeSingleState(): LiveData<Int> = stateSingleLiveData

    fun updateSetData(setId:Int = 0) {
        if (setId == 0) {
            stateLiveData.postValue(SetState.Empty)
        } else {

            viewModelScope.launch(Dispatchers.IO) {
                runCatching {
                    setsInteractor.loadSet(setId).collect {
                        stateLiveData.postValue(it)
                    }
                }.onFailure {
                    stateLiveData.postValue(SetState.Error)
                }
            }
        }
    }

    fun saveSetData(setIdExt: String, setName: String){

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                doSaveSetData(setIdExt, setName)
            }.onFailure {
                stateLiveData.postValue(SetState.Error)
            }
        }
    }

    suspend fun doSaveSetData(setIdExt: String, setName: String){

        if (stateLiveData.value is SetState.Error) return

        val currentValue = stateLiveData.value
        val setId = if (currentValue is SetState.Data) {
            setsInteractor.saveSet(
                currentValue.set.copy(
                    setIdExt = setIdExt,
                    name = setName
                )
            )
        } else {
            setsInteractor.saveSet(
                ConstructorSet(
                    setIdExt = setIdExt,
                    name = setName
                )
            )
        }
        stateLiveData.postValue(SetState.Data(ConstructorSet(
            id = setId,
            setIdExt = setIdExt,
            name = setName
        )))
    }

    fun loadLines(setIdExt: String, setName: String){
        viewModelScope.launch(Dispatchers.IO) {
            doSaveSetData(setIdExt, setName)
            runCatching {
                networkInteractor.loadSet(setIdExt).collect{}
            }.onFailure {
                stateLiveData.postValue(SetState.Error)
            }

        }
    }

    fun goToCollect(setIdExt: String, setName: String){
        val cv = stateLiveData.value
        if (cv is SetState.Data)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                doSaveSetData(setIdExt, setName)
                stateSingleLiveData.postValue(cv.set.id)
                delay(400)
                stateSingleLiveData.postValue(0)
            }.onFailure {
                stateLiveData.postValue(SetState.Error)
            }
        }
    }

    fun clearGoToValue(){
        stateSingleLiveData.postValue(0)
    }
}
