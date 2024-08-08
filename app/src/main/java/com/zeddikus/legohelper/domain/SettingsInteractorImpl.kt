package com.zeddikus.legohelper.domain

import com.zeddikus.legohelper.domain.models.ConstructorSet
import com.zeddikus.legohelper.domain.models.ConstructorSetLine
import com.zeddikus.legohelper.domain.models.LineSetState
import com.zeddikus.legohelper.domain.models.LinesState
import com.zeddikus.legohelper.domain.models.SetState
import com.zeddikus.legohelper.domain.models.SetsState
import com.zeddikus.legohelper.domain.models.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingsInteractorImpl @Inject constructor(
    private val repository: SettingsRepository,
): SettingsInteractor {
    override suspend fun loadSort(): Settings {
        return repository.loadSort()
    }

    override suspend fun loadColumnCount(): Settings {
        return repository.loadColumnCount()
    }

    override suspend fun saveSort(settings: Settings) {
        repository.saveSort(settings)
    }

    override suspend fun saveColumn(settings: Settings) {
        repository.saveColumn(settings)
    }
}