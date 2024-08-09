package com.zeddikus.legohelper.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zeddikus.legohelper.base.BaseViewModel
import com.zeddikus.legohelper.di.ErrorTypes
import com.zeddikus.legohelper.domain.SetsInteractor
import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.network.NetworkInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConstructorSetViewModel @Inject constructor(
    private val setsInteractor: SetsInteractor,
    private val networkInteractor: NetworkInteractor
) :
    BaseViewModel() {

    private var currentSetId: Int = 0

    private val stateLiveData = MutableLiveData<SetState>()
    fun observeState(): LiveData<SetState> = stateLiveData

    private val stateSingleLiveData = MutableLiveData<Int>()
    fun observeSingleState(): LiveData<Int> = stateSingleLiveData

    fun loadSetData(setId:Int = 0) {
        if (setId == 0) {
            stateLiveData.postValue(SetState.Empty)
        } else {

            viewModelScope.launch(Dispatchers.IO) {
                runCatching {
                    setsInteractor.loadSet(setId).collect {
                        if (it is SetState.Data){
                            currentSetId = it.set.id
                        }
                        stateLiveData.postValue(it)
                    }
                }.onFailure {
                    stateLiveData.postValue(SetState.Error(ErrorTypes.Unknown))
                }
            }
        }
    }

    fun updateSetData() {
        loadSetData(currentSetId)
    }

    fun saveSetData(setIdExt: String, setName: String){

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                doSaveSetData(setIdExt, setName)
            }.onFailure {
                stateLiveData.postValue(SetState.Error(ErrorTypes.Unknown))
            }
        }
    }

    suspend fun doSaveSetData(setIdExt: String, setName: String, isLoading: Boolean = false){

        if (stateLiveData.value is SetState.Error) return

        val currentValue = stateLiveData.value
        currentSetId = if (currentValue is SetState.Data) {
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
        if (!isLoading) {
            stateLiveData.postValue(
                SetState.Data(
                    ConstructorSet(
                        id = currentSetId,
                        setIdExt = setIdExt,
                        name = setName
                    )
                )
            )
        }
    }

    fun loadFromBrickLink(setIdExt: String, setName: String){
        viewModelScope.launch(Dispatchers.IO) {
            doSaveSetData(setIdExt, setName, true)
//            runCatching {
                networkInteractor.loadSet(setIdExt, currentSetId).collect{
                    stateLiveData.postValue(it)
                }
//            }.onFailure {
//                stateLiveData.postValue(SetState.Error(ErrorTypes.Unknown))
//            }

        }
    }

    fun goToCollect(setIdExt: String, setName: String){
        if (currentSetId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                runCatching {
                    doSaveSetData(setIdExt, setName)
                    stateSingleLiveData.postValue(currentSetId)
                    delay(400)
                    stateSingleLiveData.postValue(0)
                }.onFailure {
                    stateLiveData.postValue(SetState.Error(ErrorTypes.Unknown))
                }
            }
        }
    }

    fun clearGoToValue(){
        stateSingleLiveData.postValue(0)
        //stateLiveData.postValue(stateLiveData.value)
    }

    fun deleteSet(){
        if (currentSetId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                runCatching {
                    setsInteractor.deleteSet(currentSetId).collect {
                        stateLiveData.postValue(it)
                    }
                }.onFailure {
                    stateLiveData.postValue(SetState.Error(ErrorTypes.Unknown))
                }
            }
        } else {
            stateLiveData.postValue(SetState.Deleted)
        }
    }
}
