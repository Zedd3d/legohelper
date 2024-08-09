package com.zeddikus.legohelper.domain.network

import com.zeddikus.legohelper.domain.models.SetState
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    suspend fun loadSet(setLegoId: String, setId: Int): Flow<SetState>
}