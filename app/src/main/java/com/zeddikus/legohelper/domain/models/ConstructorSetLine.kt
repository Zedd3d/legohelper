package com.zeddikus.legohelper.domain.models

data class ConstructorSetLine(
    val lineId: Int,
    val setId: Int,
    val part: ConstructorPart,
    var count: Int, //Количество требуемых деталей
    var countFound: Int, // Количество найденных деталей
)
