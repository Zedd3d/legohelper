package com.zeddikus.legohelper.domain.models

sealed interface SetsState {
    data class Data(val constructorSetList: List<ConstructorSet>) : SetsState

    object Error: SetsState
}

