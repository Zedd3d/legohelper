package com.zeddikus.legohelper.domain.models

sealed interface LinesState {
    data class Data(val list: List<ConstructorSetLine>): LinesState

    data object Error: LinesState
}