package com.zeddikus.legohelper.domain.models

import com.zeddikus.legohelper.di.ErrorTypes

sealed interface SetState {
    data class Data(val set: ConstructorSet): SetState

    data object Empty: SetState

    data class Error(val errorType: ErrorTypes): SetState

    data object Deleted: SetState
}
