package com.zeddikus.legohelper.domain.models

import android.content.Context
import com.zeddikus.legohelper.R

sealed interface Settings{
    data class Sort(
        val field: String,
        val direction: String, //Уменьшение DESC Увеличение ASC
        val description: String
    ): Settings

    data class Columns (val count: Int): Settings
}

enum class FieldsForSort() {
    NAME,
    COUNT,
    COUNT_FIND,
    NUMBER,
    COLOR;
}

fun getGroupSorts(context: Context): List<Settings.Sort> {
    var result = mutableListOf<Settings.Sort>()

    for (fieldForSort in FieldsForSort.entries) {
        val descr = when (fieldForSort) {
            FieldsForSort.NAME -> context.getString(R.string.fieldName)
            FieldsForSort.COUNT -> context.getString(R.string.fieldCount)
            FieldsForSort.COUNT_FIND -> context.getString(R.string.fieldCountFind)
            FieldsForSort.NUMBER -> context.getString(R.string.fieldNumber)
            FieldsForSort.COLOR -> context.getString(R.string.fieldColor)
        }

        val field = when (fieldForSort) {
            FieldsForSort.NAME -> "TP.name"
            FieldsForSort.COUNT -> "T.count"
            FieldsForSort.COUNT_FIND -> "T.countFound"
            FieldsForSort.NUMBER -> "T.partId"
            FieldsForSort.COLOR -> "TP.colorCode"
        }

        result.add(
            Settings.Sort(
                field,
                "ASC",
                "$descr "+context.getString(R.string.sortUp)
            )
        )
        result.add(
            Settings.Sort(
                field,
                "DESC",
                "$descr "+context.getString(R.string.sortDown)
            )
        )
    }
    return result
}