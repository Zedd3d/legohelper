package com.zeddikus.legohelper.domain.models

data class ConstructorSet(
    val id: String,
    val name: String,
    val lines: List<ConstructorSetLine> = emptyList()
)
