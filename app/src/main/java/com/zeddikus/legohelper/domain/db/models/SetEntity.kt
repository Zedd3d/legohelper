package com.zeddikus.legohelper.domain.db.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "sets_table")
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int, // автоматический ИД
    val setIdExt: String, // ИД набора по классификации Лего
    val name: String // Название набора
)

@Entity(tableName = "setlines_table")
data class SetLineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Просто номер строки табличной части
    val setId: Int, // ИД набора, к которому относится эта строка
    val partId: String, // ИД детали
    val count: Int, // Количество необходимых деталей
    val countFound: Int // Количество найденных деталей
)

@Entity(tableName = "parts_table")
data class PartEntity(
    @PrimaryKey
    val id: String,  // ИД детали
    val name: String, // Имя детали Лего
    val imgUrl: String, // Картинка детали в формате Base 64
    val colorCode: String // Код цвета детали
)

// DTO для представления полного набора с его строками
data class SetWithLines(
    @Embedded val set: SetEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "setId"
    )
    val lines: List<SetLineEntity> = emptyList()
)

// DTO для представления строки набора с деталью
data class SetLineWithPart(
    @Embedded val setLine: SetLineEntity,
    @Relation(
        parentColumn = "partId",
        entityColumn = "id"
    )
    val part: PartEntity
)

data class SetWithLinesWithPart(
    val set: SetEntity,
    val lines: List<SetLineWithPart>
)