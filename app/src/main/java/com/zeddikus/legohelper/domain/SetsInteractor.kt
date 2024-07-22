package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.models.SetsState
import kotlinx.coroutines.flow.Flow

interface SetsInteractor {
    suspend fun loadSets(): Flow<SetsState>

    suspend fun loadSet(setId: Int): Flow<SetState>

    suspend fun saveSet(constructorSet: ConstructorSet)
}