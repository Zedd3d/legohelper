package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.domain.models.LineSetState
import com.zeddikus.legohelper.domain.models.LinesState
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

    override suspend fun saveSet(constructorSet: ConstructorSet): Int {
        return repository.saveSet(constructorSet)
    }

    override suspend fun loadLine(lineId: Int): Flow<LineSetState> {
        return repository.loadLine(lineId)
    }

    override suspend fun loadLines(setId: Int, hideCollected: Boolean): Flow<LinesState> {
        return repository.loadLines(setId, hideCollected)
    }

    override suspend fun saveLine(constructorSetLine: ConstructorSetLine): Flow<LineSetState> {
        return repository.saveLineSet(constructorSetLine)
    }

    override suspend fun deleteSet(setId: Int): Flow<SetState> {
        return repository.deleteSet(setId)
    }
}