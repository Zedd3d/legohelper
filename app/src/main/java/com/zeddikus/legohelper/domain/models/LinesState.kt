package com.zeddikus.legohelper.domain.models

import com.zeddikus.legohelper.di.ErrorTypes

sealed interface LinesState {

    data class Data(val list: List<ConstructorSetLine>,
                    val spanCount: Settings.Columns,
                    val sort: Settings.Sort,
                    val hideCollected: Boolean = false): LinesState

    data class Error(val errorType: ErrorTypes): LinesState
}