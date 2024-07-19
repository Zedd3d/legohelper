package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.HomeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadSetsUseCaseImpl @Inject constructor(
    private val repository: SetsRepository,
): LoadSetsUseCase {

    override suspend fun loadSets(): Flow<HomeState> = flow {
        repository.loadSets()
    }
}