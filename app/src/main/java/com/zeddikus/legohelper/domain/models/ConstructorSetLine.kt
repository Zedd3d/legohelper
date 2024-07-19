package com.zeddikus.legohelper.domain.models

data class ConstructorSetLine(
    val part: ConstructorPart,
    val count: Int, //Количество требуемых деталей
    val countFound: Int // Количество найденных деталей
)
