package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.HomeState
import kotlinx.coroutines.flow.Flow

interface SetsRepository {
    suspend fun loadSets(): Flow<HomeState>

    suspend fun saveSet(constructorSet: ConstructorSet)
}