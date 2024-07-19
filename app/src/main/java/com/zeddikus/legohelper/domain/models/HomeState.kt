package com.zeddikus.legohelper.domain.models

sealed interface HomeState {
    data class Data(val constructorSetList: List<ConstructorSet>) : HomeState

    object Error: HomeState
}

