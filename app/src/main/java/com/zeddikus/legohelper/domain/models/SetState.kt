package com.zeddikus.legohelper.domain.models

sealed interface SetState {
    data class Data(val set: ConstructorSet): SetState

    data object Empty: SetState

    data object Error: SetState
}
