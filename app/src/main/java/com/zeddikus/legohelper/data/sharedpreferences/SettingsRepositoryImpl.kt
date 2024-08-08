package com.zeddikus.legohelper.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zeddikus.legohelper.domain.SettingsRepository
import com.zeddikus.legohelper.domain.models.FieldsForSort
import com.zeddikus.legohelper.domain.models.Settings
import com.zeddikus.legohelper.domain.models.getGroupSorts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val gson: Gson,
    private val sharedPrefs: SharedPreferences
): SettingsRepository {
    override suspend fun loadSort(): Settings {
        val default = gson.toJson(
            getGroupSorts(context)[0]
        )
        return gson.fromJson<Settings.Sort>(
            sharedPrefs.getString(SP_SORT, default) ?: default,
            object : TypeToken<Settings.Sort>() {}.type
        )
    }

    override suspend fun loadColumnCount(): Settings {

        val default = gson.toJson(
            Settings.Columns(
                4
            ))

        return gson.fromJson<Settings.Columns>(
            sharedPrefs.getString(SP_COLUMN_COUNT, default) ?: default,
            object : TypeToken<Settings.Columns>() {}.type
        )
    }

    override suspend fun saveSort(settings: Settings) {
        val json = gson.toJson(settings)
        sharedPrefs.edit().putString(SP_SORT, json).apply()
    }

    override suspend fun saveColumn(settings: Settings) {
        val json = gson.toJson(settings)
        sharedPrefs.edit().putString(SP_COLUMN_COUNT, json).apply()
    }

    companion object {
        const val SP_SORT = "sp_sort"
        const val SP_COLUMN_COUNT = "sp_column_count"
    }
}