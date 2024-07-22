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
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetViewModel @Inject constructor(
    private val setsInteractor: SetsInteractor,
    private val networkInteractor: NetworkInteractor
) :
    BaseViewModel() {

    private val stateLiveData = MutableLiveData<SetState>()
    fun observeState(): LiveData<SetState> = stateLiveData

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
                val currentValue = stateLiveData.value
                if (currentValue is SetState.Data) {
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
            }.onFailure {
                stateLiveData.postValue(SetState.Error)
            }
        }

    }

    fun loadLines(setNumberExt: String){
        viewModelScope.launch(Dispatchers.IO) {
//            runCatching {
//                networkInteractor.loadSet(setNumberExt).collect{
//                    val result = it
//                    val b=0
//                }
//            }.onFailure {
//                val a=1
//                print(it.stackTrace)
//            }
            run {
                networkInteractor.loadSet(setNumberExt).collect{
                    val result = it
                    val b=0
                }
            }
        }
    }
}
