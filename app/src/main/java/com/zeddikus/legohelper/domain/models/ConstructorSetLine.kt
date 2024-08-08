package com.zeddikus.legohelper.domain.models

data class ConstructorSetLine(
    val lineId: Int,
    val setId: Int,
    var part: ConstructorPart,
    var count: Int, //Количество требуемых деталей
    var countFound: Int, // Количество найденных деталей
)
