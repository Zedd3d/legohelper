package com.zeddikus.legohelper.domain.network

import com.zeddikus.legohelper.domain.models.SetState
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    suspend fun loadSet(setId: String): Flow<SetState>
}