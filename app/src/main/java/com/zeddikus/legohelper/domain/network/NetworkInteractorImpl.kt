package com.zeddikus.legohelper.domain.network

import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.models.SetsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkInteractorImpl @Inject constructor(
    private val repository: NetworkRepository,
): NetworkInteractor {

    override suspend fun loadSet(setId: String): Flow<SetState> {
        return repository.loadSet(setId)
    }
}