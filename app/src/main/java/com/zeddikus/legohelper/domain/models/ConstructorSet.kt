package com.zeddikus.legohelper.domain.models

data class ConstructorSet(
    val id: Int = 0,
    val setIdExt: String,
    val name: String,
    val lines: MutableMap<String, ConstructorSetLine> = mutableMapOf()
)
