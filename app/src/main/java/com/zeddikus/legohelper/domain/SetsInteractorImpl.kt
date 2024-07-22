package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.models.SetsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetsInteractorImpl @Inject constructor(
    private val repository: SetsRepository,
): SetsInteractor {

    override suspend fun loadSets(): Flow<SetsState> {
        return repository.loadSets()
    }

    override suspend fun loadSet(setId: Int): Flow<SetState> {
        return repository.loadSet(setId)
    }

    override suspend fun saveSet(constructorSet: ConstructorSet) {
        repository.saveSet(constructorSet)
    }
}