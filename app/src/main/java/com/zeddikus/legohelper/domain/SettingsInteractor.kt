package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.domain.models.LineSetState
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.models.SetsState
import com.zeddikus.legohelper.domain.models.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsInteractor {
    suspend fun loadSort(): Settings

    suspend fun loadColumnCount(): Settings

    suspend fun saveSort(settings: Settings)

    suspend fun saveColumn(settings: Settings)
}