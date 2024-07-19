package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.HomeState
import kotlinx.coroutines.flow.Flow

interface LoadSetsUseCase {
    suspend fun loadSets(): Flow<HomeState>
}