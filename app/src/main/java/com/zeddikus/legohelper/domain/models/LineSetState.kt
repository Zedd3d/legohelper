package com.zeddikus.legohelper.domain.models

sealed interface LineSetState {
    data class Data(val line: ConstructorSetLine): LineSetState

    data object Error: LineSetState
}