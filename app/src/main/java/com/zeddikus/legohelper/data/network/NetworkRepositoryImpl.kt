package com.zeddikus.legohelper.data.network

import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.network.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val api: LegoBrickApi
) : NetworkRepository {
    override suspend fun loadSet(setId: String): Flow<SetState> = flow {

        val idForRequest = if (setId.contains("-")) {setId} else {setId + "-1"}

        val response = api.getStartData(idForRequest)
        val b = response.body()?.string()?:""
        handleBody(b)
        emit(SetState.Empty)
    }

    private fun handleBody(b: String){
        val a = 1
    }
}