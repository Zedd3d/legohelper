package com.zeddikus.legohelper.domain.models

data class ConstructorPart (
    val id: String,
    val name: String,
    val imgUrl: String = "", //Адрес картинки
    var colorCode: String = "", //Код цвета
)
