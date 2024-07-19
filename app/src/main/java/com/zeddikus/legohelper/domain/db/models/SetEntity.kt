package com.zeddikus.legohelper.domain.db.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "sets_table")
data class SetEntity(
    @PrimaryKey
    val id: String, // ИД набора
    val name: String // Название набора
)

@Entity(tableName = "setlines_table")
data class SetLineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Просто номер строки табличной части
    val setId: String, // ИД набора, к которому относится эта строка
    val partId: String, // ИД детали
    val count: Int, // Количество необходимых деталей
    val countFound: Int // Количество найденных деталей
)

@Entity(tableName = "parts_table")
data class PartEntity(
    @PrimaryKey
    val id: String,  // ИД детали
    val name: String, // Имя детали Лего
    val data: String, // Картинка детали в формате Base 64
    val colorCode: String // Код цвета детали
)

// DTO для представления полного набора с его строками
data class SetWithLines(
    @Embedded val set: SetEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "setId"
    )
    val lines: List<SetLineEntity>
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