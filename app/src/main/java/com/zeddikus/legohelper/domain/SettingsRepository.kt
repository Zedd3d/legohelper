package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun loadSort(): Settings

    suspend fun loadColumnCount(): Settings

    suspend fun saveSort(settings: Settings)

    suspend fun saveColumn(settings: Settings)
}